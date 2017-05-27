package com.example.omarf.tourplanning;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.omarf.tourplanning.Adapter.EventPicAdapter;
import com.example.omarf.tourplanning.Dialog.ReceivePhotoDialog;

import com.example.omarf.tourplanning.Model.EventPic;
import com.example.omarf.tourplanning.databinding.ActivityEventDetailBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

    public static final String REF_TAG = "details_ref_tag";
    private static final String TAG = "EventDetailActivityTag";
    private static FirebaseRecyclerAdapter mAdapter;
    private static Context mContext;
    private static android.app.FragmentManager mFragmentManager;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mFragmentManager=getFragmentManager();
        ActivityEventDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
        setSupportActionBar(binding.toolbar);
        String refUrl = getIntent().getStringExtra(REF_TAG);
         mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl);



        DatabaseReference eventPicRef = FirebaseDatabase.getInstance()
                .getReference(FirebaseHelper.EVENT_PIC_REF)
                .child(mReference.getKey());

        //  EventPicAdapter mAdapter = new EventPicAdapter(eventPicRef);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.eventRecyclerView.setLayoutManager(manager);




        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceivePhotoDialog dialog = new ReceivePhotoDialog();
                dialog.setmEventKey(mReference.getKey());
                dialog.show(getFragmentManager(), null);
            }
        });

        mAdapter = new FirebaseRecyclerAdapter(EventPic.class, R.layout.event_pic_row, EventPicHolder.class, eventPicRef) {
            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {
                EventPicHolder holder = (EventPicHolder) viewHolder;
                EventPic eventPic = (EventPic) model;
                Picasso.with(EventDetailActivity.this).load(eventPic.getPicUrl()).into(holder.mImageView);
                holder.mTextView.setText(eventPic.getPicDetails());
                holder.mDateTextView.setText(eventPic.getTime());
            }
        };



        binding.eventRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.expense_menu_item:
                startActivity(new Intent(EventDetailActivity.this,EventExpenseActivity.class)
                .putExtra(EventExpenseActivity.REF_TAG,mReference.getKey()));
        }

        return super.onOptionsItemSelected(item);
    }

    public static class EventPicHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView;
        public TextView mDateTextView;
        public View mView;
      //  public EventPicAdapter adapter;


        public EventPicHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.event_pic_image_view);
            mTextView = (TextView) itemView.findViewById(R.id.event_pic_details_tv);
            mDateTextView= (TextView) itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference rowRef=mAdapter.getRef(getLayoutPosition());
                    mContext.startActivity(new Intent(mContext, PictureViewActivity.class)
                            .putExtra(PictureViewActivity.INTENT_TAG, rowRef.toString()));

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final DatabaseReference rowRef=mAdapter.getRef(getLayoutPosition());
                    rowRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            EventPic eventPic=dataSnapshot.getValue(EventPic.class);
                            ReceivePhotoDialog dialog = new ReceivePhotoDialog();
                            dialog.setEventPic(eventPic,rowRef);
                            dialog.show(mFragmentManager,null);




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
            });
        }


    }
}