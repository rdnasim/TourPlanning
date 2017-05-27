package com.example.omarf.tourplanning.Fragment;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omarf.tourplanning.Dialog.AddExpenseDialog;
import com.example.omarf.tourplanning.FirebaseHelper;
import com.example.omarf.tourplanning.Model.Event;
import com.example.omarf.tourplanning.Model.Expense;

import com.example.omarf.tourplanning.R;
import com.example.omarf.tourplanning.databinding.FragmentExpenseBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpenseFragment extends Fragment {

    private static final String TAG = "expense_fragment";
    private static final String KEY_TAG = "key_tag";

    private FragmentExpenseBinding mBinding;

    private DatabaseReference mReference;

    private static FirebaseRecyclerAdapter adapter;
    private String mEventKey;
    private String mUserId;

    private DatabaseReference mExpenseListRef;

    public static ExpenseFragment newInstance(String keyUrl){
        Bundle bundle=new Bundle();
        bundle.putString(KEY_TAG,keyUrl);
        ExpenseFragment expenseFragment=new ExpenseFragment();
        expenseFragment.setArguments(bundle);
        return expenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReference= FirebaseDatabase.getInstance().getReference();


    }

    


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_expense,container,false);
        mEventKey=getArguments().getString(KEY_TAG);
        mUserId=FirebaseHelper.getUserId();
        mExpenseListRef =mReference.child(FirebaseHelper.EXPENSE_REF).child(mUserId).child(mEventKey).child("expenseList");



        //total budget

        DatabaseReference reference=mReference.child(FirebaseHelper.EVENT_REF).child(FirebaseHelper.getUserId()).child(mEventKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event=dataSnapshot.getValue(Event.class);
                mBinding.totalBudgetTextView.setText(String.valueOf(event.getBudget()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mBinding.toolbar);

       /* mBinding.collapsingToolbar.setTitleEnabled(true);
        mBinding.collapsingToolbar.setTitle("Total Expense 100tk");
*/


        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddExpenseDialog dialog=new AddExpenseDialog();
                dialog.setmRefUrl(mExpenseListRef.toString());
                dialog.show(getActivity().getFragmentManager(),null);


            }
        });






        adapter=new FirebaseRecyclerAdapter(Expense.class,
               R.layout.expense_row,
               ExpenseHolder.class,
                mExpenseListRef) {

           @Override
           protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, final int position) {
               ExpenseHolder holder= (ExpenseHolder) viewHolder;
               Expense expense= (Expense) model;
               holder.mDay.setText(expense.getmDay());
               holder.mItemName.setText(expense.getmItemName());
               holder.mMonth.setText(expense.getmMonth());
               holder.mPrice.setText(String.valueOf(expense.getmPrice()));
               holder.mView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                    DatabaseReference ref=  adapter.getRef(position);

                   }
               });


           }
       };

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        mBinding.expenseRecyclerView.setAdapter(adapter);
        mBinding.expenseRecyclerView.setLayoutManager(manager);



        return mBinding.getRoot();
    }







    private static class ExpenseHolder extends RecyclerView.ViewHolder{

        private TextView mItemName;
        private TextView mPrice;
        private TextView mDay;
        private TextView mMonth;
        private View mView;
        public ExpenseHolder( View itemView) {
            super(itemView);
            mView=itemView;
            mItemName= (TextView) itemView.findViewById(R.id.item_name_text_view);
            mPrice= (TextView) itemView.findViewById(R.id.price_text_view);
            mDay= (TextView) itemView.findViewById(R.id.day_text_view);
            mMonth= (TextView) itemView.findViewById(R.id.month_text_view);




        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mExpenseListRef.getParent().child("total_expense").addValueEventListener(totalExpenseValueEventListener);

    }

    @Override
    public void onStop() {
        mExpenseListRef.getParent().child("total_expense").removeEventListener(totalExpenseValueEventListener);
        super.onStop();
    }

    ValueEventListener totalExpenseValueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           // int totalExpense=dataSnapshot.getValue(Integer.class);
          //  mBinding.totalExpenseTextView.setText(String.valueOf(totalExpense));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
