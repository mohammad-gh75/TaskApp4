package org.maktab36.taskapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.maktab36.taskapp.database.TaskDBSchema.*;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, TaskDBSchema.NAME, null, TaskDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TaskTable.NAME + "(" +
                TaskTable.COLS.ID + " integer primary key autoincrement," +
                TaskTable.COLS.UUID + " text," +
                TaskTable.COLS.NAME + " text," +
                TaskTable.COLS.DATE + " long," +
                TaskTable.COLS.STATE + " text," +
                TaskTable.COLS.DESCRIPTION + " text," +
                TaskTable.COLS.USER_ID + " text" +
                ");");

        db.execSQL("CREATE TABLE " + UserTable.NAME + "(" +
                UserTable.COLS.ID + " integer primary key autoincrement," +
                UserTable.COLS.UUID + " text," +
                UserTable.COLS.USERNAME + " text," +
                UserTable.COLS.PASSWORD + " text," +
                UserTable.COLS.MEMBERSHIP_DATE + " text" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
