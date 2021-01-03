package com.example.megusta;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //firebase related:
    private final String LOGIN_STATUS="login status";
    private final String SIGNUP_STATUS="signup status";
    private FirebaseAuth mAuth;
    //sp variables
    private SharedPreferences sp;
    private String strname;
    private String strpass;
    //dialogs
    private Dialog login_dialog;
    private Dialog signup_dialog;
    //fields
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText password_confirm;
    //buttons
    private Button btn_login_dia;
    private Button btn_signup_dia;
    private Button btn_signup;
    private Button btn_login;


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
    public void createLoginDialog()
    {
//connect to sp
        strname=sp.getString("email",null);
        strpass=sp.getString("pass",null );
        login_dialog= new Dialog(this);
        login_dialog.setContentView(R.layout.login_dialog);
        login_dialog.setTitle("Login");
        login_dialog.setCancelable(true);
        email = login_dialog.findViewById(R.id.etUserName);
        password = login_dialog.findViewById(R.id.etPassword);
        if(strname!=null&&strpass!=null) {
            email.setText(strname);
            password.setText(strpass);
        }
        btn_login= login_dialog.findViewById(R.id.btnDialogLogin);
        btn_login.setOnClickListener(this);
        login_dialog.show();
    }

    public void createSignupDialog() {
        signup_dialog= new Dialog(this);
        signup_dialog.setContentView(R.layout.signup_dialog);
        signup_dialog.setTitle("Signup");
        signup_dialog.setCancelable(true); //can close when pressing outside.
        email =signup_dialog.findViewById(R.id.etEmail);
        username =signup_dialog.findViewById(R.id.etUserName);
        password = signup_dialog.findViewById(R.id.etPassword);
        password_confirm = signup_dialog.findViewById(R.id.etPasswordConfirm);
        btn_signup= signup_dialog.findViewById(R.id.btnDialogSignup);
        btn_signup.setOnClickListener(this);
        signup_dialog.show();
    }
    public static boolean isValidEmail(CharSequence target) {
        //validates email address.
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View view) {
        if(view==btn_login_dia)
        {
            createLoginDialog();
        }
        else if(view==btn_signup_dia)
        {
            createSignupDialog();
        }
        else if(view==btn_login)
        {
            login_dialog.dismiss();
            authenticate(email.getText().toString(), password.getText().toString());
        }
        else if(view==btn_signup) {
            try {
                if(validateSignup()) {
                    createNewUser(email.getText().toString(), password.getText().toString(),
                            username.getText().toString());
                    signup_dialog.dismiss();
                }
            }
            catch(ValidationException exc) {
                Toast.makeText(this,exc.getMessage(),Toast.LENGTH_LONG).show();
            }

        }

    }

    public void authenticate(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOGIN_STATUS, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!strname.equals(MainActivity.this.email.getText().toString())
                                    || !strpass.equals(MainActivity.this.password.getText().toString())) {
                                //if authentication was successful and the login credentials were changed
                                //save changes to SP.
                                saveAuth();
                            }
                            MainActivity.this.logIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOGIN_STATUS, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateSignup() throws ValidationException {
        if (password.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || username.getText().toString().isEmpty() || password_confirm.getText().toString().isEmpty())
            throw new ValidationException("All fields are required");
        else if(!isValidEmail(email.getText().toString()))
            throw new ValidationException("Email address not valid");
        else if (username.getText().toString().length()<5)
            throw new ValidationException("Username is too short");
        else if (password.getText().toString().length()<8)
            throw new ValidationException("Password too short");
        else if (!password.getText().toString().equals(password_confirm.getText().toString()))
            throw new ValidationException("Passwords don't match" );
        else
            return true;
    }
    private void createNewUser(String email,String password,String display_name) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(SIGNUP_STATUS, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    user.updateProfile( new UserProfileChangeRequest.Builder().setDisplayName(display_name).build());
                    Toast.makeText(MainActivity.this,"User Successfully created!",Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SIGNUP_STATUS, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveAuth(){
        /*
         saves login data to SP.
         */
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("email", email.getText().toString());
            editor.putString("pass", password.getText().toString());
            editor.apply(); //Android Studio suggested that apply  is better than commit.
            Toast.makeText(this,"username & password saved",Toast.LENGTH_SHORT).show();
    }
    public void logIn(FirebaseUser user) {
        Toast.makeText(this,"Logging in...", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(this,MainMenu.class);
        startActivity(intent);
    }
}
