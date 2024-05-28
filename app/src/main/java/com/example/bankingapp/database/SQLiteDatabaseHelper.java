package com.example.bankingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "banking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CUSTOMER = "customer";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IBAN = "iban";
    public static final String COLUMN_PIN = "pin";
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_BALANCE = "balance";
    public static final String TABLE_PAYMENT = "payment";
    public static final String COLUMN_TO_IBAN = "toIban";
    public static final String COLUMN_FROM_IBAN = "fromIban";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DETAILS = "details";

    public static final String CREATE_TABLE_CUSTOMER =
            "CREATE TABLE IF NOT EXISTS customer (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT, " +
                    "iban TEXT UNIQUE, " +
                    "pin TEXT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "balance REAL)";

    public static final String CREATE_TABLE_TRANSFER =
            "CREATE TABLE IF NOT EXISTS payment (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "toIban TEXT, " +
                    "fromIban TEXT, " +
                    "amount REAL, " +
                    "details TEXT)";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_TRANSFER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade as needed
        db.execSQL("DROP TABLE IF EXISTS customer");
        db.execSQL("DROP TABLE IF EXISTS transfer");
        onCreate(db);
    }


}
