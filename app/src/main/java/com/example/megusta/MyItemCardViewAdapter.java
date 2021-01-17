package com.example.megusta;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MyItemCardViewAdapter extends RecyclerView.Adapter<MyItemCardViewAdapter.ViewHolder> {

    private final List<Item> mValues;

    public MyItemCardViewAdapter(List<Item> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Item mItem;
        private TextView name;
        private TextView category;
        private ImageView item_photo;
        private TextView rent;
        private TextView price;
        private TextView location;
        private TextView phone;
        private TextView date;
        private TextView user_name;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            user_name=  mView.findViewById(R.id.item_username_card);
            name= mView.findViewById(R.id.item_name_card);
            category=  mView.findViewById(R.id.item_category_card);
            item_photo=  mView.findViewById(R.id.item_photo_card);
            rent=  mView.findViewById(R.id.item_rent_card);
            price=  mView.findViewById(R.id.item_price_card);
            location=  mView.findViewById(R.id.item_location_card);
            phone=  mView.findViewById(R.id.item_phone_card);
            date=  mView.findViewById(R.id.item_date_card);
        }

        public void bind(Item item) {
            date.setText("date posted: " + item.getDate());
            user_name.setText("username: " + item.getUser_name());
            name.setText("name: " + item.getName());
            category.setText("category: " + item.getCategory());
            rent.setText("rent: " + item.isRent());
            price.setText("price: " + item.getPrice() + " ₪");
            location.setText("location: " + item.getLocation());
            phone.setText("phone: " + item.getPhone());

        }
    }
}