package com.card.datasaverapp.otpVerify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.card.datasaverapp.MainActivity;
import com.card.datasaverapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginVerifyOtp extends AppCompatActivity {

    private String number,verificationId;
    private EditText o1,o2,o3,o4,o5,o6;
    private TextView verifyOtp, otp_timer, dgo, c_send;
    private FirebaseAuth mAuth;
    private ProgressBar verify_progress;
    private RelativeLayout verify_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verify_otp);

        number = getIntent().getStringExtra("number");

        o1 = findViewById(R.id.o1);
        o2 = findViewById(R.id.o2);
        o3 = findViewById(R.id.o3);
        o4 = findViewById(R.id.o4);
        o5 = findViewById(R.id.o5);
        o6 = findViewById(R.id.o6);
        verifyOtp= findViewById(R.id.verifyOtp);
        otp_timer= findViewById(R.id.otp_timer);
        dgo= findViewById(R.id.dgo);
        c_send= findViewById(R.id.c_send);

        verify_progress = findViewById(R.id.verify_progress);
        verify_layout= findViewById(R.id.verify_layout);

        mAuth = FirebaseAuth.getInstance();

        c_send.setText("Code is sent to"+ number);
        sendVerificationCode(number);

        setTimer();

        o1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!o1.getText().toString().equals("")){

                    o1.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_fill_bg));

                }
                else {
                    o1.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() ==1) {
                    o2.requestFocus();
                }

            }
        });

        o2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!o2.getText().toString().equals("")){

                    o2.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_fill_bg));

                }
                else {
                    o2.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() ==1) {
                    o3.requestFocus();
                }

            }
        });

        o3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {


                if (!o3.getText().toString().equals("")){
                    o3.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_fill_bg));

                }
                else {
                    o3.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() ==1) {
                    o4.requestFocus();
                }
            }
        });

        o4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {


                if (!o4.getText().toString().equals("")){
                    o4.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_fill_bg));

                }
                else {
                    o4.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() ==1) {
                    o5.requestFocus();
                }
            }
        });

        o5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!o5.getText().toString().equals("")){
                    o5.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_fill_bg));

                }
                else {
                    o5.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() ==1) {
                    o6.requestFocus();
                }
            }
        });

        o6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!o6.getText().toString().equals("")){
                    o6.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_fill_bg));
                }
                else {
                    o6.setBackgroundDrawable(ContextCompat.getDrawable(LoginVerifyOtp.this, R.drawable.otp_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() ==1) {
                    o1.requestFocus();
                }
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(o1.getText().toString()) && !TextUtils.isEmpty(o2.getText().toString()) && !TextUtils.isEmpty(o3.getText().toString())
                        && !TextUtils.isEmpty(o4.getText().toString()) && !TextUtils.isEmpty(o5.getText().toString()) &&  !TextUtils.isEmpty(o6.getText().toString())){
                    verifyOtp.setVisibility(View.GONE);
                    verify_progress.setVisibility(View.VISIBLE);
                    String otp1 = o1.getText().toString();
                    String otp2 = o2.getText().toString();
                    String otp3 = o3.getText().toString();
                    String otp4 = o4.getText().toString();
                    String otp5 = o5.getText().toString();
                    String otp6 = o6.getText().toString();

                    String OTP = otp1+otp2+otp3+otp4+otp5+otp6;

                    verifyCode(OTP);
                }
            }
        });

    }

    private void verifyCode(String otp) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        try {
            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent i = new Intent(LoginVerifyOtp.this, MainActivity.class);
                        startActivity(i);
                        verify_progress.setVisibility(View.GONE);
                        verify_layout.setVisibility(View.GONE);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    verify_progress.setVisibility(View.GONE);
                    verify_layout.setVisibility(View.VISIBLE);
                    verifyOtp.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTimer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                otp_timer.setText(""+millisUntilFinished / 1000+" S");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                otp_timer.setText("RESEND OTP");
                dgo.setVisibility(View.VISIBLE);
                otp_timer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendVerificationCode(number);
//                        startTimer();
                    }
                });
            }

        }.start();
    }

    private void sendVerificationCode(String number) {

        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(LoginVerifyOtp.this, "Exist"+ code, Toast.LENGTH_SHORT).show();

            // checking if the code
            // is null or not.
            if (code != null) {

                Log.d("referral_code", code);
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
//                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
//                verifyCode(code);
            }
            else {
                Toast.makeText(LoginVerifyOtp.this, "Empty", Toast.LENGTH_SHORT).show();
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(LoginVerifyOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}