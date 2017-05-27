package com.example.omarf.tourplanning.Adapter;
/*

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omarf.tourplanning.Model.EventPic;
import com.example.omarf.tourplanning.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

*/
/**
 * Created by omarf on 2/14/2017.
 *//*


public class EventPicAdapter extends FirebaseRecyclerAdapter<EventPic, */
/*EventPicAdapter.EventPicHolder*//*
EventPicHolder> {


   */
/* private OnItemEventPicListener mOnItemListner;

    public void setmOnItemListner(OnItemEventPicListener mOnItemListner) {
        this.mOnItemListner = mOnItemListner;
    }*//*


    public EventPicAdapter(Query ref) {
        super(EventPic.class, R.layout.event_pic_row, EventPicHolder.class, ref);

    }

    @Override
    protected void populateViewHolder(EventPicHolder viewHolder, EventPic model,  int position) {
        Picasso.with(viewHolder.mView.getContext()).load(model.getPicUrl()).into(viewHolder.mImageView);
        viewHolder.mTextView.setText(model.getPicDetails());
        viewHolder.adapter=EventPicAdapter.this;

    }

  */
/*  public   class EventPicHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTextView;
        private View mView;
        public EventPicHolder(View itemView) {
            super(itemView);
           mView=itemView;
            mImageView= (ImageView) itemView.findViewById(R.id.event_pic_image_view);
            mTextView= (TextView) itemView.findViewById(R.id.event_pic_details_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemListner.onClickItemPicRow(getRef(getLayoutPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemListner.onLongClickItemPicRow(getRef(getLayoutPosition()));
                    return true;
                }
            });
        }
    }

    public interface OnItemEventPicListener {
        public void onClickItemPicRow(DatabaseReference reference);
        public void onLongClickItemPicRow(DatabaseReference reference);
    }*//*


}
*/
