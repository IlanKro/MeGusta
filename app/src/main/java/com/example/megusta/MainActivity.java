package com.example.megusta;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends Activity implements View.OnClickListener {
    SharedPreferences sp;
    Dialog login_dialog;
    Dialog signup_dialog;
    EditText et_username;
    EditText et_password;
    EditText et_email;
    EditText et_password_confirm;
    Button btn_login_dia;
    Button btn_signup_dia;
    Button btn_signup;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login_dia=(Button)findViewById(R.id.btnLogin);
        btn_login_dia.setOnClickListener(this);
        btn_signup_dia=(Button)findViewById(R.id.btnSignup);
        btn_signup_dia.setOnClickListener(this);
        sp=getSharedPreferences("details1",0);
    }
    public void createLoginDialog()
    {
//connect to sp
        String strname=sp.getString("username",null);
        String strpass=sp.getString("pass",null );
        login_dialog= new Dialog(this);
        login_dialog.setContentView(R.layout.login_dialog);
        login_dialog.setTitle("Login");
        login_dialog.setCancelable(true);
        et_username=(EditText)login_dialog.findViewById(R.id.etUserName);
        et_password=(EditText)login_dialog.findViewById(R.id.etPassword);
        if(strname!=null&&strpass!=null) {
            et_username.setText(strname);
            et_password.setText(strpass);
        }
        btn_login=(Button)login_dialog.findViewById(R.id.btnDialogLogin);
        btn_login.setOnClickListener(this);
        login_dialog.show();
    }

    public void createSignupDialog() {
        signup_dialog= new Dialog(this);
        signup_dialog.setContentView(R.layout.signup_dialog);
        signup_dialog.setTitle("Signup");
        signup_dialog.setCancelable(true);
        et_email=(EditText)signup_dialog.findViewById(R.id.etEmail);
        et_username=(EditText)signup_dialog.findViewById(R.id.etUserName);
        et_password=(EditText)signup_dialog.findViewById(R.id.etPassword);
        et_password_confirm=(EditText)signup_dialog.findViewById(R.id.etPasswordConfirm);;
        btn_signup=(Button)signup_dialog.findViewById(R.id.btnDialogSignup);
        btn_signup.setOnClickListener(this);
        signup_dialog.show();
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
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("username",et_username.getText().toString());
            editor.putString("pass",et_password.getText().toString()); editor.commit();
            Toast.makeText(this,"username & password saved",Toast.LENGTH_LONG).show();
            login_dialog.dismiss();
        }
        else if(view==btn_signup) {
            System.out.println("placeholder");
        }
    }
}