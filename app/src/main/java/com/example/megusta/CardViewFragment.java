package com.example.megusta;
/**
 * Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class CardViewFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_FILTER = "filter-arg";
    private static final String ARG_VALUE = "value-arg";
    private int mColumnCount = 2;
    private List<Item> items = new ArrayList<>();
    private List<Item> filtered_items;
    private List<Item> full_list;
    private RecyclerView list;
    private String filter;
    private String value;
    private MyItemCardViewAdapter adapter = new MyItemCardViewAdapter();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardViewFragment() {
    }

    public static CardViewFragment newInstance(int columnCount, String filter, String value) {
        CardViewFragment fragment = new CardViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_FILTER, filter);
        args.putString(ARG_VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The LIKE of the project, search the items on screen.
     * filters the items.
     *
     * @param query name of item to search by.
     */
    public void searchByNameLocation(String query) {
        items.clear();//reset list to original full list.
        if (query != "") {
            filtered_items = new ArrayList<>();
            for (Item item : full_list) {
                if (item.getName().indexOf(query) != -1 || item.getLocation().indexOf(query) != -1)
                    filtered_items.add(item);
            }
            items.addAll(filtered_items); //input the list with the filtered values.
        } else { //empty search resets the array.
            items.addAll(full_list); //reset list to original state.
        }
        adapter.setValues(items);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            filter = getArguments().getString(ARG_FILTER);
            value = getArguments().getString(ARG_VALUE);
        }
        loadUserItems(FirebaseFirestore.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list);
        // Set the adapter
        if (mColumnCount <= 1) {
            list.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            list.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        list.setAdapter(adapter);
        if (filter.equals("email")) {
            adapter.setClick(item -> showDeleteDialog(item));
        }
    }

    private void showDeleteDialog(Item item) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Are you sure?")
                .setMessage("If you click ok the item will be removed.")
                .setPositiveButton("OK", (dialog12, which) -> {
                    deleteItem(item);
                    dialog12.dismiss();
                })
                .setNegativeButton(
                        "Cancel",
                        (dialog1, which) -> dialog1.dismiss()
                )
                .create();
        dialog.show();
    }

    private void deleteItem(Item item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(item.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Successfully deleted"
                                , Toast.LENGTH_LONG).show();
                        items.remove(item);
                        adapter.setValues(items);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "couldn't delete"
                                , Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loadUserItems(FirebaseFirestore db) {
        db.collection("items")
                .whereEqualTo(filter, value)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "load items";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            full_list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                items.add(new Item(document));
                            }
                            //create backup for search
                            full_list.addAll(items);
                            adapter.setValues(items);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}