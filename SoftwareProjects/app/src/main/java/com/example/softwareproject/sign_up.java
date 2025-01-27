package com.example.softwareproject;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class sign_up extends AppCompatActivity
{
    EditText edtEmail,edtPhoneNo,edtUsername,edtPassword,edtConfirmPassword,edtFirstName,edtLastName;
    Button btnSignUp;
    TextView tv,pa;

    FirebaseDatabase fb;
    DatabaseReference Gdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail= (EditText) findViewById(R.id.email_address);
        edtPhoneNo = (EditText) findViewById(R.id.Phone_number);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.pass1);
        edtConfirmPassword = (EditText) findViewById(R.id.pass2);
        btnSignUp = (Button) findViewById(R.id.sibt);
        edtFirstName=(EditText) findViewById(R.id.first_name);
        edtLastName=(EditText) findViewById(R.id.last_name);
        tv = (TextView) findViewById(R.id.tv);
        pa = (TextView) findViewById(R.id.pad);


        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = edtEmail.getText().toString();
                String number  = edtPhoneNo.getText().toString();
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String ConfirmPassword = edtConfirmPassword.getText().toString();
                String name = edtFirstName.getText().toString()+" "+edtLastName.getText().toString();

                fb= FirebaseDatabase.getInstance();
                Gdb = fb.getReference("Users");

                boolean completed = completed();
                boolean matchingPassword = passwords_match(password, ConfirmPassword);
                boolean validPassword = valid_password(password);
                boolean validEmail = check_email(email);
                boolean validNUmber = Valid_number(number);

                if (completed && matchingPassword && validPassword && validEmail && validNUmber)
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    Query checkUsername = ref.orderByChild("username").equalTo(username);
                    checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                edtUsername.setError("Username already taken");
                            }
                            else{
                                CreateUserClass createUserClass = new CreateUserClass(username,email,number,password,name);
                                Gdb.child(username).setValue(createUserClass);

                                Intent intent= new Intent(sign_up.this, Log_in.class);
                                intent.putExtra("Username",username);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }

    public boolean Valid_number(String num){
        if(num.length()!=10){
            edtPhoneNo.setError("invalid phone number");
            return false;
        }
        else{
            return true;
        }
    }


    public boolean check_email(String email){
        if(email.contains("@")){
            return true;
        }
        else {
            edtEmail.setError("Invalid email address");
            return false;
        }
    }
    public boolean completed(){
        boolean key = true;
        if(TextUtils.isEmpty(edtUsername.getText().toString())){
            edtUsername.setError("Please enter in a username");
            key =  false;
        }
        if(TextUtils.isEmpty(edtEmail.getText().toString())){
            edtEmail.setError("Please enter in an email address");
            key =  false;
        }
        if(TextUtils.isEmpty(edtPhoneNo.getText().toString())){
            edtPhoneNo.setError("Please enter in a phone number");
            key =  false;
        }
        if(TextUtils.isEmpty(edtFirstName.getText().toString())){
            edtFirstName.setError("Please enter in your first name");
            key =  false;
        }
        if(TextUtils.isEmpty(edtLastName.getText().toString())){
            edtLastName.setError("Please enter in your surname ");
            key =  false;
        }
        if(TextUtils.isEmpty(edtPassword.getText().toString())){
            edtPassword.setError("Please enter in a password ");
            key =  false;
        }
        if(TextUtils.isEmpty(edtConfirmPassword.getText().toString())){
            edtConfirmPassword.setError("Please enter in a password ");
            key =  false;
        }
        if(key == false){
            tv.setText("Please ensure you have\ncompleted all the fields");
        }
        return key;
    }
    public  boolean passwords_match(String pw1,String pw2){
        if(!pw1.equals(pw2)){
            edtConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
    public boolean valid_password(String password){
        if(password.length()<=7){
            edtPassword.setError("Password needs to be of length 8 or more");
            return false;
        }else{
            return true;
        }
    }
}
