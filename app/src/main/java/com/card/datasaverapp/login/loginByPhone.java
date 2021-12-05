package com.card.datasaverapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.card.datasaverapp.MainActivity;
import com.card.datasaverapp.R;
import com.card.datasaverapp.otpVerify.OtpVerifyActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginByPhone extends AppCompatActivity {

    private EditText editText, user_name, referral_code;
    private ImageView btnClear,correct_code,wrong_code;
    private TextView sendOptBtn,verify_code,number_error,name_error;
    private FirebaseAuth mAuth;
    private String verificationId, code, id;
    private DatabaseReference ref;
    private ProgressBar progress_b,otp_progress;
    private Spinner spinner;
    private String[] gender;
    private String gender_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone);

        editText = findViewById(R.id.editText);
        btnClear = findViewById(R.id.btn_clear);
        sendOptBtn = findViewById(R.id.send_otp_btn);
        referral_code = findViewById(R.id.referral_code);
        user_name = findViewById(R.id.user_name);
        wrong_code = findViewById(R.id.wrong_code);
        correct_code = findViewById(R.id.correct_code);
        verify_code= findViewById(R.id.verify_code);
        progress_b = findViewById(R.id.progress_b);
        otp_progress= findViewById(R.id.otp_progress);
        spinner = findViewById(R.id.spinner);
        name_error= findViewById(R.id.name_error);
        number_error = findViewById(R.id.number_error);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        ref = FirebaseDatabase.getInstance().getReference("Users");

        //set on text change listener for edittext
        editText.addTextChangedListener(textWatcher());
        //set event for clear button
        btnClear.setOnClickListener(onClickListener());

        referral_code.addTextChangedListener(text_watcher());

        gender = new String[]{"Male", "Female", "Other"};

        //Creating the ArrayAdapter instance having the gender list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender_value = gender[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editText.setText(""); //clear edittext
                Toast.makeText(loginByPhone.this, "Button Delete clicked!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private TextWatcher text_watcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (referral_code.getText().toString().length()==6){
                    wrong_code.setVisibility(View.GONE);
                    verify_code.setVisibility(View.VISIBLE);
                    correct_code.setVisibility(View.GONE);
                    progress_b.setVisibility(View.GONE);

                    verify_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verifyCode(referral_code.getText().toString().trim());
                            progress_b.setVisibility(View.VISIBLE);
                            verify_code.setVisibility(View.GONE);
                        }
                    });

                }
                else {
                    wrong_code.setVisibility(View.VISIBLE);
                    correct_code.setVisibility(View.GONE);
                    progress_b.setVisibility(View.GONE);
                    verify_code.setVisibility(View.GONE);
                }

            }
        };

    }

    private void verifyCode(String s) {

        try {
            ref.orderByChild("my_referral").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            id = ""+ds.child("userId").getValue().toString();

                        }

                        wrong_code.setVisibility(View.GONE);
                        progress_b.setVisibility(View.GONE);
                        correct_code.setVisibility(View.VISIBLE);
                        verify_code.setVisibility(View.GONE);

                    }
                    else {
                        progress_b.setVisibility(View.GONE);
                        correct_code.setVisibility(View.GONE);
                        verify_code.setVisibility(View.GONE);
                        wrong_code.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TextWatcher textWatcher() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editText.getText().toString().equals("")) { //if edittext include text
                    btnClear.setVisibility(View.VISIBLE);
//                    textView.setText(editText.getText().toString());
                } else { //not include text
                    btnClear.setVisibility(View.GONE);
//                    textView.setText("Edittext cleared!");
                    Toast.makeText(loginByPhone.this, "All texts have gone!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void sendOTP(View view) {

        if (!TextUtils.isEmpty(editText.getText().toString()) && !TextUtils.isEmpty(user_name.getText().toString()) && !gender_value.isEmpty()){
            sendOptBtn.setVisibility(View.GONE);
            otp_progress.setVisibility(View.VISIBLE);
            String phone = "+91" + editText.getText().toString().trim();
            String u_name = user_name.getText().toString();
            Intent intent = new Intent(loginByPhone.this, OtpVerifyActivity.class);

            if (!TextUtils.isEmpty(referral_code.getText().toString())){
                code = referral_code.getText().toString();

                if (checkNumberIsExistOrNot(phone)){

                    if (verifyReferralCode(code)){

                        intent.putExtra("phone", phone);
                        intent.putExtra("name", u_name);
                        intent.putExtra("gender", gender_value);
                        intent.putExtra("code", code);
                        intent.putExtra("id", id);

                        startActivity(intent);
                        finish();

                    }
                    else {

                        progress_b.setVisibility(View.GONE);
                        correct_code.setVisibility(View.GONE);
                        verify_code.setVisibility(View.GONE);
                        wrong_code.setVisibility(View.VISIBLE);
                    }
                }
                else {

                    otp_progress.setVisibility(View.GONE);
                    sendOptBtn.setVisibility(View.VISIBLE);
                    number_error.setText(R.string.already_exist_number);
                    number_error.setVisibility(View.VISIBLE);
                }

            }

            else {

                if (checkNumberIsExistOrNot(phone)){


                    intent.putExtra("phone", phone);
                    intent.putExtra("name", u_name);
                    intent.putExtra("gender", gender_value);
                    intent.putExtra("code", "0");
                    startActivity(intent);
                    finish();
                }

                else {

                    otp_progress.setVisibility(View.GONE);
                    sendOptBtn.setVisibility(View.VISIBLE);
                    number_error.setText(R.string.already_exist_number);
                    number_error.setVisibility(View.VISIBLE);
                }

            }

        }

        else if (TextUtils.isEmpty(editText.getText().toString()) || TextUtils.isEmpty(user_name.getText().toString())){
            if (TextUtils.isEmpty(editText.getText().toString())){
                number_error.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(user_name.getText().toString())){
                name_error.setVisibility(View.VISIBLE);
            }
        }
        else {
            Toast.makeText(this, "Please Fill All Credential Carefully", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkNumberIsExistOrNot(String phone) {

        final boolean[] isExist = {false};
        Log.d("problems", phone);

        try {
            ref.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        isExist[0] = true;
                    }
                    else {

                        isExist[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isExist[0];
    }


    private boolean verifyReferralCode(String s) {

        final boolean[] isExist = {false};

        try {

            ref.orderByChild("my_referral").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){

//                    wrong_code.setVisibility(View.GONE);
//                    progress_b.setVisibility(View.GONE);
//                    correct_code.setVisibility(View.VISIBLE);
//                    verify_code.setVisibility(View.GONE);

                        isExist[0] = true;
                    }
                    else {
//                    progress_b.setVisibility(View.GONE);
//                    correct_code.setVisibility(View.GONE);
//                    verify_code.setVisibility(View.GONE);
//                    wrong_code.setVisibility(View.VISIBLE);

                        isExist[0] = false;

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isExist[0];

    }

    @Override
    protected void onStart() {

        if (mAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        super.onStart();
    }

    public void AlreadyAccount(View view) {
        startActivity(new Intent(getApplicationContext(), AlreadyCreate.class));
        finish();
    }
}