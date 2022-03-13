package main.registry.signinactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button Login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        register = (TextView) findViewById (R.id.register);
        register.setOnClickListener (this);

        Login = (Button) findViewById (R.id.Login);
        Login.setOnClickListener (this);

        editTextEmail = (EditText) findViewById (R.id.Email);
        editTextPassword = (EditText) findViewById (R.id.Password);

        progressBar = (ProgressBar) findViewById (R.id.progressBar);

        mAuth = FirebaseAuth.getInstance ();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId ()) {
            case R.id.register:
                startActivity (new Intent (this, RegisterUser.class));
                break;

            case R.id.Login:
                userLogin ();
                break;
        }

    }

    private void userLogin() {
        String email = editTextEmail.getText ().toString ().trim ();
        String password = editTextPassword.getText ().toString ().trim ();

        if (email.isEmpty ()) {
            editTextEmail.setError ("Email must Required");
            editTextEmail.requestFocus ();
            return;
        }
        if (Patterns.EMAIL_ADDRESS.matcher (email).matches ()) {
            editTextEmail.setError ("Valid Email is Required");
            editTextEmail.requestFocus ();
            return;
        }
        if (password.isEmpty ()) {
            editTextPassword.setError ("Password must Required");
            editTextPassword.requestFocus ();
            return;
        }
        if (password.length () < 6) {
            editTextPassword.setError ("Password min is 6 characters!");
            editTextPassword.requestFocus ();
            return;
        }

        progressBar.setVisibility (View.VISIBLE);

        mAuth.signInWithEmailAndPassword (email, password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful ()) {
                    startActivity (new Intent (MainActivity.this, ProfileActivity.class));

                } else {
                    Toast.makeText (MainActivity.this, "Failed to Login! Please check your credentials", Toast.LENGTH_LONG).show ();

                }
            }
        });
    }

}




