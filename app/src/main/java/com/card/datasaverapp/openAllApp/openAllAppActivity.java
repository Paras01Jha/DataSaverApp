package com.card.datasaverapp.openAllApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.card.datasaverapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class openAllAppActivity extends AppCompatActivity {

    private WebView webView;
    private String app_name;
    private String my_id;
    private DatabaseReference database, mobileData;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_all_app);

        webView = (WebView)findViewById(R.id.webView);

        getSupportActionBar().hide();

        app_name = getIntent().getStringExtra("AppName");

        auth = FirebaseAuth.getInstance();
        my_id = auth.getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance().getReference("Applications").child(my_id);
        mobileData = FirebaseDatabase.getInstance().getReference("RefundData").child(my_id);

        webView.setWebViewClient(new WebViewClient());

        updateUsedTime(app_name);

        if (app_name.equals("WhatsApp")){
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            if(intent != null){
                startActivity(intent);
            }
        }

        else if (app_name.equals("Facebook")){
            try {
                webView.loadUrl("https://www.facebook.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (app_name.equals("Instagram")){
            try {
                webView.loadUrl("https://www.instagram.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (app_name.equals("Flipkart")){
            try {
                webView.loadUrl("https://www.flipkart.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (app_name.equals("Amazon Shopping")){
            try {
                webView.loadUrl("https://www.amazon.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (app_name.equals("YouTube")){
            try {
                webView.loadUrl("https://www.youtube.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (app_name.equals("LinkedIn")){
            try {
                webView.loadUrl("https://www.linkedin.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        int UID = android.os.Process.myUid();
        double rxBytes= 0, txBytes=0;
        rxBytes += getUidRxBytes(UID);
        txBytes += getUidTxBytes(UID);

        double r = rxBytes/(1024*1024);
        double s = txBytes/(1024*1024);

        double total = r+s;

        String refund_data = String.format("%.2f", total) + " MB";

        uploadData(refund_data);

        Log.d("Total_Data_used", String.format("%.2f", total) + " MB");


    }


    private void uploadData(String refund_data) {
        try {
            mobileData.child("mobileData").setValue(refund_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUsedTime(String name) {

        try {

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild(app_name)){

                        String st = snapshot.child(name).child("start_time").getValue().toString();
                        String end = snapshot.child(name).child("end_time").getValue().toString();
                        String to = snapshot.child(name).child("total_time").getValue().toString();

                        long s = Long.parseLong(st);
                        long e = Long.parseLong(end);
                        long t = Long.parseLong(to);

                        String date = snapshot.child(name).child("date").getValue().toString();

                        long current_date = System.currentTimeMillis();
                        String cu_time = String.valueOf(System.currentTimeMillis());

                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy");
//                    Date previous_date = new Date(date);
                        Date c_date = new Date(current_date);
//                    String prev_date = sdf.format(previous_date);
                        String curr_date = sdf1.format(c_date);

                        String tt = String.valueOf((e-s)+t);

                        if (!date.equals(curr_date)){
                            database.child(name).child("start_time").setValue(cu_time);
                            database.child(name).child("total_time").setValue(tt);
                        }
                        else {
                            database.child(name).child("start_time").setValue(cu_time);
                        }

                    }
                    else {

                        long date = Long.parseLong(String.valueOf(System.currentTimeMillis()));
                        Toast.makeText(openAllAppActivity.this, "Not Exist", Toast.LENGTH_SHORT).show();
                        addData(date, name);

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

    private void addData(long date, String name) {
        HashMap<Object, String> hashMap = new HashMap<>();

        long tt = 0;

        SimpleDateFormat d = new SimpleDateFormat("MMM dd,yyyy");
        Date c_d = new Date(date);
        String cur_date = d.format(c_d);

        hashMap.put("date", cur_date);
        hashMap.put("start_time", String.valueOf(date));
        hashMap.put("end_time", String.valueOf(date));
        hashMap.put("total_time", String.valueOf(tt));
        hashMap.put("myId", my_id);

        try {
            database.child(name).setValue(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Read UID Tx Bytes
     *
     * @param uid
     * @return txBytes
     */
    public Long getUidTxBytes(int uid) {
        BufferedReader reader;
        Long txBytes = 0L;
        try {
            reader = new BufferedReader(new FileReader("/proc/uid_stat/" + uid
                    + "/tcp_snd"));
            txBytes = Long.parseLong(reader.readLine());
            reader.close();
        }
        catch (FileNotFoundException e) {
            txBytes = TrafficStats.getUidTxBytes(uid);
            //e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return txBytes;
    }
    /**
     * Read UID Rx Bytes
     *
     * @param uid
     * @return rxBytes
     */
    public Long getUidRxBytes(int uid) {
        BufferedReader reader;
        Long rxBytes = 0L;
        try {
            reader = new BufferedReader(new FileReader("/proc/uid_stat/" + uid
                    + "/tcp_rcv"));
            rxBytes = Long.parseLong(reader.readLine());
            reader.close();
        }
        catch (FileNotFoundException e) {
            rxBytes = TrafficStats.getUidRxBytes(uid);
            //e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return rxBytes;
    }

    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateEndTime();
    }

    private void updateEndTime() {

        try {

            database.child(app_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String st = snapshot.child("start_time").getValue().toString();
                    long s = Long.parseLong(st);
                    long e = System.currentTimeMillis();
                    String d = snapshot.child("date").getValue().toString();
                    String to = snapshot.child("total_time").getValue().toString();
                    long t = Long.parseLong(to);
                    long tt = (e-s)+t;

                    HashMap<Object, String> map1 = new HashMap<>();
                    map1.put("start_time", String.valueOf(s));
                    map1.put("end_time", String.valueOf(e));
                    map1.put("date", d);
                    map1.put("total_time", String.valueOf(tt));
                    map1.put("myId", my_id);

                    database.child(app_name).setValue(map1);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateEndTime();
    }
}