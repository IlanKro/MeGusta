package com.example.megusta;
/**
 * Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Main hub of the app.
 */
public class MainMenu extends AppCompatActivity
        implements View.OnClickListener, TabLayout.OnTabSelectedListener,
        android.widget.SearchView.OnQueryTextListener {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private FloatingActionButton add_item_btn;
    private TabLayout tab_layout;
    private CardViewFragment category_fragment;
    private final int VIEW_ITEMS = R.id.view_items_category; //the main view of the main menu.
    private SearchView search;

    /**
     * Initiates attributes of the activity when the activity is created.
     * @param savedInstanceState the state saved of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (mAuth.getCurrentUser() != null)
            getSupportActionBar().setTitle("hello, " + mAuth.getCurrentUser().getDisplayName());
        add_item_btn = findViewById(R.id.add_item_plus);
        add_item_btn.setOnClickListener(this);
        makeTabLayout();
        search = findViewById(R.id.search);
        search.setOnQueryTextListener(this);
    }

    /**
     * Creates the tab in the tab layout.
     */
    private void makeTabLayout() {
        tab_layout = findViewById(R.id.tabs);
        tab_layout.addTab(tab_layout.newTab().setText("Shoes"));
        tab_layout.addTab(tab_layout.newTab().setText("Shirts"));
        tab_layout.addTab(tab_layout.newTab().setText("Pants"));
        tab_layout.addTab(tab_layout.newTab().setText("Coats and Jackets"));
        tab_layout.addTab(tab_layout.newTab().setText("Dresses"));
        tab_layout.addTab(tab_layout.newTab().setText("Accessories"));
        //select the first one,more abstract than to point at "Shoes".
        initiateFragment((String) tab_layout
                .getTabAt(tab_layout.getSelectedTabPosition()).getText());
        tab_layout.addOnTabSelectedListener(this);
    }

    /**
     * creates the action bar items.
     * @param menu  menu to create
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * What should the activity do on start.
     * Check if a user is logged in.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    /**
     * Listener to buttons on the Appbar.
     * @param item the item pressed.
     * @return true to signal that the override listener handled the action.
     * otherwise default of interface @TabLayout.OnTabSelectedListener .
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.view_items:
                startActivity(new Intent(this, ViewItems.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * On click listener to buttons on screen.
     * @param view the view to listen to.
     */
    @Override
    public void onClick(View view) {
        if (view == add_item_btn) {
            startActivity(new Intent(this, AddItemActivity.class));
        }
    }

    /**
     * initiate the card view fragment.
     * @param category the category of fragment to initiate (essentially a filter).
     */
    private void initiateFragment(String category) {
        category_fragment = CardViewFragment
                .newInstance(2,"item_category",category);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(VIEW_ITEMS, category_fragment, null)
                .commit();
    }

    /**
     * What happens when you press those tabs on the main screen?
     * the items of that category (globally) show on screen.
     * @param tab the selected tab.
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        initiateFragment((String) tab.getText());
    }

    /**
     * what happens when we switch tabs: the previous fragment get destroyed to save ram.
     * @param tab the tab we were previously in.
     */
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        getSupportFragmentManager().beginTransaction().remove(category_fragment).commit();
    }

    /**
     * what happens when a tab is reselected: reset the fragment on screen.
     * @param tab the tab selected
     */
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        getSupportFragmentManager().beginTransaction().remove(category_fragment).commit();
        initiateFragment((String) tab.getText());
    }

    /**
     * search function listener.
     * as for this project there is only search by name/location globally.
     * @param query the query made
     * @return true, to indicate the functionallity has been changed.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        CardViewFragment frag = (CardViewFragment) getSupportFragmentManager().getFragments().get(0);
        frag.searchByNameLocation(query);
        search.setIconified(true);
        search.clearFocus();
        return true;
    }

    /**
     * This overrides the function from the listener interface when a new text is inputted     *
     * @param newText the new text input.
     * @return false cause the rest of the functionality should be default.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}