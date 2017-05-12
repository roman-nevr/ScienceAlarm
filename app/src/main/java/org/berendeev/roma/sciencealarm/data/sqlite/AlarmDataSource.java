package org.berendeev.roma.sciencealarm.data.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.berendeev.roma.sciencealarm.domain.entity.Alarm;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper.ALARM_TABLE;
import static org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper.CARD;
import static org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper.IS_STARTED;
import static org.berendeev.roma.sciencealarm.data.sqlite.DatabaseOpenHelper.TIME;

public class AlarmDataSource {

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;
    private final ContentValues contentValues;

    public AlarmDataSource(DatabaseOpenHelper openHelper) {
        this.openHelper = openHelper;
        database = openHelper.getWritableDatabase();

        contentValues = new ContentValues();
    }

    public void saveAlarm(Alarm alarm){
        fillContentValues(alarm);

        database.insertWithOnConflict(ALARM_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteAlarm(long id) {
        String selection = _ID + " = ?";
        String[] selectionArgs = {"" + id};
        database.delete(ALARM_TABLE, selection, selectionArgs);
    }

    public Alarm getAlarm(long id){
        String selection = _ID + " = ?";
        String[] selectionArgs = {"" + id};

        Cursor cursor = database.query(ALARM_TABLE, null, selection, selectionArgs, null, null, null);

        Alarm alarm = null;

        if(cursor.moveToFirst()){
            alarm = getAlarmFromCursor(cursor);
        }

        cursor.close();
        return alarm;
    }

    public List<Alarm> getAllAlarm() {
        List<Alarm> alarms = new ArrayList<>();

        Cursor cursor = database.query(ALARM_TABLE, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            alarms.add(getAlarmFromCursor(cursor));
        }
        cursor.close();
        return alarms;
    }

    private Alarm getAlarmFromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(_ID);
        int cardIndex = cursor.getColumnIndex(CARD);
        int timeIndex = cursor.getColumnIndex(TIME);
        int isStartedIndex = cursor.getColumnIndex(IS_STARTED);

        return Alarm.create(cursor.getLong(idIndex),
                cursor.getString(cardIndex),
                cursor.getInt(timeIndex),
                getBoolean(cursor.getInt(isStartedIndex)));
    }

    private void fillContentValues(Alarm alarm) {
        contentValues.clear();

        if(alarm.id() != -1){
            contentValues.put(_ID, alarm.id());
        }
        contentValues.put(CARD, alarm.card());
        contentValues.put(TIME, alarm.time());
        contentValues.put(IS_STARTED, alarm.isStarted());
    }

    private boolean getBoolean(int i){
        return i == 1;
    }
}
