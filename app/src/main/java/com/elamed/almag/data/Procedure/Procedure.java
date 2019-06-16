package com.elamed.almag.data.Procedure;

import android.os.Parcel;
import android.os.Parcelable;

public class Procedure implements Parcelable {
    private int id;
    private boolean isDone;
    private long time;
    private int timetable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTimetable() {
        return timetable;
    }

    public void setTimetable(int timetable) {
        this.timetable = timetable;
    }

    protected Procedure(Parcel in) {
        id = in.readInt();
        isDone = in.readInt() == 1;
        time = in.readLong();
        timetable = in.readInt();
    }

    public Procedure() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(isDone ? 1 : 0);
        dest.writeLong(time);
        dest.writeInt(timetable);
    }

    public static final Parcelable.Creator<Procedure> CREATOR = new Creator<Procedure>() {
        @Override
        public Procedure createFromParcel(Parcel source) {
            return new Procedure(source);
        }

        @Override
        public Procedure[] newArray(int size) {
            return new Procedure[size];
        }
    };
}
