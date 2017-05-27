package com.example.omarf.tourplanning.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.omarf.tourplanning.FirebaseHelper;
import com.example.omarf.tourplanning.Model.Event;
import com.example.omarf.tourplanning.R;
import com.example.omarf.tourplanning.databinding.AddEventDialogBinding;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by omarf on 2/11/2017.
 */

public class AddEventDialog extends DialogFragment {
    private static final int PLACE_AUTO_COMPLETE_REQUEST = 1;
    public static final int ADD_DIALOG_FLAG = 1;
    AddEventDialogBinding mBinding;
    private Date mInitialDate;
    private Date mLastDate;
    private Event mEvent;
    private DatabaseReference mReference;
    private boolean isUpdateEvent;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.add_event_dialog, null, false);

        mBinding.placePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTO_COMPLETE_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        mBinding.startDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mInitialDate = null;
                createDatePicker(1);

            }
        });
        mBinding.endDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  mLastDate = null;
                createDatePicker(2);
            }
        });

        if(mEvent!=null){

            updateElements();
            return new AlertDialog.Builder(getActivity())
                    .setTitle("EDIT")
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateEventFB();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,null)
                    .setView(mBinding.getRoot())
                    .create();
        }
        else {





            return new AlertDialog.Builder(getActivity())
                    .setTitle("ADD EVENT")
                    .setView(mBinding.getRoot())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            insertDataToFirebase();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           // Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create();
        }


    }

    private void updateEventFB() {
        mReference.child("placeName").setValue(mBinding.placePickerTextView.getText());
        mReference.child("startDate").setValue(mBinding.startDatePickerTextView.getText());
        mReference.child("endDate").setValue(mBinding.endDatePickerTextView.getText());
        mReference.child("budget").setValue(Integer.parseInt(mBinding.budgetEditText.getText().toString()));
    }

    private void updateElements() {
        mBinding.placePickerTextView.setText(mEvent.getPlaceName());
        mBinding.startDatePickerTextView.setText(mEvent.getStartDate());
        mBinding.endDatePickerTextView.setText(mEvent.getEndDate());
        mBinding.budgetEditText.setText(String.valueOf(mEvent.getBudget()));

        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        try {
            mInitialDate=format.parse(mEvent.getStartDate());
            mLastDate=format.parse(mEvent.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void insertDataToFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseHelper.EVENT_REF);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        Event event = new Event(mBinding.placePickerTextView.getText().toString(),
                mBinding.startDatePickerTextView.getText().toString(),
                mBinding.endDatePickerTextView.getText().toString(),
                Integer.parseInt(mBinding.budgetEditText.getText().toString()));
        reference.child(userId).push().setValue(event);


    }

    private void createDatePicker(final int flag) {


        Calendar c = Calendar.getInstance();
        int year, month, day;
        if ((flag == 1 && mInitialDate != null) || isUpdateEvent) {
            c.setTime(mInitialDate);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            mInitialDate=null;

        } else if ((flag == 2 && mLastDate != null)|| isUpdateEvent) {
            c.setTime(mLastDate);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            mLastDate=null;
        }
        else{
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }



        DatePickerDialog datepickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 += 1;
                String date = i2 + "/" + i1 + "/" + i;
                if (flag == 1) {
                    setInitialDate(i2, i1 - 1, i);
                    mBinding.startDatePickerTextView.setText(date);
                } else {
                    setLastDate(i2, i1 - 1, i);
                    mBinding.endDatePickerTextView.setText(date);
                }
            }
        }, year, month, day);

        if (mLastDate != null) {
            datepickerDialog.getDatePicker().setMaxDate(mLastDate.getTime());

        }
        if (mInitialDate != null) {
            datepickerDialog.getDatePicker().setMinDate(mInitialDate.getTime());



        }


        datepickerDialog.show();


    }

    private void setLastDate(int day, int month, int year) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        mLastDate = c.getTime();
    }

    private void setInitialDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        mInitialDate = c.getTime();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTO_COMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mBinding.placePickerTextView.setText(place.getName());
            }
        }
    }

    public void setEvent(Event event, DatabaseReference reference) {
        mEvent = event;
        mReference=reference;
    }
}
