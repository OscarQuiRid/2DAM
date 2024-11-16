package com.example.practica04_login_ddbb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseInterna extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_LOGIN_ERROR = "LOGIN_ERROR";
    public static final String TABLE_USER_LOGIN = "USER_LOGIN";

    public DataBaseInterna(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLoginErrorTable = "CREATE TABLE " + TABLE_LOGIN_ERROR + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "errorTime TEXT, " +
                "errorCode TEXT)";
        db.execSQL(createLoginErrorTable);

        String createUserLoginTable = "CREATE TABLE " + TABLE_USER_LOGIN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "fechaNacimiento TEXT)";
        db.execSQL(createUserLoginTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOGIN);
        onCreate(db);
    }
}