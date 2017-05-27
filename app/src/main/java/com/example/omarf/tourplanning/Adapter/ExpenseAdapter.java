package com.example.omarf.tourplanning.Adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omarf.tourplanning.Model.Expense;
import com.example.omarf.tourplanning.R;


import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by omarf on 1/30/2017.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {

    private ArrayList<Expense> mExpenseList;

    public ExpenseAdapter(ArrayList<Expense> mExpenseList) {
        this.mExpenseList = mExpenseList;
    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ExpenseRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.expense_row,parent,false);*/

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);

        return new ExpenseHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, int position) {
        Expense expense=mExpenseList.get(position);
        holder.mMonth.setText(expense.getmMonth());
        holder.mDay.setText(expense.getmDay());
        holder.mItemName.setText(expense.getmItemName());
        holder.mPrice.setText(String.valueOf(expense.getmPrice()));


//        holder.mBinding.setExpense(expense);

    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public class ExpenseHolder extends RecyclerView.ViewHolder{
        private TextView mItemName;
        private TextView mPrice;
        private TextView mDay;
        private TextView mMonth;
        public ExpenseHolder(View itemView) {
            super(itemView);
            mItemName= (TextView) itemView.findViewById(R.id.item_name_text_view);
            mPrice= (TextView) itemView.findViewById(R.id.price_text_view);
            mDay= (TextView) itemView.findViewById(R.id.day_text_view);
            mMonth= (TextView) itemView.findViewById(R.id.month_text_view);


        }
       /* ExpenseRowBinding mBinding;
        public ExpenseHolder(ExpenseRowBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }*/
    }
}
