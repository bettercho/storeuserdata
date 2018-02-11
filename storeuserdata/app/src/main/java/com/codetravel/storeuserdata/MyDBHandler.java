package com.codetravel.storeuserdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 조연진 on 2018-01-31.
 */

public class MyDBHandler {

    private final String TAG = "MyDBHandler";

    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;

    public MyDBHandler(Context context, String name) {
        mHelper = new MySQLiteOpenHelper(context, name, null, 1);
    }

    public static MyDBHandler open(Context context, String name) {
        return new MyDBHandler(context, name);
    }

    public Cursor select()
    {
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query("student", null, null, null, null, null, null);
        return c;
    }

    public void insert(String name, int age, String address) {

        Log.d(TAG, "insert");

        mDB = mHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("age", age);
        value.put("address", address);

        mDB.insert("student", null, value);

    }

    public void delete(String name)
    {
        Log.d(TAG, "delete");
        mDB = mHelper.getWritableDatabase();
        mDB.delete("student", "name=?", new String[]{name});
    }

    public void close() {
        mHelper.close();
    }

}
