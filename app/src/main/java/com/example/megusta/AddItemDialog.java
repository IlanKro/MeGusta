package com.example.megusta;

import android.app.Dialog;

import android.content.Context;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AddItemDialog extends Dialog implements View.OnClickListener {
    private EditText name,price,location,phone;
    private ImageView photo;
    //spinner
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String category;
    private CheckBox rent;
    private Button btn_cancel,btn_add,btn_img;
    private final Context context;
    //firebase:
    private final FirebaseAuth mAuth;
    private final FirebaseUser current_user;
    private final FirebaseFirestore db;
    private final String TAG="add_item";

    public AddItemDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        current_user=mAuth.getCurrentUser();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    public void CreateDialog(@NonNull Context context) {
        setContentView(R.layout.create_item_dialog);
        setTitle("add item");
        setCanceledOnTouchOutside(false);
        name= findViewById(R.id.item_name);
        createSpinner();
        //Set category as the first item since it's the default that is displayed to the user.
        category=context.getResources().getStringArray(R.array.category)[0];
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
        this.show();
    }

    private void createSpinner() {
        spinner = (Spinner) findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(context,R.array.category,android.R.layout.simple_spinner_item);
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
                    this.dismiss();
                }
            }
            catch(ValidationException exception){
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
        else if(view==btn_cancel) {
            this.dismiss();
        }
        else if(view==btn_img) {
            selectImage();
        }
    }

    private void selectImage() {

    }

    private void addItem() {
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

