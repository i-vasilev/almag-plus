package com.elamed.almag.data.Timetable;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public enum RemindBefore {
    NO_ALARM(0),
    FIVE_MINUTES(1),
    TEN_MINUTES(2),
    FIVETEEN_MINUTES(3);

    private int value;
    private static Map map = new HashMap<>();

    static {
        for (RemindBefore pageType : RemindBefore.values()) {
            map.put(pageType.value, pageType);
        }
    }

    RemindBefore(int value) {
        this.value = value;
    }

    public static RemindBefore valueOf(int value) {
        return (RemindBefore) map.get(value);
    }

    public int getValue() {
        return value;
    }

    public Calendar takeTime(Calendar calendar) {
        calendar.add(Calendar.MINUTE, -value * 5);
        return calendar;
    }

    public Calendar addTime(Calendar calendar) {
        calendar.add(Calendar.MINUTE, value * 5);
        return calendar;
    }

    @Override
    public String toString() {
        String result = "не напоминать";
        if (value != 0)
            result = value * 5 + " минут";
        return result;
    }
}
