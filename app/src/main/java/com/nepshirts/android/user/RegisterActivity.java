package com.nepshirts.android.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nepshirts.android.R;
import com.nepshirts.android.home.MainActivity;
import com.nepshirts.android.models.UserModel;
import com.nepshirts.android.utils.ProgressDialog;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText fullName,userEmail,userPhoneNumber,userGender,userBirthDate,userPassword, confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        // initializing
        fullName = findViewById(R.id.name);
        userEmail = findViewById(R.id.email);
        userPhoneNumber = findViewById(R.id.phone);
        userGender = findViewById(R.id.gender);
        userBirthDate = findViewById(R.id.birthday);
        userPassword = findViewById(R.id.password2);
        confirmPassword  = findViewById(R.id.password1);
        Button registerBtn = findViewById(R.id.reg_button);



           registerBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    registerNewUser();
               }
           });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent home = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(home);
        }
    }

    public void login_page(View view) {
        Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }
    public void registerNewUser(){
        //getting text from input fields
        final String name = fullName.getText().toString();
        final String email = userEmail.getText().toString();
        final String phone = userPhoneNumber.getText().toString();
        final String gender = userGender.getText().toString();
        final String birthday = userBirthDate.getText().toString();
        final String confirmPass = confirmPassword.getText().toString();
        final String password = userPassword.getText().toString();


        if(name.isEmpty()){
            fullName.setError("required!");
            fullName.requestFocus();
        }else if(email.isEmpty()){
            userEmail.setError("required!");
            userEmail.requestFocus();
        }else if(phone.isEmpty()){
            userPhoneNumber.setError("required!");
            userPhoneNumber.requestFocus();
        }else if(password.isEmpty()) {
            userPassword.setError("required!");
            userPassword.requestFocus();
        } else if (!password.equals(confirmPass)) {
            userPassword.setError("Passwords didn't match");
            userPassword.requestFocus();
        } else if (password.length() < 6) {
            userPassword.setError("Must be at least 6 characters");
            userPassword.requestFocus();
        }
        else{

            ProgressDialog.start(RegisterActivity.this);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        ProgressDialog.stop();
                        Toast.makeText(RegisterActivity.this, "Firebase User Created", Toast.LENGTH_SHORT).show();
                        String city = "Update city";
                        String street = "update street";
                        String landmark ="update landmark";
                        UserModel user = new UserModel(name,email,phone,gender,birthday,city,street,landmark);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast toast=Toast.makeText(RegisterActivity.this,"Success!!",Toast.LENGTH_LONG);
                                    View view =toast.getView();
                                    view.setBackgroundColor(Color.parseColor("#28ff378f")); //green
                                    TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                    toastMessage.setTextColor(Color.WHITE);
                                    toast.show();
                                }else{
                                    Toast toast=Toast.makeText(RegisterActivity.this,"Something Went Wrong!",Toast.LENGTH_LONG);
                                    View view =toast.getView();
                                    view.setBackgroundColor(Color.parseColor("#ff2828cc")); //red
                                    TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                    toastMessage.setTextColor(Color.WHITE);
                                    toast.show();
                                }
                                
                            }
                        })
                        ;

                    }else{
                        Toast.makeText(RegisterActivity.this, "User with this email already exists.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    public void homepage(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
