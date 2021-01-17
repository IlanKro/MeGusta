package com.example.megusta;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class CardViewFragment extends Fragment {


    protected static final String ARG_COLUMN_COUNT = "column-count";

    protected int mColumnCount = 2;
    protected List<Item> items = new ArrayList<>();
    protected RecyclerView list;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardViewFragment() {
    }


    public static CardViewFragment newInstance(int columnCount) {
        CardViewFragment fragment = new CardViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUserItems(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseFirestore.getInstance());
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        list = view.findViewById(R.id.list);
        // Set the adapter
        if (mColumnCount <= 1) {
            list.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            list.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        return view;
    }

    public void loadUserItems(String email, FirebaseFirestore db){
      db.collection("items")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "load items";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                items.add(new Item(document));
                            }
                            list.setAdapter(new MyItemCardViewAdapter(items));
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}