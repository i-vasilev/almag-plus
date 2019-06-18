package com.elamed.almag.data.Calendar;

import com.elamed.almag.data.Timetable.Timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar {

    private Timetable timetable = new Timetable();
    List<Date> dates = new ArrayList<>();
    List<Integer> ratesBefore = new ArrayList<>();
    List<Integer> ratesAfter = new ArrayList<>();

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public List<Date> getDates() {
        return dates;
    }

    public List<Integer> getRatesBefore() {
        return ratesBefore;
    }

    public void setRatesBefore(List<Integer> ratesBefore) {
        this.ratesBefore = ratesBefore;
    }

    public List<Integer> getRatesAfter() {
        return ratesAfter;
    }

    public void setRatesAfter(List<Integer> ratesAfter) {
        this.ratesAfter = ratesAfter;
    }
}