package com.example.megusta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import  androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//TODO every card will have username,photo,price,rent/sell,location,phone number
public class MainMenu extends AppCompatActivity implements View.OnClickListener,TabLayout.OnTabSelectedListener{
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private FloatingActionButton add_item_btn;
    private TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mAuth=FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (mAuth.getCurrentUser()!=null)
            getSupportActionBar().setTitle("hello, " +mAuth.getCurrentUser().getDisplayName());
        add_item_btn=findViewById(R.id.add_item_plus);
        add_item_btn.setOnClickListener(this);

        tab_layout=findViewById(R.id.tabs);
        tab_layout.addTab(tab_layout.newTab().setText("Shoes"));
        tab_layout.addTab(tab_layout.newTab().setText("Shirts"));
        tab_layout.addTab(tab_layout.newTab().setText("Pants"));
        tab_layout.addTab(tab_layout.newTab().setText("Coats and Jackets"));
        tab_layout.addTab(tab_layout.newTab().setText("Dresses"));
        tab_layout.addTab(tab_layout.newTab().setText("Accessories"));
        tab_layout.addOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void updateUI(FirebaseUser user) {

    }

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

    @Override
    public void onClick(View view) {

        if (view == add_item_btn) {
            AddItemDialog dia = new AddItemDialog(this);
            dia.CreateDialog(this);
        }
        /*
        else if (view==shoes) {
            initiateFragment( R.id.shoes_tab,"Shoes");
        }
        else if (view==shirts) {
            initiateFragment(R.id.shirts_tab,"Shirts");
        }
        else if (view==pants) {
            initiateFragment(R.id.pants_tab,"Pants");
        }
        else if (view==coats_jackets) {
            initiateFragment(R.id.coats_jackets_tab,"Coats and Jackets");
        }
        else if (view==accessories) {
            initiateFragment(R.id.accessories_tab,"Accessories");
        }
         */
    }

    private void initiateFragment(int id,String category) {
        CardViewFragmentCategory.setCategory(category);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(id, CardViewFragmentCategory
                        .newInstance(2),null)
                .commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}