package com.example.megusta;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

public class Item {
    private String id;
    private String name;
    private String category;
    private String item_photo;
    private boolean rent;
    private int price;
    private String location;
    private String phone;
    private String email;
    private String date;
    private String user_name;
    private static final String TAG= "read item";

    public Item(String id, String name, String category, String item_photo, boolean rent,
                int price, String location, String phone, String email, String date,String user_name) {
        this.id=id;
        this.name = name;
        this.category = category;
        this.item_photo = item_photo;
        this.rent = rent;
        this.price = price;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.user_name=user_name;
    }

    public Item(DocumentSnapshot document) throws NullPointerException {
        this.id=document.getId();
        this.name = (String)document.getData().get("item_name");
        this.category = (String)document.getData().get("item_category");
        this.item_photo = (String)document.getData().get("item_photo");
        this.rent = (boolean)document.getData().get("rent");
        this.price = Integer.parseInt((String)document.getData().get("price"));
        this.location = (String)document.getData().get("location");
        this.phone = (String)document.getData().get("phone");
        this.email = (String)document.getData().get("email");
        this.date = (String)document.getData().get("date");
        this.user_name=(String)document.getData().get("user_name");
    }
    /*
    public static Item getItemById(String id,FirebaseFirestore db) {
        DocumentReference docRef = db.collection("items").document(id);
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return new Item(documentSnapshotTask.getResult());
    }
    */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getItem_photo() {
        return item_photo;
    }

    public boolean isRent() {
        return rent;
    }

    public int getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getUser_name() {
        return user_name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", item_photo='" + item_photo + '\'' +
                ", rent=" + rent +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
