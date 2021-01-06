package com.example.megusta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ViewItems extends AppCompatActivity {
    private final CharSequence TITLE="View Items";
    private ViewGroup items;
    private GridLayout grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        setTitle(TITLE);
        LoadItems();
    }

    private void LoadItems() {
        CardView card;
        LayoutInflater inflater = LayoutInflater.from(this);
        card=(CardView) inflater.inflate(R.layout.card_view,null);

        items.addView(card);
        grid=findViewById(R.id.grid);


    }
}