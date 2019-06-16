package com.elamed.almag.data.PlanApp;

import android.os.Parcel;
import android.os.Parcelable;

import com.elamed.almag.data.Procedure.Procedure;

public class ParcelablePlanDetalization implements Comparable<ParcelablePlanDetalization>, Parcelable {
    private int id;
    private int mode;
    private int day;
    private String duration;
    private boolean isSkip;


    public boolean isSkip() {
        return isSkip;
    }

    public void setSkip(boolean skip) {
        isSkip = skip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(ParcelablePlanDetalization o) {
        return day - o.day;
    }

    protected ParcelablePlanDetalization(Parcel in) {
        id = in.readInt();
        mode = in.readInt();
        day = in.readInt();
        duration = in.readString();
        isSkip = in.readInt() == 1;
    }

    public ParcelablePlanDetalization() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mode);
        dest.writeInt(day);
        dest.writeString(duration);
        dest.writeInt(isSkip ? 1 : 0);
    }

    public static final Parcelable.Creator<ParcelablePlanDetalization> CREATOR = new Creator<ParcelablePlanDetalization>() {
        @Override
        public ParcelablePlanDetalization createFromParcel(Parcel source) {
            return new ParcelablePlanDetalization(source);
        }

        @Override
        public ParcelablePlanDetalization[] newArray(int size) {
            return new ParcelablePlanDetalization[size];
        }
    };

}
