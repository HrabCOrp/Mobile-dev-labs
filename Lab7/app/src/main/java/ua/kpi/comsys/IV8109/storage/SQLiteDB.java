package ua.kpi.comsys.IV8109.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDB extends SQLiteOpenHelper {
    public SQLiteDB (Context context) {
        super(context, "hrcDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table booktable ("
                + "id integer primary key autoincrement,"
                + "request text,"
                + "json text"
                + ");");

        db.execSQL("create table bookpagetable ("
                + "id integer primary key autoincrement,"
                + "request text,"
                + "json text"
                + ");");

        db.execSQL("create table gallerytable ("
                + "id integer primary key autoincrement,"
                + "request text,"
                + "json text"
                + ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
