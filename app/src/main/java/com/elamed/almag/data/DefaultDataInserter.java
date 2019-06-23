package com.elamed.almag.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public abstract class DefaultDataInserter {

    static void InsertFirstPlan(SQLiteDatabase db, String namePlan, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DESCRIPTION_PLANS, namePlan);
        contentValues.put(DBHelper.KEY_VERSION_PLANS, 0);
        contentValues.put(DBHelper.KEY_ID_PLAN, id);
        db.insertOrThrow(DBHelper.TABLE_PLANS, null, contentValues);
        for (int i = 1; i <= 2; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "10");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 3; i <= 4; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "7");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 5; i <= 6; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "10");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        contentValues.clear();
        contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, 7);
        contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 0);
        contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "0");
        contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
        contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 1);
        db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        for (int i = 8; i <= 10; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "12");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 11; i <= 13; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "15");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        contentValues.clear();
        contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, 14);
        contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 0);
        contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "0");
        contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
        contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 1);
        db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        for (int i = 15; i <= 17; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "15");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 18; i <= 20; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "20");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 0);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
    }

    static void InsertSecondPlan(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DESCRIPTION_PLANS, "План лечения пациентов от 1 месяца до года");
        contentValues.put(DBHelper.KEY_VERSION_PLANS, 0);
        contentValues.put(DBHelper.KEY_ID_PLAN, 5);
        db.insertOrThrow(DBHelper.TABLE_PLANS, null, contentValues);
        for (int i = 1; i <= 4; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "3-4");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 5; i <= 10; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 2);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "3-4");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
    }

    static void InsertThirdPlan(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DESCRIPTION_PLANS, "От 1 года до 3 лет");
        contentValues.put(DBHelper.KEY_VERSION_PLANS, 0);
        contentValues.put(DBHelper.KEY_ID_PLAN, 6);
        db.insertOrThrow(DBHelper.TABLE_PLANS, null, contentValues);
        for (int i = 1; i <= 4; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "5");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 2);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        contentValues.clear();
        contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, 5);
        contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 2);
        contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "5");
        contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 2);
        contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
        db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        for (int i = 6; i <= 10; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 2);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "5-6");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 1);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
    }

    static void InsertFourthPlan(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DESCRIPTION_PLANS, "От 3 до 7 лет");
        contentValues.put(DBHelper.KEY_VERSION_PLANS, 0);
        contentValues.put(DBHelper.KEY_ID_PLAN, 7);
        db.insertOrThrow(DBHelper.TABLE_PLANS, null, contentValues);
        for (int i = 1; i <= 4; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "7-8");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 5; i <= 10; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 2);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "7-8");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
    }

    static void InsertFivesPlan(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_DESCRIPTION_PLANS, "От 7 до 15 лет");
        contentValues.put(DBHelper.KEY_VERSION_PLANS, 0);
        contentValues.put(DBHelper.KEY_ID_PLAN, 8);
        db.insertOrThrow(DBHelper.TABLE_PLANS, null, contentValues);
        for (int i = 1; i <= 4; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 3);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "10-12");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 4);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
        for (int i = 5; i <= 10; i++) {
            contentValues.clear();
            contentValues.put(DBHelper.KEY_DAY_DETAILED_PLANS, i);
            contentValues.put(DBHelper.KEY_MODE_DETAILED_PLANS, 2);
            contentValues.put(DBHelper.KEY_DURATION_DETAILED_PLANS, "10-12");
            contentValues.put(DBHelper.KEY_PLAN_DETAILED_PLANS, 4);
            contentValues.put(DBHelper.KEY_IS_SKIP_DETAILED_PLANS, 0);
            db.insertOrThrow(DBHelper.TABLE_DETAILED_PLANS, null, contentValues);
        }
    }
}
