package com.example.omarf.tourplanning.Model;

/**
 * Created by omarf on 2/11/2017.
 */

public class Event {
    private String placeName;
    private String startDate;
    private String endDate;
    private int budget;


    public Event() {
    }

    public Event(String placeName, String startDate, String endDate, int budget) {
        this.placeName = placeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
