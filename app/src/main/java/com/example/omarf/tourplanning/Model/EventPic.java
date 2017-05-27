package com.example.omarf.tourplanning.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by omarf on 2/14/2017.
 */

public class EventPic  {
    private String picUrl;
    private String picDetails;
    private String time ;

    public EventPic() {
    }

    public EventPic(String picUrl, String picDetails) {
        this.picUrl = picUrl;
        this.picDetails = picDetails;
        setTime();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicDetails() {
        return picDetails;
    }

    public void setPicDetails(String picDetails) {
        this.picDetails = picDetails;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        time=simpleDateFormat.format(new Date());
    }
}
