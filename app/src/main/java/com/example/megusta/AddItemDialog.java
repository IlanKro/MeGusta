package com.example.megusta;

import android.app.Dialog;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class AddItemDialog extends Dialog implements View.OnClickListener {
    private EditText name;
    private ImageView photo;
    private EditText price;
    private Spinner category;
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

        name= this.findViewById(R.id.item_name);
        category= this.findViewById(R.id.category);
        photo= this.findViewById(R.id.photo);
        rent=this.findViewById(R.id.rent);
        location=this.findViewById(R.id.item_location);
        phone=this.findViewById(R.id.item_phone);
        btn_add= this.findViewById(R.id.btnDialogAdd);
        btn_add.setOnClickListener(this);
        btn_cancel= this.findViewById(R.id.btnDialogCancel);
        btn_cancel.setOnClickListener(this);
        this.show();
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
    }
}

