package com.example.shreshth.cloudparking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mNameField;
    private EditText mPasswordField;
    private Button mRegBtn;
    private ProgressBar progressBar;
    private Button mLoginPageBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();

        progressBar=(ProgressBar)findViewById(R.id.reg_progress_bar);
        mEmailField=(EditText)findViewById(R.id.register_email);
        mNameField=(EditText)findViewById(R.id.register_name);
        mPasswordField=(EditText)findViewById(R.id.register_password);
        mFirestore=FirebaseFirestore.getInstance();

        mRegBtn=(Button)findViewById(R.id.register_btn);
        mLoginPageBtn=(Button)findViewById(R.id.register_login_btn);



        mRegBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String name=mNameField.getText().toString();
                String email=mEmailField.getText().toString();
                String password=mPasswordField.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                final String user_id=mAuth.getCurrentUser().getUid();
                                progressBar.setVisibility(View.INVISIBLE);
                                sendToMain();

                                Map<String,Object> userMap = new HashMap<>();
                                userMap.put("name",name);
                                mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        sendToMain();


                                    }
                                });

                            }
                            else {

                                Toast.makeText(RegisterActivity.this,"Error : "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }


                        }
                    });

                }


            }
        });







        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void sendToMain() {

        Intent mainIntent=new Intent(RegisterActivity.this,DashboardActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
