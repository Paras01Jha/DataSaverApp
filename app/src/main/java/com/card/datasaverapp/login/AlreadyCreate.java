package com.card.datasaverapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.card.datasaverapp.R;
import com.card.datasaverapp.otpVerify.LoginVerifyOtp;
import com.card.datasaverapp.otpVerify.OtpVerifyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlreadyCreate extends AppCompatActivity {

    private EditText editText;
    private TextView sendOptBtn, number_error;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private ProgressBar otp_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_create);

        editText = findViewById(R.id.editText);
        sendOptBtn = findViewById(R.id.send_otp_btn);
        otp_progress= findViewById(R.id.otp_progress);
        number_error = findViewById(R.id.number_error);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");


    }

    public void sendOTP(View view) {

        if (!TextUtils.isEmpty(editText.getText().toString())){
            String phone = "+91" + editText.getText().toString();
            ref.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue()!=null){
                        Intent intent = new Intent(AlreadyCreate.this, LoginVerifyOtp.class);
                        intent.putExtra("number", phone);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        number_error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            number_error.setVisibility(View.VISIBLE);
        }
    }
}