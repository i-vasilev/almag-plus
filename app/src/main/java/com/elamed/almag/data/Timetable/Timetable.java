package com.elamed.almag.data.Timetable;

import android.os.Parcel;
import android.os.Parcelable;

import com.elamed.almag.data.Procedure.Procedure;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Timetable implements Parcelable {

    private int id;
    private int id_disease;
    private boolean included;
    private String name;//name of disease
    private Date time;//time of everyday treatment
    private int durationOfTreatment; //duration of treatment
    private int interval;
    private int idPlan;
    private List<Procedure> procedureList;
    private RemindBefore remindBefore;
    private String image;

    public List<Procedure> getProcedureList() {
        return procedureList;
    }

    public void setProcedureList(List<Procedure> procedureList) {
        this.procedureList = procedureList;
    }


    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public int getCountMadeProcedures() {
        int result = 0;
        for (Procedure procedure :
                procedureList) {
            if (procedure.isDone()) {
                result++;
            }
        }
        return result;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time.getTime();
    }


    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return "images/" +image;
    }

    public String getTimeString() {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        return fmt.format(time);
    }

    public void setTime(long time) {
        this.time.setTime(time);
    }

    public int getDurationOfTreatment() {
        return durationOfTreatment;
    }

    public void setDurationOfTreatment(int durationOfTreatment) {
        this.durationOfTreatment = durationOfTreatment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RemindBefore getRemindBefore() {
        return remindBefore;
    }

    public void setRemindBefore(RemindBefore remindBefore) {
        this.remindBefore = remindBefore;
    }

    public Timetable(boolean included, String name, Date time, int durationOfTreatment, int interval, int idPlan, RemindBefore remindBefore, String image) {
        this.included = included;
        this.name = name;
        this.time = time;
        this.durationOfTreatment = durationOfTreatment;
        this.interval = interval;
        this.idPlan = idPlan;
        this.remindBefore = remindBefore;
        this.image = image;
    }

    public Timetable() {
        included = true;
        this.time = new Date();
        interval = 1;
    }


    protected Timetable(Parcel in) {
        id = in.readInt();
        included = in.readByte() != 0x00;
        name = in.readString();
        long tmpTime = in.readLong();
        time = tmpTime != -1 ? new Date(tmpTime) : null;
        durationOfTreatment = in.readInt();
        interval = in.readInt();
        idPlan = in.readInt();
        remindBefore = RemindBefore.valueOf(in.readInt());
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (included ? 0x01 : 0x00));
        dest.writeString(name);
        dest.writeLong(time != null ? time.getTime() : -1L);
        dest.writeInt(durationOfTreatment);
        dest.writeInt(interval);
        dest.writeInt(idPlan);
        dest.writeInt(remindBefore.getValue());
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Timetable> CREATOR = new Parcelable.Creator<Timetable>() {
        @Override
        public Timetable createFromParcel(Parcel in) {
            return new Timetable(in);
        }

        @Override
        public Timetable[] newArray(int size) {
            return new Timetable[size];
        }
    };
}