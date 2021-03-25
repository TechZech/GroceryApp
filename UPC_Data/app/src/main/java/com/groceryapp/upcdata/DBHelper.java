package com.groceryapp.upcdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.groceryapp.upcdata.DB.User.User;

public class DBHelper extends SQLiteOpenHelper {
    public final String USER_TABLE = "USER_TABLE";
    public final String COLUMN_USER_NAME = "USER_NAME";
    public DBHelper(@Nullable Context context) {
        super(context, "User.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT  )";
        db.execSQL(createTableStatement);
    }
    //this is called if the database version number changes. It prevents users apps from breaking when you change the database schema
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addOne(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, user.getName());
        db.insert(USER_TABLE, null, cv);
    }
}
