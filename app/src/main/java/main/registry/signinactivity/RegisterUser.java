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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    public TextView banner;


    private EditText editTextFirstname, editTextLastname,editTextCompleteAddress,editTextEmail,editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private Object User;

    public RegisterUser() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        TextView registerUser = (Button) findViewById(R.id.RegisterUser);
        registerUser.setOnClickListener(this);

        editTextFirstname = (EditText) findViewById(R.id.Firstname);
        editTextLastname = (EditText) findViewById(R.id.Lastname);
        editTextCompleteAddress= (EditText) findViewById(R.id.CompleteAddress);
        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPassword = (EditText) findViewById(R.id.Password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.RegisterUser:
                registerUser();
                break;

        }

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String Firstname = editTextFirstname.getText().toString().trim();
        String Lastname = editTextLastname.getText().toString().trim();
        String CompleteAddress = editTextCompleteAddress.getText().toString().trim();

        if (Firstname.isEmpty()) {
            editTextFirstname.setError("Firstname is required!");
            editTextFirstname.requestFocus();
            return;
        }

        if (Lastname.isEmpty()) {
            editTextLastname.setError("Lastname is required!");
            editTextLastname.requestFocus();
            return;

        }

        if (CompleteAddress.isEmpty()) {
            editTextCompleteAddress.setError("Complete Address is Required!");
            editTextCompleteAddress.requestFocus();
            return;

        }

        if (email.isEmpty()){
            editTextEmail.setError("Email is Required!");
            editTextEmail.requestFocus();
            return;
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please valid email is provided!");
            editTextEmail.requestFocus();
            return;

        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is Required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            editTextPassword.setError("Password min is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(Firstname, Lastname, CompleteAddress,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been Registered Successfully",Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(RegisterUser.this,"Try Again Failed to Register the User",Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);

                                }
                            });

                        }else{
                            Toast.makeText(RegisterUser.this,"Try Again Failed to Register the User",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    public void setUser(Object user) {
        User = user;
    }
}