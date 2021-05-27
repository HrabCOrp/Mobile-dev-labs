package ua.kpi.comsys.IV8109.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class JSONHandler {

    public String handleJSON(String queryText, String booksJSON, SQLiteDatabase db, String tableName) {
        /* Handling JSON situation */
        // [If request was successful -> place Book:JSON entry into DB]
        if (booksJSON != null) {
            insertData(queryText, booksJSON, db, tableName);
            return  booksJSON;
        // [If not -> try getting JSON from DB]
        } else {
            booksJSON = getFromStorage(queryText, db, tableName);
            return booksJSON;
        }
    }

    /* Place Book:JSON entry to DB */
    public void insertData(String key, String json, SQLiteDatabase db, String tableName) {
        Cursor c = db.rawQuery("select request from "+ tableName +" where request='" + key +"'", null);
        if (!c.moveToFirst()) {
            ContentValues data = new ContentValues();
            data.put("request", key);
            data.put("json", json);
            db.insert(tableName, null, data);
        }
//        } else {
//            c.close();
//        }
    }

    /* Get JSON from DB */
    public String getFromStorage(String book, SQLiteDatabase db, String tableName) { ;
        String selection = "request='"+book+"'";
        Cursor c = db.query(tableName, null, selection, null, null, null, null);

        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex("json"));
        }
//        } else {
//            c.close();
//        }

        return null;
    }
}
