package com.example.omarf.tourplanning;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.omarf.tourplanning.Model.EventPic;
import com.example.omarf.tourplanning.databinding.ActivityPictureViewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PictureViewActivity extends AppCompatActivity {

    public static final String INTENT_TAG = "picture_view_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityPictureViewBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_picture_view);

        String picRefString=getIntent().getStringExtra(INTENT_TAG);

        DatabaseReference picRef= FirebaseDatabase.getInstance().getReferenceFromUrl(picRefString);

        picRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EventPic eventPic=dataSnapshot.getValue(EventPic.class);
                Picasso.with(PictureViewActivity.this).load(eventPic.getPicUrl()).into(binding.fullSizeImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
