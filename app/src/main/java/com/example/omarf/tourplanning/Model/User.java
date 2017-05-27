package com.example.omarf.tourplanning.Model;

/**
 * Created by omarf on 2/3/2017.
 */

public class User {

    private String mId;
    private String mDisplayName;
    private String mEmail;
    private String mPhotoUrl;

    public User(String mId, String mDisplayName, String mEmail, String mPhotoUrl) {
        this.mId = mId;
        this.mDisplayName = mDisplayName;
        this.mEmail = mEmail;
        this.mPhotoUrl = mPhotoUrl;
    }

    public User( String mDisplayName, String mEmail, String mPhotoUrl) {
        this.mId = mId;
        this.mDisplayName = mDisplayName;
        this.mEmail = mEmail;
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }
}
