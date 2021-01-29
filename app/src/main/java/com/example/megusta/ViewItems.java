package com.example.megusta;
/** Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * View and delete items the user posted.
 */
public class ViewItems extends AppCompatActivity {
    private final CharSequence TITLE="View Items";

    /**
     * default constructor sets up the fragment that shows the user items.
     * @param savedInstanceState return the activity to how it was before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        setTitle(TITLE);
        initiateFragment();

    }
    /**
     * sets up the fragment.
     */
    private void initiateFragment() {
        String filter="email";
        String value=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.view_items, CardViewFragment
                        .newInstance(2,filter,value),null)
                .commit();
    }
}