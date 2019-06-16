package com.elamed.almag.data.Calendar;

import com.elamed.almag.data.Timetable.Timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar {

    private Timetable timetable = new Timetable();
    List<Date> dates = new ArrayList<>();

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public List<Date> getDates() {
        return dates;
    }

}