package com.card.datasaverapp.ui.home;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.card.datasaverapp.R;
import com.card.datasaverapp.openAllApp.openAllAppActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "UsageStatsActivity";
    private static final boolean localLOGV = false;
    private UsageStatsManager mUsageStatsManager;
    private LayoutInflater mInflater;
    private UsageStatsAdapter mAdapter;
    private PackageManager mPm;
    private Animation rocket_bottom_up_animation;
    private DatabaseReference reference, mobile_data_ref, used_time;
    private FirebaseAuth auth;
    private String my_id;
    private CardView progress_card;
    private ProgressBar progress_bar;
    // creating object of RewardedVideoAd
    private String label, application_name;

    private RewardedVideoAd fbRewardedVideoAd;

    public static class AppNameComparator implements Comparator<UsageStats> {
        private Map<String, String> mAppLabelList;

        AppNameComparator(Map<String, String> appList) {
            mAppLabelList = appList;
        }

        @Override
        public final int compare(UsageStats a, UsageStats b) {
            String alabel = mAppLabelList.get(a.getPackageName());
            String blabel = mAppLabelList.get(b.getPackageName());
            return alabel.compareTo(blabel);
        }
    }

    public static class LastTimeUsedComparator implements Comparator<UsageStats> {
        @Override
        public final int compare(UsageStats a, UsageStats b) {
            // return by descending order
            return (int)(b.getLastTimeUsed() - a.getLastTimeUsed());
        }
    }

    public static class UsageTimeComparator implements Comparator<UsageStats> {
        @Override
        public final int compare(UsageStats a, UsageStats b) {
            return (int)(b.getTotalTimeInForeground() - a.getTotalTimeInForeground());
        }
    }

    // View Holder used when displaying views
    static class AppViewHolder {
        TextView pkgName;
        TextView lastTimeUsed;
        TextView usageTime;
        ImageView app_icon;
        TextView dot,use;
    }


    class UsageStatsAdapter extends BaseAdapter {
        // Constants defining order for display order
        private static final int _DISPLAY_ORDER_USAGE_TIME = 0;
        private static final int _DISPLAY_ORDER_LAST_TIME_USED = 1;
        private static final int _DISPLAY_ORDER_APP_NAME = 2;

        private int mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME;
        private LastTimeUsedComparator mLastTimeUsedComparator = new LastTimeUsedComparator();
        private UsageTimeComparator mUsageTimeComparator = new UsageTimeComparator();
        private AppNameComparator mAppLabelComparator;
        private final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
        private final  ArrayMap<String, Drawable> AppIcon = new ArrayMap<>();
        private final ArrayList<UsageStats> mPackageStats = new ArrayList<>();

        UsageStatsAdapter() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -5);

            long hour_in_mil = 1000*1000; // In Milliseconds
            long end_time = System.currentTimeMillis();
            long start_time = end_time - hour_in_mil;

            final List<UsageStats> stats =
                    mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                            start_time, end_time);
            if (stats == null) {
                return;
            }

            ArrayMap<String, UsageStats> map = new ArrayMap<>();
            final int statCount = stats.size();
            for (int i = 0; i < statCount; i++) {
                final android.app.usage.UsageStats pkgStats = stats.get(i);

                // load application labels for each application
                try {
                    ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                    String label = appInfo.loadLabel(mPm).toString();
                    Drawable icon = appInfo.loadIcon(getActivity().getPackageManager());
                    mAppLabelMap.put(pkgStats.getPackageName(), label);
                    AppIcon.put(pkgStats.getPackageName(), icon);

                    UsageStats existingStats =
                            map.get(pkgStats.getPackageName());
                    String packages = appInfo.packageName;

                    if (existingStats == null) {
                        if (packages.equals("com.whatsapp") || packages.equals("com.facebook.katana") || packages.equals("com.snapchat.android")
                                || packages.equals("com.instagram.android") || packages.equals("com.flipkart.android") || packages.equals("in.amazon.mShop.android.shopping")
                                || packages.equals("com.twitter.android") || packages.equals("com.google.android.youtube") || packages.equals("com.linkedin.android")
                                || packages.equals("com.gaana") || packages.equals("com.bsbportal.music") || packages.equals("com.jio.media.jiobeats")){
                            map.put(pkgStats.getPackageName(), pkgStats);
                        }
                    } else {
                        existingStats.add(pkgStats);

                    }

                } catch (PackageManager.NameNotFoundException e) {
                    // This package may be gone.
                }
            }

            mPackageStats.addAll(map.values());

            // Sort list
            mAppLabelComparator = new AppNameComparator(mAppLabelMap);
            sortList();
        }

        @Override
        public int getCount() {
            return mPackageStats.size();
        }

        @Override
        public Object getItem(int position) {
            return mPackageStats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            AppViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.home_app_list_layout, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new AppViewHolder();
                holder.pkgName = (TextView) convertView.findViewById(R.id.package_name);
                holder.lastTimeUsed = (TextView) convertView.findViewById(R.id.last_time_used);
                holder.usageTime = (TextView) convertView.findViewById(R.id.usage_time);
                holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
                holder.dot = (TextView) convertView.findViewById(R.id.dot);
                holder.use = (TextView) convertView.findViewById(R.id.use);
                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (AppViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder
            UsageStats pkgStats = mPackageStats.get(position);

            try {

                if (pkgStats != null) {
                    label = mAppLabelMap.get(pkgStats.getPackageName());
                    Drawable appIcon = AppIcon.get(pkgStats.getPackageName());
                    holder.pkgName.setText(label);
                    holder.app_icon.setImageDrawable(appIcon);
//                holder.lastTimeUsed.setText(DateUtils.formatSameDayTime(pkgStats.getLastTimeUsed(),
//                        System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));

                    if (label.equals("WhatsApp")){
                        holder.lastTimeUsed.setText(DateUtils.formatSameDayTime(pkgStats.getLastTimeUsed(),
                                System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));
                        long t = pkgStats.getTotalTimeInForeground()/1000;
                        holder.usageTime.setText(calculateTime(t));
                        if (t>0){
                            holder.dot.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.green_dot));
                        }
                        else {
                            holder.dot.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.red_dot));
                        }
                    }
                    else {
                        setUsgaeTime(holder, label);
                    }


                    long t = pkgStats.getTotalTimeInForeground()/1000;
