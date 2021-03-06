package com.example.megusta;
/** Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
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
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Sign up menu for the app.
 */
public class SignUpDialog extends Dialog implements View.OnClickListener {
    private Button btn_signup;
    private final Context context;
    private EditText username, password, password_confirm, email;
    private final String SIGNUP_STATUS = "signup status";
    private final FirebaseAuth mAuth;
    private final String TITLE = "Signup";

    /**
     * Initiates the dialog.
     * @param context the activity that called the dialog.
     */
    public SignUpDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        createSignupDialog();
    }

    /**
     * The UI creation and initialization.
     */
    private void createSignupDialog() {
        setContentView(R.layout.signup_dialog);
        setTitle(TITLE);
        setCancelable(true); //can close when pressing outside.
        email = findViewById(R.id.etEmail);
        username = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPassword);
        password_confirm = findViewById(R.id.etPasswordConfirm);
        btn_signup = findViewById(R.id.btnDialogSignup);
        btn_signup.setOnClickListener(this);
        show();
    }

    /**
     * Validates that the sign up is up to some standards.
     * @return true if validated
     * @throws ValidationException an error with the reason why the validation failed.
     */
    private boolean validateSignup() throws ValidationException {
        if (password.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || username.getText().toString().isEmpty() || password_confirm.getText().toString().isEmpty())
            throw new ValidationException("All fields are required");
        else if (!isValidEmail(email.getText().toString()))
            throw new ValidationException("Email address not valid");
        else if (username.getText().toString().length() < 5)
            throw new ValidationException("Username is too short");
        else if (password.getText().toString().length() < 8)
            throw new ValidationException("Password too short");
        else if (!password.getText().toString().equals(password_confirm.getText().toString()))
            throw new ValidationException("Passwords don't match");
        else
            return true;
    }

    /**
     * Checks if an Email address makes sense.
     * @param target the Email to check.
     * @return Boolean if an email address is in the right format or not.
     */
    private static boolean isValidEmail(CharSequence target) {
        //validates email address.
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Creates a new user in Firebase.
     * @param email the email to assign the account to
     * @param password the password of the user.
     * @param display_name the display name in app.
     */
    private void createNewUser(String email, String password, String display_name) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(SIGNUP_STATUS, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(display_name).build());
                    Toast.makeText(context, "User Successfully created!", Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SIGNUP_STATUS, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * On click listener for the buttons on dialog.
     * @param view the view to listen to.
     */
    @Override
    public void onClick(View view) {
        if (view == btn_signup) {
            try {
                if (validateSignup()) {
                    createNewUser(email.getText().toString(), password.getText().toString(),
                            username.getText().toString());
                    dismiss();
                }
            } catch (ValidationException exc) {
                Toast.makeText(context, exc.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
