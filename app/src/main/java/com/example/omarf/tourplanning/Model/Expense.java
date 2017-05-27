package com.example.omarf.tourplanning.Model;

/*
import android.databinding.BaseObservable;
import android.databinding.Bindable;



//import com.example.omarf.tourplanning.BR;


import com.android.databinding.library.baseAdapters.BR;
*/

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by omarf on 1/30/2017.
 */

public class Expense {

    private String mItemName;
    private int mPrice;
    private String mDay;
    private String mMonth;

    public Expense(String mItemName, int mCoast, String mDay, String mMonth) {
        this.mItemName = mItemName;
        this.mPrice = mCoast;
        setTime();
    }

    public Expense() {

    }

    public Expense(String itemName, int itemPrice) {
        mItemName=itemName;
        mPrice=itemPrice;
        //Date date=new Date();
        setTime();
    }

    private void setTime() {
        Date date= new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMM");
        mMonth=dateFormat.format(date);
        dateFormat=new SimpleDateFormat("dd");
        mDay=dateFormat.format(date);
    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }
}

