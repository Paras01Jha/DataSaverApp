package com.card.datasaverapp.ui.invite;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.card.datasaverapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class invite extends Fragment {

    private TextView referral_c;
    private DatabaseReference ref, joined_ref;
    private FirebaseAuth mAuth;
    private String my_id;
    private ProgressBar progress_bar;
    private CardView code_card;
    private RecyclerView joined_recycler;
    private FirebaseRecyclerAdapter<joined_model, joined_viewholder> adapter;
    private LinearLayout whatsapp, facebook, gmail, instagram, more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_invite, container, false);

        referral_c = root.findViewById(R.id.referral_c);
        progress_bar= root.findViewById(R.id.progress_bar);
        code_card = root.findViewById(R.id.code_card);
        whatsapp = root.findViewById(R.id.whatsapp);
        facebook = root.findViewById(R.id.facebook);
        gmail = root.findViewById(R.id.gmail);
        instagram = root.findViewById(R.id.instagram);
        more = root.findViewById(R.id.more);
        joined_recycler = root.findViewById(R.id.joined_recycler);

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        joined_ref = FirebaseDatabase.getInstance().getReference("JoinedByReferral");

        joined_recycler.setHasFixedSize(true);
        joined_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        joined_recycler.setItemAnimator(new DefaultItemAnimator());

        getUserReferral();

        loadjoinedUser();

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTowhatsApp();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToFacebook();
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                shareToMail();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToInstagram();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Text I want to share.";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
            }
        });

        return root;
    }

    private void loadjoinedUser() {

        try {
            FirebaseRecyclerOptions<joined_model> options = new FirebaseRecyclerOptions.Builder<joined_model>()
                    .setQuery(joined_ref.child(my_id) , joined_model.class).build();

            adapter = new FirebaseRecyclerAdapter<joined_model, joined_viewholder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull joined_viewholder holder, int position, @NonNull joined_model model) {

                    String joinById = model.getJoinedById();
                    holder.getAllDataOfUser(joinById, getActivity());

                }

                @NonNull
                @Override
                public joined_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_layout, parent, false);
                    return new joined_viewholder(view);
                }
            };
            adapter.notifyDataSetChanged();
            adapter.startListening();
            joined_recycler.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void shareToInstagram() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.instagram.android");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Application have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareToMail() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.facebook.katana");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Application have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareToFacebook() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.facebook.katana");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Application have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareTowhatsApp() {

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
//            ToastHelper.MakeShortText("Whatsapp have not been installed.");
        }
    }

    private void getUserReferral() {
        try {
            ref.child(my_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String code = snapshot.child("my_referral").getValue().toString();
                        referral_c.setText(code);
                        progress_bar.setVisibility(View.GONE);
                        code_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickOnCodeToCopy(code);
                            }
                        });

                    }
                    else {
                        progress_bar.setVisibility(View.VISIBLE);
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

    private void clickOnCodeToCopy(String code) {
        ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(code);
        Toast.makeText(getContext(), "Copied To Clipboard", Toast.LENGTH_SHORT).show();
    }
}