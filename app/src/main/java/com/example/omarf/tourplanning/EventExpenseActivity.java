package com.example.omarf.tourplanning;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.omarf.tourplanning.Fragment.ExpenseFragment;
import com.google.firebase.database.FirebaseDatabase;

public class EventExpenseActivity extends AppCompatActivity {

    public static final String REF_TAG = "ref_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_expense);
        String eventKey = getIntent().getStringExtra(REF_TAG);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null){
         ExpenseFragment  expenseFragment =ExpenseFragment.newInstance(eventKey);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,expenseFragment)
                    .commit();
        }

    }
}
