package com.example.omarf.tourplanning.Adapter;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.omarf.tourplanning.Model.Event;
import com.example.omarf.tourplanning.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by omarf on 2/11/2017.
 */

public class EventAdapter extends FirebaseRecyclerAdapter<Event, EventAdapter.EventHolder> {
    private ItemClickListener mItemClickListener;

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public EventAdapter(Query ref) {
        super(Event.class, R.layout.event_row, EventHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(EventHolder viewHolder, Event model, final int position) {

        String budget="Budget "+model.getBudget()+" Taka";
        String tourText=model.getPlaceName()+" Tour";
        viewHolder.mTourNameTextView.setText(tourText);
        viewHolder.mBudgetTextView.setText(budget);
        viewHolder.mStartDateTextView.setText(model.getStartDate());
        viewHolder.mEndDateTextView.setText(model.getEndDate());
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onClickItem(getRef(position));
            }
        });
    }

    public static class EventHolder extends RecyclerView.ViewHolder {
        private TextView mTourNameTextView;
        private TextView mStartDateTextView;
        private TextView mEndDateTextView;
        private TextView mBudgetTextView;
        private View mView;
        public EventHolder(View itemView) {
            super(itemView);
            mBudgetTextView= (TextView) itemView.findViewById(R.id.budget_text_view);
            mTourNameTextView= (TextView) itemView.findViewById(R.id.tour_name_text_view);
            mStartDateTextView= (TextView) itemView.findViewById(R.id.start_date_text_view);
            mEndDateTextView= (TextView) itemView.findViewById(R.id.end_date_text_view);
            mView=itemView;

        }
    }


    public interface ItemClickListener{
        public void onClickItem(DatabaseReference reference);
    }
}
