package com.example.omarf.tourplanning;


import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.omarf.tourplanning.Model.Expense;
import com.example.omarf.tourplanning.databinding.FragmentAddExpenseBBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseBSFragment extends BottomSheetDialogFragment {

    private DataExpenseListener mExpenseListener;

    public AddExpenseBSFragment() {
        // Required empty public constructor
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        // View view=View.inflate(getActivity(),R.layout.fragment_add_expense_b,null);
        final FragmentAddExpenseBBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_add_expense_b, null, false);


        binding.getRoot().getLayoutParams();

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = binding.itemNameTextView.getText().toString();
                int itemPrice = Integer.parseInt(binding.priceTextView.getText().toString());
                Expense expense = new Expense(itemName, itemPrice);
                mExpenseListener.onReceiveExpense(expense);
                dismiss();

              /*  BottomSheetBehavior behavior=BottomSheetBehavior.from(binding.getRoot());
                behavior.setPeekHeight(500);*/


            }
        });
        dialog.setContentView(binding.getRoot());


    }


    public interface DataExpenseListener {
        public void onReceiveExpense(Expense expense);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mExpenseListener = (DataExpenseListener) context;
    }
}
