package com.example.megusta;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginDialog extends Dialog implements View.OnClickListener {
    private Button btn_login;
    private EditText password,email;
    private final Context context;
    private SharedPreferences sp;
    private String strname,strpass;
    private FirebaseAuth mAuth;
    private final String LOGIN_STATUS="login status";
    public LoginDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        mAuth=FirebaseAuth.getInstance();
        sp=context.getSharedPreferences("details1",0);
        createLoginDialog();
    }

    public void createLoginDialog()
    {
        strname=sp.getString("email",null);
        strpass=sp.getString("pass",null );
        setContentView(R.layout.login_dialog);
        setTitle("Login");
        setCancelable(true);
        email = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPassword);
        if(strname!=null&&strpass!=null) {
            email.setText(strname);
            password.setText(strpass);
        }
        btn_login= findViewById(R.id.btnDialogLogin);
        btn_login.setOnClickListener(this);
        show();
    }
    public void authenticate(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOGIN_STATUS, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!strname.equals(LoginDialog.this.email.getText().toString())
                                    || !strpass.equals(LoginDialog.this.password.getText().toString())) {
                                //if authentication was successful and the login credentials were changed
                                //save changes to SP.
                                saveAuth();
                            }
                            LoginDialog.this.logIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOGIN_STATUS, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void logIn(FirebaseUser user) {
        Toast.makeText(context,"Logging in...", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(context,MainMenu.class);
        context.startActivity(intent);
    }
    private void saveAuth(){
        /*
         saves login data to SP.
         */
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("pass", password.getText().toString());
        editor.apply(); //Android Studio suggested that apply  is better than commit.
        Toast.makeText(context,"username & password saved",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
         if(view==btn_login)
        {
            dismiss();
            authenticate(email.getText().toString(), password.getText().toString());
        }
    }
}
