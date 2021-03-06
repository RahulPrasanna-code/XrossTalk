package com.example.xrosslogin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Register extends AppCompatActivity {

    EditText email_field;
    Button btnSendOTP;

    private String from_email;
    private String from_email_password;
    private String code;
    private String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_field = findViewById(R.id.txtEmail);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        from_email = "noreplyxrosstalk@gmail.com";
        from_email_password = "xross@talk";
        auth = FirebaseAuth.getInstance();

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            email = email_field.getText().toString();


            auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                            if (isNewUser) {
                                Log.e("TAG", "Is New User!");
                                Random random = new Random();

                                int temp_code = random.nextInt(8999) + 1000;

                                code = Integer.toString(temp_code);

                                String message_to_send = "Your otp is " + code;
                                Properties props = new Properties();
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.starttls.enable", "true");
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.port", "587");

                                Session session = Session.getInstance(props,
                                        new Authenticator() {
                                            @Override
                                            protected PasswordAuthentication getPasswordAuthentication() {
                                                return new PasswordAuthentication(from_email, from_email_password);
                                            }
                                        });
                                try {
                                    Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(from_email));
                                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(email_field.getText().toString()));
                                    message.setSubject("OTP VERIFICATION");
                                    message.setText(message_to_send);
                                    Transport.send(message);
                                    Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Register.this, OTPverification.class);
                                    i.putExtra("email", email);
                                    i.putExtra("code", code);
                                    startActivity(i);
                                } catch (MessagingException e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.e("TAG", "Is Old User!");
                                Toast.makeText(getApplicationContext(),"User Already available please sign in",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

            }
        });
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


    }



}


