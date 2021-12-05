package com.card.datasaverapp.userDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.card.datasaverapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_detailsActivity extends AppCompatActivity {

    private EditText user_name;
    private TextView submit, name_error;
    private ProgressBar submit_progress;
    private Spinner spinner;
    private String[] gender;
    private String gender_value,my_id;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        user_name = findViewById(R.id.user_name);
        submit = findViewById(R.id.submit);
        name_error = findViewById(R.id.name_error);
        submit_progress = findViewById(R.id.submit_progress);
        spinner = findViewById(R.id.spinner);

        mAuth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference("Users");

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

    public void Submit(View view) {
        if (!TextUtils.isEmpty(user_name.getText().toString()) && !gender_value.isEmpty()){
            submit.setVisibility(View.GONE);
            submit_progress.setVisibility(View.VISIBLE);
        }
        else{
            name_error.setVisibility(View.VISIBLE);
        }
    }
}