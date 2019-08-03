package com.elamed.almag.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elamed.almag.data.Calendar.Calendar;
import com.elamed.almag.data.Calendar.CalendarAdapter;
import com.elamed.almag.data.PlanApp.ParcelablePlanDetalization;
import com.elamed.almag.data.Procedure.Procedure;
import com.elamed.almag.data.Timetable.RemindBefore;
import com.elamed.almag.data.Timetable.Timetable;
import com.elamed.almag.data.Timetable.TimetableAdapter;
import com.elamed.almag.data.PlanApp.Plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdaterData {

    public static List<Calendar> calendars = new ArrayList<>();
    public static List<Timetable> timetables = new ArrayList();
    public static List<Plan> plans = new ArrayList<>();
    private static TimetableAdapter timetableAdapter;
    private static CalendarAdapter calendarAdapter;
    private static AlarmReceiver alarmReceiver;


    public static TimetableAdapter getTimetableAdapter() {
        return timetableAdapter;
    }

    public static void setTimetableAdapter(TimetableAdapter timetableAdapter) {
        UpdaterData.timetableAdapter = timetableAdapter;
    }

    public static CalendarAdapter getCalendarAdapter() {
        return calendarAdapter;
    }

    public static void setCalendarAdapter(CalendarAdapter calendarAdapter) {
        UpdaterData.calendarAdapter = calendarAdapter;
    }

    public static List<Procedure> getProceduresByIdTimetable(int id, SQLiteDatabase database) {
        List<Procedure> procedures = new ArrayList<>();
        Cursor cursorProcedures = database.query(DBHelper.TABLE_PROCEDURES, null,
                DBHelper.KEY_TIMETABLE_PROCEDURES + " = ? ", new String[]{Integer.toString(id)},
                null, null, null);
        if (cursorProcedures.moveToFirst()) {
            int idProcedureIndex = cursorProcedures.getColumnIndex(DBHelper.KEY_ID_PROCEDURES);
            int doneProcedureIndex = cursorProcedures.getColumnIndex(DBHelper.KEY_DONE_PROCEDURES);
            int timeProcedureIndex = cursorProcedures.getColumnIndex(DBHelper.KEY_DAY_PROCEDURES);
            do {
                Procedure procedure = new Procedure();
                procedure.setId(cursorProcedures.getInt(idProcedureIndex));
                procedure.setDone(cursorProcedures.getInt(doneProcedureIndex) == 1);
                procedure.setTime(cursorProcedures.getLong(timeProcedureIndex));
                procedures.add(procedure);
            } while (cursorProcedures.moveToNext());
        }
        return procedures;
    }

    public static void selectAllDataFromDB() {
        alarmReceiver = new AlarmReceiver();
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_TIMETABLE, null, null, null, null, null, null);
        calendars.clear();
        timetables.clear();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_TIMETABLE);
            int treatmentIndex = cursor.getColumnIndex(DBHelper.KEY_TREATMENT_TIMETABLE);
            int includedIndex = cursor.getColumnIndex(DBHelper.KEY_INCLUDED_TIMETABLE);
            int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_ALARM_TIMETABLE);
            int idPlanIndex = cursor.getColumnIndex(DBHelper.KEY_PLAN_TIMETABLE);
            int intervalIndex = cursor.getColumnIndex(DBHelper.KEY_FREQUENCY_TIMETABLE);
            int remindBefore = cursor.getColumnIndex(DBHelper.KEY_REMIND_BEFORE_TIMETABLE);
            do {
                Timetable timetable = new Timetable();
                Disease disease = getDiseaseById(cursor.getInt(treatmentIndex));
                timetable.setName(disease.getName());
                timetable.setImage(getImageDisease(disease.getId()));
                timetable.setIncluded(cursor.getInt(includedIndex) != 0);
                timetable.setId(cursor.getInt(idIndex));
                timetable.setTime(cursor.getLong(timeIndex));
                timetable.setIdPlan(cursor.getInt(idPlanIndex));
                timetable.setInterval(cursor.getInt(intervalIndex));
                timetable.setRemindBefore(RemindBefore.valueOf(cursor.getInt(remindBefore)));

                timetable.setProcedureList(getProceduresByIdTimetable(timetable.getId(), database));
                timetable.setDurationOfTreatment(timetable.getProcedureList().size());
                timetable.setTime(cursor.getLong(timeIndex));

                Cursor cursorProcedures = database.query(DBHelper.TABLE_PROCEDURES, null,
                        DBHelper.KEY_TIMETABLE_PROCEDURES + " = ? ", new String[]{Integer.toString(timetable.getId())},
                        null, null, null);
                Calendar calendar = new Calendar();
                calendar.setTimetable(timetable);
                int dateIndex = cursorProcedures.getColumnIndex(DBHelper.KEY_DAY_PROCEDURES);
                int rateBeforeIndex = cursorProcedures.getColumnIndex(DBHelper.KEY_RATE_BEFORE_PROCEDURES);
                int rateAfterIndex = cursorProcedures.getColumnIndex(DBHelper.KEY_RATE_AFTER_PROCEDURES);
                if (cursorProcedures.moveToFirst()) {
                    do {
                        calendar.getDates().add(new Date(cursorProcedures.getLong(dateIndex)));
                        calendar.getRatesBefore().add(cursorProcedures.getInt(rateBeforeIndex));
                        calendar.getRatesAfter().add(cursorProcedures.getInt(rateAfterIndex));
                    } while (cursorProcedures.moveToNext());
                }
                calendars.add(calendar);
                timetables.add(timetable);
            } while (cursor.moveToNext());
        }
        cursor.close();

        plans = getPlans();
    }

    public static void setRating(int position, int rate, int idTimetable, boolean isAfter) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String cell = isAfter ? DBHelper.KEY_RATE_AFTER_PROCEDURES : DBHelper.KEY_RATE_BEFORE_PROCEDURES;
        contentValues.put(cell, rate);

        database.update(DBHelper.TABLE_PROCEDURES, contentValues,
                DBHelper.KEY_TIMETABLE_PROCEDURES + " = ? AND " + DBHelper.KEY_ID_PROCEDURES + " = ? ",
                new String[]{String.valueOf(idTimetable),
                        String.valueOf(getProceduresByIdTimetable(idTimetable, database).get(position).getId())});
    }

    public static void insertNewTimetable(Timetable timetable, Context context) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TREATMENT_TIMETABLE, getIdDisease(timetable.getName()));
        contentValues.put(DBHelper.KEY_DURATION_OF_TREATMENT_TIMETABLE, timetable.getDurationOfTreatment());
        contentValues.put(DBHelper.KEY_INCLUDED_TIMETABLE, timetable.isIncluded());
        contentValues.put(DBHelper.KEY_TIME_ALARM_TIMETABLE, timetable.getTime());
        contentValues.put(DBHelper.KEY_PLAN_TIMETABLE, timetable.getIdPlan());
        contentValues.put(DBHelper.KEY_FREQUENCY_TIMETABLE, timetable.getInterval());
        contentValues.put(DBHelper.KEY_REMIND_BEFORE_TIMETABLE, timetable.getRemindBefore().getValue());
        timetable.setId((int) database.insertOrThrow(DBHelper.TABLE_TIMETABLE, null, contentValues));
        updateAdapters();


        Plan plan = null;
        for (Plan Plan :
                plans) {
            if (Plan.getId() == timetable.getIdPlan()) {
                plan = Plan;
            }
        }
        Date dat = new Date(timetable.getTime());
        java.util.Calendar cal_alarm = java.util.Calendar.getInstance();
        java.util.Calendar cal_now = java.util.Calendar.getInstance();
        cal_alarm.setTime(dat);
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }
        int countProcedures = 0;
        if (plan != null) {
            boolean isFirst = true;
            for (ParcelablePlanDetalization detalization :
                    plan.getPlanDetalizationList()) {
                if (countProcedures++ < timetable.getDurationOfTreatment()) {

                    if (isFirst) {
                        isFirst = false;
                    } else {
                        cal_alarm.add(java.util.Calendar.DAY_OF_YEAR, timetable.getInterval());
                    }
                    if (!detalization.isSkip()) {
                        Procedure procedure = new Procedure();
                        procedure.setDone(false);
                        procedure.setTime(cal_alarm.getTimeInMillis());
                        procedure.setTimetable(timetable.getId());
                        alarmReceiver.setProcedure(procedure);

                        contentValues.clear();
                        contentValues.put(DBHelper.KEY_DONE_PROCEDURES, false);
                        contentValues.put(DBHelper.KEY_TIMETABLE_PROCEDURES, timetable.getId());
                        contentValues.put(DBHelper.KEY_DETAILAED_PLANS_PROCEDURES, detalization.getId());
                        contentValues.put(DBHelper.KEY_DAY_PROCEDURES, cal_alarm.getTimeInMillis());
                        database.insertOrThrow(DBHelper.TABLE_PROCEDURES, null, contentValues);

                        Cursor cursor = database.query(DBHelper.TABLE_PROCEDURES, new String[]{DBHelper.KEY_ID_PROCEDURES},
                                DBHelper.KEY_DAY_PROCEDURES + " = ? AND " + DBHelper.KEY_TIMETABLE_PROCEDURES + " = ?",
                                new String[]{String.valueOf(procedure.getTime()), String.valueOf(procedure.getTimetable())},
                                null, null, null, null);
                        if (cursor.moveToFirst()) {
                            procedure.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID_PROCEDURES)));
                        }
                        alarmReceiver.setAlarm(context, procedure, detalization);
                    }
                }
            }
        }
    }

    public static Timetable getTimetableById(int id) {
        selectAllDataFromDB();
        for (Timetable timetable :
                timetables) {
            if (timetable.getId() == id) {
                return timetable;
            }
        }
        return null;
    }

    public static String getNameTimetableById(int id) {
        String result = "";
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_TIMETABLE, new String[]{DBHelper.KEY_TREATMENT_TIMETABLE},
                DBHelper.KEY_ID_TIMETABLE + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            int idDisease = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_TREATMENT_TIMETABLE));
            result = getDiseaseById(idDisease).getName();
        }
        db.close();
        return result;
    }

    public static void updateTimetable(Timetable timetable, Context context) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TREATMENT_TIMETABLE, getIdDisease(timetable.getName()));
        contentValues.put(DBHelper.KEY_DURATION_OF_TREATMENT_TIMETABLE, timetable.getDurationOfTreatment());
        contentValues.put(DBHelper.KEY_INCLUDED_TIMETABLE, timetable.isIncluded());
        contentValues.put(DBHelper.KEY_TIME_ALARM_TIMETABLE, timetable.getTime());
        contentValues.put(DBHelper.KEY_FREQUENCY_TIMETABLE, timetable.getInterval());
        contentValues.put(DBHelper.KEY_PLAN_TIMETABLE, timetable.getIdPlan());
        contentValues.put(DBHelper.KEY_REMIND_BEFORE_TIMETABLE, timetable.getRemindBefore().getValue());
        String[] id = new String[]{String.valueOf(timetable.getId())};

        database.update(DBHelper.TABLE_TIMETABLE, contentValues, DBHelper.KEY_ID_TIMETABLE + " = ?", id);
        updateAdapters();

        for (int i = 0; i < timetables.size(); i++) {
            if (timetables.get(i).getId() == timetable.getId()) {
                timetable.setProcedureList(timetables.get(i).getProcedureList());
                break;
            }
        }

        Plan plan = null;
        for (Plan Plan :
                plans) {
            if (Plan.getId() == timetable.getIdPlan()) {
                plan = Plan;
                break;
            }
        }


        for (Procedure procedure :
                timetable.getProcedureList()) {
            if (!procedure.isDone()) {
                alarmReceiver.cancelAlarm(context, procedure);
                database.delete(DBHelper.TABLE_PROCEDURES, DBHelper.KEY_ID_PROCEDURES + " = ?", new String[]{String.valueOf(procedure.getId())});
            }
        }
        int countProcedures = timetable.getCountMadeProcedures();
        boolean isFirst = countProcedures <= 0;

        Date dat = new Date(timetable.getTime());
        java.util.Calendar cal_alarm = java.util.Calendar.getInstance();
        java.util.Calendar cal_now = java.util.Calendar.getInstance();
        cal_alarm.setTime(dat);
        if (cal_alarm.before(cal_now) && isFirst) {
            cal_alarm.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }
        for (int i = timetable.getCountMadeProcedures(); i < plan.getPlanDetalizationList().size(); i++) {
            if (countProcedures++ < timetable.getDurationOfTreatment()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    cal_alarm.add(java.util.Calendar.DAY_OF_YEAR, timetable.getInterval());
                }
                if (!plan.getPlanDetalizationList().get(i).isSkip()) {
                    Procedure procedure = new Procedure();
                    procedure.setTime(cal_alarm.getTimeInMillis());
                    procedure.setTimetable(timetable.getId());
                    contentValues.clear();
                    contentValues.put(DBHelper.KEY_DAY_PROCEDURES, procedure.getTime());
                    contentValues.put(DBHelper.KEY_TIMETABLE_PROCEDURES, procedure.getTimetable());
                    contentValues.put(DBHelper.KEY_DONE_PROCEDURES, 0);
                    procedure.setId((int) database.insertOrThrow(DBHelper.TABLE_PROCEDURES, null, contentValues));
                    alarmReceiver.setAlarm(context, procedure, plan.getPlanDetalizationList().get(i));
                }
            }
        }
        updateAdapters();
    }

    public static int getIdDiseaseByName(String name) {
        int id = -1;
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_DISEASES, new String[]{DBHelper.KEY_ID_DISEASE},
                DBHelper.KEY_DISEASE_NAME + " = ?", new String[]{name},
                null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID_DISEASE));
        }
        return id;
    }

    public static void updateProcedure(Procedure procedure, boolean isDone, Context context) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DONE_PROCEDURES, isDone);
        String[] selectionArgs = new String[]{String.valueOf(procedure.getId())};
        database.update(DBHelper.TABLE_PROCEDURES, contentValues, DBHelper.KEY_ID_PROCEDURES + " = ?", selectionArgs);
        updateAdapters();
    }

    public static void updateAdapters() {
        selectAllDataFromDB();
        if (timetableAdapter != null) {
            timetableAdapter.notifyDataSetChanged();
        }
        if (calendarAdapter != null) {
            calendarAdapter.notifyDataSetChanged();
        }
    }

    public static void deleteTimetable(Timetable timetable, Context context) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_PROCEDURES,
                new String[]{DBHelper.KEY_ID_PROCEDURES},
                DBHelper.KEY_TIMETABLE_PROCEDURES + " = ?", new String[]{String.valueOf(timetable.getId())},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Procedure procedure = new Procedure();
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_PROCEDURES);
                procedure.setId(cursor.getInt(idIndex));
                alarmReceiver.setProcedure(procedure);
                alarmReceiver.cancelAlarm(context, procedure);
            } while (cursor.moveToNext());
        }
        database.delete(dbHelper.TABLE_TIMETABLE, dbHelper.KEY_ID_TIMETABLE + " = ?", new String[]{String.valueOf(timetable.getId())});
        UpdaterData.updateAdapters();
    }

    public static void updateDiseases(List<Disease> diseases) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        for (Disease d :
                diseases) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_DISEASE_NAME, d.getName());
            contentValues.put(DBHelper.KEY_DESCRIPTION_DISEASE, d.getDescription());
            contentValues.put(DBHelper.KEY_VERSION_DISEASE, d.getVersion());
            String[] id = {String.valueOf(d.getId())};
            switch (d.changingType) {
                case Add:
                    contentValues.put(DBHelper.KEY_ID_DISEASE, d.getId());
                    database.insert(DBHelper.TABLE_DISEASES, null, contentValues);
                    break;
                case Change:
                    database.update(DBHelper.TABLE_DISEASES, contentValues, DBHelper.KEY_ID_TIMETABLE + " = ?", id);
                    break;
                case Delete:
                    database.delete(DBHelper.TABLE_DISEASES, DBHelper.KEY_ID_TIMETABLE + " = ?", id);
                    break;
            }
        }
        updateAdapters();
    }


    public static ArrayList<Disease> getDiseases() {
        DBHelper dbHelper = DBHelper.getInstance();
        ArrayList<Disease> result = new ArrayList<Disease>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] columns = {DBHelper.KEY_ID_DISEASE, DBHelper.KEY_VERSION_DISEASE};
        Cursor cursor = database.query(DBHelper.TABLE_DISEASES, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(DBHelper.KEY_ID_DISEASE);
            int versionColumn = cursor.getColumnIndex(DBHelper.KEY_VERSION_DISEASE);
            do {
                Disease disease = new Disease();
                disease.setId(cursor.getInt(idColumn));
                disease.setVersion(cursor.getInt(versionColumn));
                result.add(disease);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public static String getDescriptionPlanById(int id) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(id)};
        String result = "";
        Cursor cursor = database.query(DBHelper.TABLE_PLANS, null, DBHelper.KEY_ID_PLAN + " = ?",
                selectionArgs, null, null, null, null);
        if (cursor.moveToNext()) {
            int descriptionColumn = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_PLANS);
            result = cursor.getString(descriptionColumn);
        }
        return result;
    }

    public static ArrayList<Plan> getPlans() {
        DBHelper dbHelper = DBHelper.getInstance();
        ArrayList<Plan> result = new ArrayList<Plan>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_PLANS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(DBHelper.KEY_ID_PLAN);
            int descriptionColumn = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_PLANS);
            int versionColumn = cursor.getColumnIndex(DBHelper.KEY_VERSION_PLANS);
            do {
                Plan plan = new Plan();
                plan.setId(cursor.getInt(idColumn));
                plan.setDescription(cursor.getString(descriptionColumn));
                plan.setVersion(cursor.getInt(versionColumn));
                plan.setPlanDetalizationList(getPlanDetalizationList(plan.getId()));
                result.add(plan);
            } while (cursor.moveToNext());
        }
        return result;
    }

    private static ArrayList<ParcelablePlanDetalization> getPlanDetalizationList(int idPlan) {
        DBHelper dbHelper = DBHelper.getInstance();
        ArrayList<ParcelablePlanDetalization> result = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_DETAILED_PLANS, null,
                DBHelper.KEY_PLAN_DETAILED_PLANS + " = ?",
                new String[]{String.valueOf(idPlan)},
                null, null, null);
        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(DBHelper.KEY_ID_DETAILED_PLANS);
            int dayColumn = cursor.getColumnIndex(DBHelper.KEY_DAY_DETAILED_PLANS);
            int modeColumn = cursor.getColumnIndex(DBHelper.KEY_MODE_DETAILED_PLANS);
            int durationColumn = cursor.getColumnIndex(DBHelper.KEY_DURATION_DETAILED_PLANS);
            int isSkipColumn = cursor.getColumnIndex(DBHelper.KEY_IS_SKIP_DETAILED_PLANS);
            do {
                ParcelablePlanDetalization plan = new ParcelablePlanDetalization();
                plan.setId(cursor.getInt(idColumn));
                plan.setDay(cursor.getInt(dayColumn));
                plan.setMode(cursor.getInt(modeColumn));
                plan.setDuration(cursor.getString(durationColumn));
                plan.setSkip(cursor.getInt(isSkipColumn) == 1);
                result.add(plan);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public static ArrayList<String> getStringPlans() {
        DBHelper dbHelper = DBHelper.getInstance();
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_PLANS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int descriptionColumn = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_PLANS);
            do {
                result.add(cursor.getString(descriptionColumn));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public static ArrayList<String> getStringDiseases() {
        DBHelper dbHelper = DBHelper.getInstance();
        ArrayList<String> resultList = new ArrayList<String>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] columns = new String[]{DBHelper.KEY_DISEASE_NAME};
        Cursor cursor = database.query(DBHelper.TABLE_DISEASES, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameColumn = cursor.getColumnIndex(DBHelper.KEY_DISEASE_NAME);
            do {
                resultList.add(cursor.getString(nameColumn));
            } while (cursor.moveToNext());
        }
        return resultList;
    }

    public static int getIdDisease(String name) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] selectionArgs = {name};
        Cursor cursor = database.query(DBHelper.TABLE_DISEASES, null, DBHelper.KEY_DISEASE_NAME + "= ?",
                selectionArgs, null, null, null, null);
        if (cursor.moveToNext()) {
            int idColumn = cursor.getColumnIndex(DBHelper.KEY_ID_DISEASE);
            return cursor.getInt(idColumn);
        }
        return -1;
    }

    public static String getImageDisease(int id) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(id)};
        String result = "";
        Cursor cursor = database.query(DBHelper.TABLE_DISEASES, null, DBHelper.KEY_ID_DISEASE + "= ?",
                selectionArgs, null, null, null, null);
        if (cursor.moveToNext()) {
            int nameColumn = cursor.getColumnIndex(DBHelper.KEY_IMAGE_DISEASE);
            result = cursor.getString(nameColumn);
        }
        return result;
    }

    public static Disease getDiseaseById(int id) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(id)};
        Disease result = new Disease();
        Cursor cursor = database.query(DBHelper.TABLE_DISEASES, null, DBHelper.KEY_ID_DISEASE + "= ?",
                selectionArgs, null, null, null, null);
        if (cursor.moveToNext()) {
            int nameColumn = cursor.getColumnIndex(DBHelper.KEY_DISEASE_NAME);
            int descriptionColumn = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_DISEASE);
            int versionColumn = cursor.getColumnIndex(DBHelper.KEY_VERSION_DISEASE);
            result.setName(cursor.getString(nameColumn));
            result.setVersion(cursor.getInt(versionColumn));
            result.setId(id);
            result.setDescription(cursor.getString(descriptionColumn));
        }
        return result;
    }

    public static String getArticleByPrefix(String pref) {
        String result = "";
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor articleCursor = database.query(DBHelper.TABLE_ADDITIONAL_ARTICLES, null,
                DBHelper.KEY_PREF_TYPE_ADDITIONAL_ARTICLE + " = ?", new String[]{pref},
                null, null, null);
        if (articleCursor.moveToFirst()) {
            int articleColumn = articleCursor.getColumnIndex(DBHelper.KEY_ARTICLE_ADDITIONAL_ARTICLES);
            result = articleCursor.getString(articleColumn);
        }
        return result;
    }

    public static List<String> getArticlesByPrefix(String pref) {
        List<String> result = new ArrayList<>();
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor articleCursor = database.query(DBHelper.TABLE_ADDITIONAL_ARTICLES, null,
                DBHelper.KEY_PREF_TYPE_ADDITIONAL_ARTICLE + " = ?", new String[]{pref},
                null, null, null);
        if (articleCursor.moveToFirst()) {
            int nameColumn = articleCursor.getColumnIndex(DBHelper.KEY_NAME_ADDITIONAL_ARTICLES);
            do {
                result.add(articleCursor.getString(nameColumn));
            } while (articleCursor.moveToNext());
        }
        return result;
    }

    public static String getArticleByName(String name) {
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] selectionArgs = {name};
        String result = "";
        Cursor cursor = database.query(DBHelper.TABLE_ADDITIONAL_ARTICLES, null, DBHelper.KEY_NAME_ADDITIONAL_ARTICLES + "= ?",
                selectionArgs, null, null, null, null);
        if (cursor.moveToNext()) {
            int additionalArticleColumn = cursor.getColumnIndex(DBHelper.KEY_ARTICLE_ADDITIONAL_ARTICLES);
            result = cursor.getString(additionalArticleColumn);
        }
        return result;
    }

    public static int getIdByDescriptionPlan(String description) {
        int id = 0;
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_PLANS, new String[]{DBHelper.KEY_ID_PLAN},
                DBHelper.KEY_DESCRIPTION_PLANS + " = ?", new String[]{description},
                null, null, null);
        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(DBHelper.KEY_ID_PLAN);
            id = cursor.getInt(idColumn);
        }
        return id;
    }
}
