package com.example.megusta;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //firebase related:
    private FirebaseAuth mAuth;
    //sp variables
    private SharedPreferences sp;
    //buttons
    private Button btn_login_dia;
    private Button btn_signup_dia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login_dia= findViewById(R.id.btnLogin);
        btn_login_dia.setOnClickListener(this);
        btn_signup_dia= findViewById(R.id.btnSignup);
        btn_signup_dia.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        sp=getSharedPreferences("details1",0);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
            this.logIn(currentUser);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_login_dia)
        {
            new LoginDialog(this);
        }
        else if(view==btn_signup_dia)
        {
            new SignUpDialog(this);
        }
    }

    public void logIn(FirebaseUser user) {
        Toast.makeText(this,"Logging in...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainMenu.class));
    }
}
