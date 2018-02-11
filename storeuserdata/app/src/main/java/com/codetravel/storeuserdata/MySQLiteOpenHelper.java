package com.codetravel.storeuserdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 조연진 on 2018-01-31.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "MySQLiteOpenHelper";

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table student (_id integer primary key autoincrement, name text, age integer, address text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql="drop table if exists student";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
