package com.card.datasaverapp.ui.invite;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.card.datasaverapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class joined_viewholder extends RecyclerView.ViewHolder{

    TextView name, coins;
    CircleImageView u_avatar;

    public joined_viewholder(@NonNull View itemView) {
        super(itemView);

        u_avatar = itemView.findViewById(R.id.u_image);
        coins = itemView.findViewById(R.id.coins);
        name = itemView.findViewById(R.id.u_name);
    }

    public void getAllDataOfUser(String joinedFromId, FragmentActivity activity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        try {
            reference.child(joinedFromId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String user_name = snapshot.child("name").getValue().toString();
                    String g = snapshot.child("gender").getValue().toString();

                    name.setText(user_name);
                    if (g.equals("Male")){
                        Picasso.get().load(R.drawable.boy_avatar).into(u_avatar);
                    }
                    else {
                        Picasso.get().load(R.drawable.girl_avatar).into(u_avatar);
                    }

                    getCoinsOfUsers(joinedFromId,activity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCoinsOfUsers(String joinedFromId, FragmentActivity activity) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Coins");
        try {
            ref.child(joinedFromId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.exists()){
                            String c = snapshot.child("coins").getValue().toString();
                            String t = snapshot.child("T").getValue().toString();

                            int c_coin = Integer.parseInt(c);
                            int u_coin = (int) (c_coin*0.1);

                            coins.setText("+ "+u_coin);

                            if (t.equals("Yes")){
                                //
                            }
                            else {
                                //
                            }
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
}
