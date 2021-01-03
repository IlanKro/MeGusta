package com.example.megusta;

import android.app.Dialog;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;

public class AddItemDialog extends Dialog implements View.OnClickListener {
    private EditText name;
    private ImageView photo;
    private Button btn_img;
    private EditText price;
    //spinner
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String category;

    private CheckBox rent;
    private EditText location;
    private EditText phone;
    private Button btn_add;
    private Button btn_cancel;
    private Context context;
    public AddItemDialog(@NonNull Context context) {
        super(context);
        this.context=context;
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

    public Boolean validateItem() throws ValidationException {
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view==btn_add) {
            try {
                if (validateItem()) { //check if the validation is right.
                    //TODO add to database
                    Toast.makeText(context, "Item added successfully", Toast.LENGTH_LONG).show();
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
        /*
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    context.startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        */
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

