package com.example.urlshortener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LinkShortenApi";
    public static final String TABLE_LINKS = "links";

    public static final String KEY_ID = "_id";
    public static final String URL = "Url";
    public static final String HASH_ID = "HashId";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LINKS +
                "(" + KEY_ID + " integer primary key, " +
                URL + " text, " +
                HASH_ID + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_LINKS);
        onCreate(db);
    }
}
