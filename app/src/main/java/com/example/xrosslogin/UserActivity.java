package com.example.xrosslogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    private Button tag_btn[] = new Button[9];
    private Button create_account;
    private EditText username_field,password_field;
    String tag_value,email, username,password;
    int[] btn_id = {R.id.tag1,R.id.tag2,R.id.tag3,R.id.tag4,R.id.tag5,R.id.tag6,R.id.tag7,R.id.tag8,R.id.tag9};
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference mdbreference;
    String userid;
    TextView selected_tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();

        username_field = findViewById(R.id.txtUsername);
        create_account = findViewById(R.id.btnCreateAccount);
        password_field = findViewById(R.id.txtPassword);
        selected_tag = findViewById(R.id.selectedtag);

        mdbreference = FirebaseDatabase.getInstance().getReference();

        email = intent.getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();

        for (int i = 0; i< btn_id.length; i++)
        {
            tag_btn[i] = (Button) findViewById(btn_id[i]);
        }

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_field.getText().toString();
                password = password_field.getText().toString();

                createUser(email,password);
            }
        });
    }

    public void change_tag_value()
    {
        selected_tag.setText(tag_value);
    }

    public void ButtononClick(View v) {
        switch (v.getId()) {
            case R.id.tag1:
                tag_value = tag_btn[0].getText().toString();
                change_tag_value();
                break;

            case R.id.tag2:
                tag_value = tag_btn[1].getText().toString();
                change_tag_value();
                break;

            case R.id.tag3:
                tag_value = tag_btn[2].getText().toString();
                change_tag_value();
                break;

            case R.id.tag4:
                tag_value = tag_btn[3].getText().toString();
                change_tag_value();
                break;

            case R.id.tag5:
                tag_value = tag_btn[4].getText().toString();
                change_tag_value();
                break;

            case R.id.tag6:
                tag_value = tag_btn[5].getText().toString();
                change_tag_value();
                break;

            case R.id.tag7:
                tag_value = tag_btn[6].getText().toString();
                change_tag_value();
                break;

            case R.id.tag8:
                tag_value = tag_btn[7].getText().toString();
                change_tag_value();
                break;

            case R.id.tag9:
                tag_value = tag_btn[8].getText().toString();
                change_tag_value();
                break;

        }
    }


    public void createUser(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            userid = user.getUid();
                            Toast.makeText(UserActivity.this,"User account created successfully",Toast.LENGTH_LONG).show();
                            addUser();
                            startActivity(new Intent(UserActivity.this,Explore.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(UserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addUser()
    {
        AddUser newuser = new AddUser(email, username, tag_value);
        mdbreference.child("users").child(userid).setValue(newuser);
    }
}