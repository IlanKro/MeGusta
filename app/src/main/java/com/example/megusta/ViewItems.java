package com.example.megusta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewItems extends AppCompatActivity {
    private final CharSequence TITLE="View Items";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        setTitle(TITLE);
        //test=findViewById(R.id.test);
        //test.setText(Item.getItemById("ctxUAkmYyHck9OTuX1tB", FirebaseFirestore.getInstance()).toString());
        initiateFragment();

    }

    private void initiateFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.view_items, CardViewFragment
                        .newInstance(2),null)
                .commit();
    }


}