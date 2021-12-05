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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OtpVerifyActivity extends AppCompatActivity {

    private EditText o1,o2,o3,o4,o5,o6;
    private TextView verifyOtp, otp_timer, dgo, c_send;
    private FirebaseAuth mAuth;
    private String phone, verificationId, code, name, my_id, gender,user_joined_name,c;
    private DatabaseReference ref, reference, joinedRef, coinsRef;
    private ProgressBar verify_progress;
    private RelativeLayout verify_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

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

        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        gender = getIntent().getStringExtra("gender");
        c = getIntent().getStringExtra("code");

        c_send.setText("Code is sent to"+ phone);
        sendVerificationCode(phone);

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
                        sendVerificationCode(phone);
                        startTimer();
                    }
                });
            }

        }.start();


        o1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!o1.getText().toString().equals("")){

                    o1.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_fill_bg));

                }
                else {
                    o1.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_bg));
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

                    o2.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_fill_bg));

                }
                else {
                    o2.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_bg));
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
                    o3.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_fill_bg));

                }
                else {
                    o3.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_bg));
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
                    o4.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_fill_bg));

                }
                else {
                    o4.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_bg));
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
                    o5.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_fill_bg));

                }
                else {
                    o5.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_bg));
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
                    o6.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_fill_bg));
                }
                else {
                    o6.setBackgroundDrawable(ContextCompat.getDrawable(OtpVerifyActivity.this, R.drawable.otp_bg));
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

    private void startTimer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                otp_timer.setText(""+millisUntilFinished / 1000+" S");
                dgo.setVisibility(View.GONE);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                otp_timer.setText("RESEND OTP");
                dgo.setVisibility(View.VISIBLE);
                otp_timer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendVerificationCode(phone);
                        dgo.setVisibility(View.GONE);
                        startTimer();
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
            Toast.makeText(OtpVerifyActivity.this, "Exist"+ code, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(OtpVerifyActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(OtpVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.

        try {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                my_id = mAuth.getCurrentUser().getUid();
                                reference = FirebaseDatabase.getInstance().getReference("Users");
                                joinedRef = FirebaseDatabase.getInstance().getReference("JoinedByReferral");
                                ref = FirebaseDatabase.getInstance().getReference("Users");
                                coinsRef = FirebaseDatabase.getInstance().getReference("Coins");

                                HashMap<Object, String> map = new HashMap<>();
                                String referral_code = createRandomCode(6);

                                if (!c.equals("0")){
                                    code = getIntent().getStringExtra("code");
//                                String joined_u_id = getNameByReferral(code);
                                    String joined_u_id = getIntent().getStringExtra("id");
                                    map.put("phone", phone);
                                    map.put("code", code);
                                    map.put("name", name);
                                    map.put("my_referral", referral_code);
                                    map.put("gender", gender);
                                    map.put("userId", my_id);
                                    ref.child(my_id).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            HashMap<Object, String> map1 = new HashMap<>();
                                            map1.put("joinedById", my_id);
                                            map1.put("joinedFromId", joined_u_id);

                                            joinedRef.child(joined_u_id).child(my_id).setValue(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    HashMap<Object, String> coinMap = new HashMap<>();
                                                    coinMap.put("coins", "20");
                                                    coinMap.put("T", "No");

                                                    coinsRef.child(my_id).setValue(coinMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            coinsRef.child(joined_u_id).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot s) {
                                                                    try {
                                                                        if (s.exists()){
                                                                            String co = s.child("coins").getValue().toString();
                                                                            String t = s.child("T").getValue().toString();

                                                                            int c = Integer.parseInt(co);
                                                                            int u_coins = c+10;

                                                                            HashMap<Object, String> coinMap1 = new HashMap<>();
                                                                            coinMap1.put("coins", String.valueOf(u_coins));
                                                                            coinMap1.put("T", t);

                                                                            coinsRef.child(joined_u_id).setValue(coinMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    // if the code is correct and the task is successful
                                                                                    // we are sending our user to new activity.
                                                                                    Intent i = new Intent(OtpVerifyActivity.this, MainActivity.class);
                                                                                    startActivity(i);
                                                                                    verify_progress.setVisibility(View.GONE);
                                                                                    verify_layout.setVisibility(View.GONE);
                                                                                    finish();
                                                                                }
                                                                            });
                                                                        }
                                                                        else {
                                                                            HashMap<Object, String> coinMap1 = new HashMap<>();
                                                                            coinMap1.put("coins", "10");
                                                                            coinMap1.put("T", "No");
                                                                            coinsRef.child(joined_u_id).setValue(coinMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    // if the code is correct and the task is successful
                                                                                    // we are sending our user to new activity.
                                                                                    Intent i = new Intent(OtpVerifyActivity.this, MainActivity.class);
                                                                                    startActivity(i);
                                                                                    verify_progress.setVisibility(View.GONE);
                                                                                    verify_layout.setVisibility(View.GONE);
                                                                                    finish();
                                                                                }
                                                                            });
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    });
//                                Toast.makeText(OtpVerifyActivity.this, "Have a referral", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    map.put("phone", phone);
                                    map.put("code", "0");
                                    map.put("name", name);
                                    map.put("my_referral", referral_code);
                                    map.put("gender", gender);
                                    map.put("userId", my_id);
                                    ref.child(my_id).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // if the code is correct and the task is successful
                                            // we are sending our user to new activity.
                                            Intent i = new Intent(OtpVerifyActivity.this, MainActivity.class);
                                            startActivity(i);
                                            verify_progress.setVisibility(View.GONE);
                                            verify_layout.setVisibility(View.GONE);
                                            finish();
                                        }
                                    });
//                                Toast.makeText(OtpVerifyActivity.this, "No referral", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                // if the code is not correct then we are
                                // displaying an error message to the user.
                                Toast.makeText(OtpVerifyActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getNameByReferral(String s) {

        try {
            reference.orderByChild("my_referral").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        user_joined_name = snapshot.child("userId").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user_joined_name;

    }

    public String createRandomCode(int codeLength){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        System.out.println(output);
        return output ;
    }
}