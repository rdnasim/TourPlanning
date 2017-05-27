package com.example.omarf.tourplanning.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.omarf.tourplanning.Model.Expense;
import com.example.omarf.tourplanning.R;
import com.example.omarf.tourplanning.databinding.AddExpenseDialogBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by omarf on 2/15/2017.
 */

public class AddExpenseDialog extends DialogFragment {
    private String mRefUrl;


    public void setmRefUrl(String mRefUrl) {
        this.mRefUrl = mRefUrl;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AddExpenseDialogBinding binding= DataBindingUtil.inflate(LayoutInflater.from(getActivity())
        , R.layout.add_expense_dialog,null,false);



        return new AlertDialog.Builder(getActivity())
                .setTitle("ADD ITEM")
                .setView(binding.getRoot())
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemName=binding.itemNameTextView.getText().toString();
                        int  price= Integer.parseInt(binding.priceTextView.getText().toString());

                        Expense expense=new Expense(itemName,price);
                        addDataToFirebase(expense);
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create();
    }

    private void addDataToFirebase(final Expense expense) {
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(mRefUrl);

        reference.getParent().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()){
                    reference.getParent().child("total_expense").setValue(expense.getmPrice());
                    reference.push().setValue(expense);
                }
                else {
                    reference.push().setValue(expense);

                    reference.getParent().child("total_expense").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int total=dataSnapshot.getValue(Integer.class);
                            total+=expense.getmPrice();
                            reference.getParent().child("total_expense").setValue(total);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
