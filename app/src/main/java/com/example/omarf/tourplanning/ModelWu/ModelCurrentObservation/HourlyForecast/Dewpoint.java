
package com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.HourlyForecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dewpoint {

    @SerializedName("english")
    @Expose
    private String english;
    @SerializedName("metric")
    @Expose
    private String metric;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

}
