package com.example.megusta;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    private final TextView name;
    private final TextView category;
    private ImageView item_photo;
    private final TextView text_photo;
    private final TextView rent;
    private final TextView price;
    private final TextView location;
    private final TextView phone;
    private final TextView date;
    private final TextView user_name;

    public ViewHolder(View view) {
        super(view);
        mView = view;
        user_name = mView.findViewById(R.id.item_username_card);
        name = mView.findViewById(R.id.item_name_card);
        category = mView.findViewById(R.id.item_category_card);
        item_photo = mView.findViewById(R.id.item_photo_card);
        text_photo = mView.findViewById(R.id.item_photo_text_card);
        item_photo = mView.findViewById(R.id.item_photo_card);
        rent = mView.findViewById(R.id.item_rent_card);
        price = mView.findViewById(R.id.item_price_card);
        location = mView.findViewById(R.id.item_location_card);
        phone = mView.findViewById(R.id.item_phone_card);
        date = mView.findViewById(R.id.item_date_card);
    }
    public void bind(Item item) {
        date.setText("date posted: " + item.getDate());
        user_name.setText("username: " + item.getUser_name());
        name.setText("name: " + item.getName());
        text_photo.setText("photo: ");
        //if no placeholder text.
        if (item.getItem_photo().contains("https://firebasestorage.googleapis.com")) {
            Glide.with(itemView.getContext()).load(item.getItem_photo())
                    .into(item_photo);
        } else {
            Glide.with(itemView.getContext())
                    .load(R.drawable.placeholder)
                    .into(item_photo);
        }
        category.setText("category: " + item.getCategory());
        if (item.isRent())
            rent.setText("rent: yes" );
        else
            rent.setText("rent: no" );
        price.setText("price: " + item.getPrice() + " ₪");
        location.setText("location: " + item.getLocation());
        phone.setText("phone: " + item.getPhone());
    }
}
