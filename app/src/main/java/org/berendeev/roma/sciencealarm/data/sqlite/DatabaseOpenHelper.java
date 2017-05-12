package org.berendeev.roma.sciencealarm.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DatabaseOpenHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 1;

    //ID | CARD | TIME
    public static final String ALARM_TABLE = "alarms";
    public static final String TIME = "time";
    public static final String CARD = "card";
    public static final String IS_STARTED = "is_started";

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String script = "create table " + ALARM_TABLE + " (" +
                BaseColumns._ID + " integer primary key autoincrement, " +
                TIME + " integer, " +
                CARD + " real not null, " +
                IS_STARTED + " integer not null);";
        db.execSQL(script);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        onCreate(db);
    }

    @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        onCreate(db);
    }
}