//                holder.usageTime.setText(calculateTime(t));
//                if (t>0){
//                    holder.dot.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.green_dot));
//                }
//                else {
//                    holder.dot.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.red_dot));
//                }
//                holder.usageTime.setText(
//                        DateUtils.formatElapsedTime(pkgStats.getTotalTimeInForeground() / 1000));
//                Log.d("TotalTime", String.valueOf(pkgStats.getTotalTimeInForeground()/1000));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            application_name = mAppLabelMap.get(pkgStats.getPackageName());
//                        Intent intent = getPackageManager().getLaunchIntentForPackage(pkgStats.getPackageName());
//                        if(intent != null){
//                            startActivity(intent);
//                        }
                            if (label.equals("WhatsApp")){
                                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                                if(intent != null){
                                    startActivity(intent);
                                }
                            }
                            else {
                                showRewardedVideoAd();
//                            Intent intent = new Intent(getActivity(), openAllAppActivity.class);
//                            intent.putExtra("AppName", label);
//                            startActivity(intent);
                            }

                        }
                    });
                    holder.use.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (label.equals("WhatsApp")){
                                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                                if(intent != null){
                                    startActivity(intent);
                                }
                            }
                            else {
                                Intent intent = new Intent(getActivity(), openAllAppActivity.class);
                                intent.putExtra("AppName", label);
                                startActivity(intent);
                            }
                        }
                    });

                }
                else {
                    Log.w(TAG, "No usage stats info for package:" + position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        private String converLongToTimeChar(long usedTime) {

            String hour="", min="", sec="";

            int h=(int)(usedTime/1000/60/60);
            if (h!=0)
                hour = h+"h ";

            int m=(int)((usedTime/1000/60) % 60);
            if (m!=0)
                min = m+"m ";

            int s=(int)((usedTime/1000) % 60);
            if (s==0 && (h!=0 || m!=0))
                sec="";
            else
                sec = s+"s";

            return hour+min+sec;

        }

        public String calculateTime(long seconds) {
            int day = (int) TimeUnit.SECONDS.toDays(seconds);
            long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
            long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
            long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

//            Log.d("TotalTime", "Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second);

//            System.out.println("Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second);
            return hours+"h "+ minute+"m "+ second+"s";

        }

        void sortList(int sortOrder) {
            if (mDisplayOrder == sortOrder) {
                // do nothing
                return;
            }
            mDisplayOrder= sortOrder;
            sortList();
        }
        private void sortList() {
            if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
                if (localLOGV) Log.i(TAG, "Sorting by usage time");
                Collections.sort(mPackageStats, mUsageTimeComparator);
            } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
                if (localLOGV) Log.i(TAG, "Sorting by last time used");
                Collections.sort(mPackageStats, mLastTimeUsedComparator);
            } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
                if (localLOGV) Log.i(TAG, "Sorting by application name");
                Collections.sort(mPackageStats, mAppLabelComparator);
            }
            notifyDataSetChanged();
        }
    }

    private void setUsgaeTime(AppViewHolder holder, String label) {
        auth = FirebaseAuth.getInstance();
        my_id = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Applications");

        try {

            reference.child(my_id).child(label).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String t_time = snapshot.child("total_time").getValue().toString();
                        String da = snapshot.child("end_time").getValue().toString();

                        long total_time = Long.parseLong(t_time);
                        long date = Long.parseLong(da);
                        if (date>0){
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                            Date resultdate = new Date(date);
                            holder.lastTimeUsed.setText(sdf.format(resultdate));
                        }

                        long tt = total_time/1000;
                        holder.usageTime.setText(calculateTotalTime(tt));
                        if (tt>0){
                            holder.dot.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.green_dot));
                        }

                    }
                    else {
                        holder.usageTime.setText(0+"h "+ 0+"m "+ 0+"s");
                        holder.dot.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.red_dot));
                        holder.lastTimeUsed.setText("This application not be used yet");
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

    private String calculateTotalTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return hours+"h "+ minute+"m "+ second+"s";
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager mViewPager;

        // images array
        int[] images = {R.drawable.invite, R.drawable.unnamed};

        // Creating Object of ViewPagerAdapter
        sliderAdapter mViewPagerAdapter;

        // Initializing the ViewPager Object
        mViewPager = (ViewPager)root.findViewById(R.id.viewPagerMain);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new sliderAdapter(getActivity(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);


//        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPm = getActivity().getPackageManager();

//        Spinner typeSpin = root.findViewById(R.id.typeSpinner);
        ImageView rocket = root.findViewById(R.id.rocket);
        TextView mobile_data = root.findViewById(R.id.mobile_data);
        progress_card = root.findViewById(R.id.progress_card);
        progress_bar = root.findViewById(R.id.progress_bar);
//        typeSpin.setOnItemSelectedListener(this);

        ListView listView = (ListView) root.findViewById(R.id.pkg_list);
        mAdapter = new UsageStatsAdapter();
        listView.setAdapter(mAdapter);

        rocket_bottom_up_animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.rocket_bottom_up_anim);

        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rocket.startAnimation(rocket_bottom_up_animation);
            }
        });

        setMobileData(mobile_data);

        // initialize facebook network
        AudienceNetworkAds.initialize(getContext());

        // initializing and loading rewarded video ad

        loadRewardedVideoAd();

        deleteApplicationNode();

        return root;
    }

    private void deleteApplicationNode() {
        auth = FirebaseAuth.getInstance();
        my_id = auth.getCurrentUser().getUid();
        used_time = FirebaseDatabase.getInstance().getReference("Applications").child(my_id);

        SimpleDateFormat d = new SimpleDateFormat("MMM dd,yyyy");
        String str = d.format(new Date());

        try {
            used_time.orderByChild("myId").equalTo(my_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot ds : snapshot.getChildren()){
                        String date = ""+ds.child("date").getValue().toString();

                        if (!date.equals(str)){
                            ds.getRef().removeValue();
                        }

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

    private void setMobileData(TextView mobile_data) {
        auth = FirebaseAuth.getInstance();
        my_id = auth.getCurrentUser().getUid();
        mobile_data_ref = FirebaseDatabase.getInstance().getReference("RefundData");

        try {
            mobile_data_ref.child(my_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {
                        if (snapshot.exists()){
                            String data = snapshot.child("mobileData").getValue().toString();
                            mobile_data.setText(data);
                            progress_card.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                        else {
                            progress_card.setVisibility(View.GONE);
                            mobile_data.setText("0 MB");

//                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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

    void loadRewardedVideoAd()

    {

        // initializing RewardedVideoAd Object

        // RewardedVideoAd  Constructor Takes 2 Arguments

        // 1)Context

        // 2)Placement Id

        fbRewardedVideoAd = new RewardedVideoAd(

                getActivity(), "VID_HD_16_9_46S_APP_INSTALL#YOUR_PLACEMENT_ID");


        // RewardedVideoAd AdListener

        fbRewardedVideoAd.setAdListener(

                new RewardedVideoAdListener() {

                    @Override

                    public void onError(Ad ad, AdError error)

                    {

                        // Showing Toast Message

                        Toast

                                .makeText(getActivity(),

                                        "onError",

                                        Toast.LENGTH_SHORT)

                                .show();

                    }



                    @Override public void onAdLoaded(Ad ad)

                    {

                        // Showing Toast Message

                        Toast

                                .makeText(getActivity(),

                                        "onAdLoaded",

                                        Toast.LENGTH_SHORT)

                                .show();

                    }



                    @Override public void onAdClicked(Ad ad)

                    {

                        // Showing Toast Message

                        Toast

                                .makeText(getActivity(),

                                        "onAdClicked",

                                        Toast.LENGTH_SHORT)

                                .show();

                    }



                    @Override

                    public void onLoggingImpression(Ad ad)

                    {

                        // Showing Toast Message

                        Toast

                                .makeText(getActivity(),

                                        "onLoggingImpression",

                                        Toast.LENGTH_SHORT)

                                .show();

                    }



                    @Override

                    public void onRewardedVideoCompleted()

                    {

                        // Showing Toast Message

                        Toast

                                .makeText(

                                        getActivity(),

                                        "onRewardedVideoCompleted",

                                        Toast.LENGTH_SHORT)

                                .show();

                    }



                    @Override

                    public void onRewardedVideoClosed()

                    {

//                        // Showing Toast Message
//
//                        Toast
//
//                                .makeText(getActivity(),
//
//                                        "onRewardedVideoClosed",
//
//                                        Toast.LENGTH_SHORT)
//
//                                .show();

                        Intent intent = new Intent(getActivity(), openAllAppActivity.class);
                        intent.putExtra("AppName", application_name);
                        startActivity(intent);



                    }

                });



        // loading Ad

        fbRewardedVideoAd.loadAd();

    }

    public void showRewardedVideoAd()

    {

        // Checking If Ad is Loaded

        // or Not

        if (fbRewardedVideoAd.isAdLoaded()) {

            // showing Video Ad

            fbRewardedVideoAd.show();

        }

        else {

            // Loading Video Ad If it  is Not Loaded

            fbRewardedVideoAd.loadAd();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mAdapter.sortList(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}