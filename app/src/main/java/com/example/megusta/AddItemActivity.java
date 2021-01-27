package com.example.megusta;

import android.Manifest;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name,price,location,phone;
    private ImageView photo;
    //spinner
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String category;
    private CheckBox rent;
    private Button btn_cancel,btn_add,btn_img;
    //firebase:
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseFirestore db;
    private String TAG="add_item";
    private static final int TAKE_PHOTO = 0;
    private static final int PICK_GALLERY = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int GALLERY_REQUEST = 3;
    private final String PERMISSION_DENIED="Permission not granted can't upload photo this way, " +
            "please grant access in settings.";

    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        createInterface();
    }
    private void createInterface() {
        setContentView(R.layout.add_item_activity);
        setTitle("add item");
        name= findViewById(R.id.item_name);
        createSpinner();
        //Set category as the first item since it's the default that is displayed to the user.
        category=this.getResources().getStringArray(R.array.category)[0];
        photo= findViewById(R.id.photo);
        btn_img=findViewById(R.id.add_img_btn);
        btn_img.setOnClickListener(this);
        rent=findViewById(R.id.rent);
        price=findViewById(R.id.item_price);
        location=findViewById(R.id.item_location);
        phone=findViewById(R.id.item_phone);
        btn_add= findViewById(R.id.btnDialogAdd);
        btn_add.setOnClickListener(this);
        btn_cancel= findViewById(R.id.btnDialogCancel);
        btn_cancel.setOnClickListener(this);
    }

    private void createSpinner() {
        spinner = (Spinner) findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        EventHandler();
    }

    private Boolean validateItem() throws ValidationException {
        if(name.getText().toString().isEmpty() || location.getText().toString().isEmpty() ||
           phone.getText().toString().isEmpty() || price.getText().toString().isEmpty())
            throw new ValidationException("All fields are required!");
        else if(name.getText().toString().length()>32) {
            throw new ValidationException("Name is too long, the maximum is 32 characters.");
        }
        else if(location.getText().toString().length()>16) {
            throw new ValidationException("location is too long, the maximum is 16 characters.");
        }
        else if(price.getText().toString().length()>8) {
            throw new ValidationException("price is too long,the maximum is 8 characters, please don't sell stuff people can't afford.");
        }
        else if(phone.getText().toString().length()>16) {
            throw new ValidationException("phone is too long,the maximum is 16 characters"); //would put a phone validator but for this project this is enough.
        }
        else if(Integer.parseInt(price.getText().toString())<0)
            throw new ValidationException("price can't be negative!");
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view==btn_add) {
            try {
                if (validateItem()) { //check if the validation is right.
                    addItem();
                }
            }
            catch(ValidationException exception){
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
        else if(view==btn_cancel) {
            startActivity(new Intent(this,MainMenu.class));
        }
        else if(view==btn_img) {
            selectImage();
        }
    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        final Context context=this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if(checkPermission(options[item])) {
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, TAKE_PHOTO);
                    }
                    else { //in case permission is not granted
                        dialog.dismiss();
                        Toast.makeText(context,PERMISSION_DENIED,Toast.LENGTH_LONG).show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission(options[item])) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_GALLERY);
                    }
                    else { //in case permission is not granted
                        dialog.dismiss();
                        Toast.makeText(context,PERMISSION_DENIED,Toast.LENGTH_LONG).show();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean checkPermission(CharSequence option) {
        if (option.equals("Take Photo")) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
                return true; //permission already present
            }
            else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST);
            }
        }
        else //Choose from Gallery
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else if (shouldShowRequestPermissionRationale
                    (Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY_REQUEST);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            ImageView imageView=findViewById(R.id.photo);
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case PICK_GALLERY:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn,
                                    null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==CAMERA_REQUEST && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, TAKE_PHOTO);
        }
        else if (requestCode==PICK_GALLERY && grantResults[0]==PackageManager.PERMISSION_GRANTED                   ) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_GALLERY);
        }
        if(grantResults[0]==PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this,PERMISSION_DENIED,Toast.LENGTH_LONG).show();
        }
    }

    private void addItem() {
        Context context=this;
        Map<String,Object> item = new HashMap<>();
        item.put("user_name",current_user.getDisplayName());
        item.put("item_name",name.getText().toString());
        item.put("item_category",category);
        item.put("item_photo","FAKE_PATH"); //TODO: add image path.
        item.put("rent",rent.isChecked());
        item.put("price",price.getText().toString());
        item.put("location",location.getText().toString());
        item.put("phone",phone.getText().toString());
        item.put("email",current_user.getEmail());
        item.put("date", GregorianCalendar.getInstance().getTime().toString());

        db.collection("items")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(context, "Item added successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(context,MainMenu.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(context, "An error has occurred while adding item", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void EventHandler() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
                category = "";
            }
        });
    }
}

