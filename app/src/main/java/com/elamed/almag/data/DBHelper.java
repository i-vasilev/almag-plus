package com.elamed.almag.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static volatile DBHelper instance;
    private static Context context;

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "almagDb";
    public static final String TABLE_TIMETABLE = "timetable";
    public static final String TABLE_PROCEDURES = "procedures";
    public static final String TABLE_DISEASES = "diseases";
    public static final String TABLE_ADDITIONAL_ARTICLES = "additional_articles";
    public static final String TABLE_PLANS = "plans";
    public static final String TABLE_DETAILED_PLANS = "detailedPlans";

    public static final String PREF_ALMAG = "almag_";
    public static final String PREF_MAGNETOTHERAPY = "magnet";

    //Table Timetable
    public static final String KEY_ID_TIMETABLE = "_id";
    public static final String KEY_TREATMENT_TIMETABLE = "treatment";
    public static final String KEY_TIME_ALARM_TIMETABLE = "timeAlarm";
    public static final String KEY_DURATION_OF_TREATMENT_TIMETABLE = "durationOfTreatment";
    public static final String KEY_INCLUDED_TIMETABLE = "included";
    public static final String KEY_FREQUENCY_TIMETABLE = "frequency";
    public static final String KEY_START_DAY_TIMETABLE = "startDay";
    public static final String KEY_PLAN_TIMETABLE = "plan_";
    public static final String KEY_REMIND_BEFORE_TIMETABLE = "remindBefore";

    //Table Procedures
    public static final String KEY_ID_PROCEDURES = "_id";
    public static final String KEY_TIMETABLE_PROCEDURES = "timetable";
    public static final String KEY_DAY_PROCEDURES = "day";
    public static final String KEY_DONE_PROCEDURES = "done";
    public static final String KEY_DETAILAED_PLANS_PROCEDURES = "detailed";
    public static final String KEY_RATE_BEFORE_PROCEDURES = "rate_before";
    public static final String KEY_RATE_AFTER_PROCEDURES = "rate_after";

    //Table Treatments
    public static final String KEY_ID_DISEASE = "_id";
    public static final String KEY_DISEASE_NAME = "disease";
    public static final String KEY_DESCRIPTION_DISEASE = "description";
    public static final String KEY_VERSION_DISEASE = "version";
    public static final String KEY_IMAGE_DISEASE = "path_to_image";

    //Table Additional articles
    public static final String KEY_ID_ADDITIONAL_ARTICLES = "_id";
    public static final String KEY_ARTICLE_ADDITIONAL_ARTICLES = "article";
    public static final String KEY_VERSION_ADDITIONAL_ARTICLES = "version";
    public static final String KEY_PREF_TYPE_ADDITIONAL_ARTICLE = "pref";

    //Table Plans
    public static final String KEY_ID_PLAN = "_id";
    public static final String KEY_DESCRIPTION_PLANS = "description";
    public static final String KEY_VERSION_PLANS = "version";

    //Table Detailed Plans
    public static final String KEY_ID_DETAILED_PLANS = "_id";
    public static final String KEY_DAY_DETAILED_PLANS = "day";
    public static final String KEY_MODE_DETAILED_PLANS = "mode";
    public static final String KEY_DURATION_DETAILED_PLANS = "duration";
    public static final String KEY_PLAN_DETAILED_PLANS = "plan_";
    public static final String KEY_IS_SKIP_DETAILED_PLANS = "skip";

    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    public static void setContext(Context context) {
        DBHelper.context = context;
    }

    private DBHelper() {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_TIMETABLE + "("
                + KEY_ID_TIMETABLE + " integer primary key autoincrement, "
                + KEY_TREATMENT_TIMETABLE + " integer, "
                + KEY_TIME_ALARM_TIMETABLE + " Numeric, "
                + KEY_DURATION_OF_TREATMENT_TIMETABLE + " integer, "
                + KEY_INCLUDED_TIMETABLE + " Numeric, "
                + KEY_FREQUENCY_TIMETABLE + " integer, "
                + KEY_START_DAY_TIMETABLE + " Numeric,"
                + KEY_PLAN_TIMETABLE + " integer,"
                + KEY_REMIND_BEFORE_TIMETABLE + " integer, "
                + " foreign key (" + KEY_TREATMENT_TIMETABLE + ") references " + TABLE_DISEASES + "(" + KEY_ID_DISEASE + "),"
                + " foreign key (" + KEY_PLAN_TIMETABLE + ") references " + TABLE_PLANS + "(" + KEY_ID_PLAN + "))");

        db.execSQL("create table " + TABLE_PROCEDURES + "("
                + KEY_ID_PROCEDURES + " integer primary key autoincrement not null, "
                + KEY_TIMETABLE_PROCEDURES + " integer not null references " + TABLE_TIMETABLE + "(" + KEY_ID_TIMETABLE + "),"
                + KEY_DAY_PROCEDURES + " Numeric, "
                + KEY_RATE_AFTER_PROCEDURES + " Numeric, "
                + KEY_RATE_BEFORE_PROCEDURES + " Numeric, "
                + KEY_DONE_PROCEDURES + " Numeric, "
                + KEY_DETAILAED_PLANS_PROCEDURES + " Numeric references " + TABLE_DETAILED_PLANS + "(" + KEY_ID_DETAILED_PLANS + "))");

        db.execSQL("create table " + TABLE_DISEASES + "("
                + KEY_ID_DISEASE + " integer primary key, "
                + KEY_DISEASE_NAME + " text, "
                + KEY_DESCRIPTION_DISEASE + " Text, "
                + KEY_IMAGE_DISEASE + " Text, "
                + KEY_VERSION_DISEASE + " integer )");

        db.execSQL("create table " + TABLE_ADDITIONAL_ARTICLES + "("
                + KEY_ID_ADDITIONAL_ARTICLES + " integer primary key autoincrement, "
                + KEY_ARTICLE_ADDITIONAL_ARTICLES + " text, "
                + KEY_VERSION_ADDITIONAL_ARTICLES + " integer, "
                + KEY_PREF_TYPE_ADDITIONAL_ARTICLE + " text UNIQUE)");

        db.execSQL("create table " + TABLE_PLANS + "("
                + KEY_ID_PLAN + " integer primary key, "
                + KEY_VERSION_PLANS + " integer, "
                + KEY_DESCRIPTION_PLANS + " text)");

        db.execSQL("create table " + TABLE_DETAILED_PLANS + "("
                + KEY_ID_DETAILED_PLANS + " integer primary key, "
                + KEY_DAY_DETAILED_PLANS + " integer, "
                + KEY_MODE_DETAILED_PLANS + " integer, "
                + KEY_DURATION_DETAILED_PLANS + " text, "
                + KEY_IS_SKIP_DETAILED_PLANS + " numeric, "
                + KEY_PLAN_DETAILED_PLANS + " integer references " + TABLE_PLANS + "(" + KEY_ID_PLAN + "))");


        int id = 0;
        ContentValues valuesDiseases = new ContentValues();
        valuesDiseases.put(KEY_DISEASE_NAME, "Артропатии (артриты, артрозы) неинфекционной этиологии");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/artropaties.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "1.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Остеохондропатии, пяточная шпора");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/osteochndropaties.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "8.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Остеохондроз позвоночника, включая грыжу межпозвоночного диска, сколиоз");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/osteochondrosis.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "9.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Остеопороз");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/osteoporosis.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "13.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Гипертоническая болезнь I и II степени. Вегетативная дистония");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/hipertoniya.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "15.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Осложнения сахарного диабета I и II типов");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/diabet.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "17.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Болезни вен и лимфатических сосудов");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/limphat.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "21.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Бронхиальная астма");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/astma.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "22.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Поражения отдельных нервов, нервных корешков");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/insult.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "25.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_DISEASE_NAME, "Скелетные травмы");
        valuesDiseases.put(KEY_DESCRIPTION_DISEASE, getStringFile("articles/diseases/skelet_travmy.html"));
        valuesDiseases.put(KEY_ID_DISEASE, id++);
        valuesDiseases.put(KEY_VERSION_DISEASE, 0);
        valuesDiseases.put(KEY_IMAGE_DISEASE, "28.jpg");
        db.insertOrThrow(TABLE_DISEASES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_ID_ADDITIONAL_ARTICLES, 0);
        valuesDiseases.put(KEY_ARTICLE_ADDITIONAL_ARTICLES, getStringFile("articles/almag_plus.html"));
        valuesDiseases.put(KEY_VERSION_ADDITIONAL_ARTICLES, 1);
        valuesDiseases.put(KEY_PREF_TYPE_ADDITIONAL_ARTICLE, PREF_ALMAG);
        db.insertOrThrow(TABLE_ADDITIONAL_ARTICLES, null, valuesDiseases);
        valuesDiseases.clear();
        valuesDiseases.put(KEY_ID_ADDITIONAL_ARTICLES, 1);
        valuesDiseases.put(KEY_ARTICLE_ADDITIONAL_ARTICLES, getStringFile("articles/magnetotherapy.html"));
        valuesDiseases.put(KEY_VERSION_ADDITIONAL_ARTICLES, 1);
        valuesDiseases.put(KEY_PREF_TYPE_ADDITIONAL_ARTICLE, PREF_MAGNETOTHERAPY);
        db.insertOrThrow(TABLE_ADDITIONAL_ARTICLES, null, valuesDiseases);
        valuesDiseases.clear();
        DefaultDataInserter.InsertFirstPlan(db);
        DefaultDataInserter.InsertSecondPlan(db);
        DefaultDataInserter.InsertThirdPlan(db);
        DefaultDataInserter.InsertFourthPlan(db);
        DefaultDataInserter.InsertFivesPlan(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("drop table if exists " + TABLE_DISEASES);
            db.execSQL("drop table if exists " + TABLE_TIMETABLE);
            db.execSQL("drop table if exists " + TABLE_PROCEDURES);
            db.execSQL("drop table if exists " + TABLE_ADDITIONAL_ARTICLES);
            db.execSQL("drop table if exists " + TABLE_PLANS);
            db.execSQL("drop table if exists " + TABLE_DETAILED_PLANS);

            onCreate(db);
        }
    }

    protected String getStringFile(String filename) {
        String result = "";
        AssetManager assetManager = context.getAssets();


        InputStream input;
        try {
            input = assetManager.open(filename);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
