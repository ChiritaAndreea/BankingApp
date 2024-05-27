package com.example.bankingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PaymentRep extends SQLiteDatabaseHelper{

    public PaymentRep(Context context) {
        super(context);
    }

    public Cursor getAllPayments() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_TO_IBAN,
                COLUMN_FROM_IBAN,
                COLUMN_AMOUNT,
                COLUMN_DETAILS
        };

        Cursor cursor = db.query(
                TABLE_PAYMENT,   // The table to query
                projection,       // The columns to return
                null,        // The columns for the WHERE clause
                null,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor;
    }

    public void insertPayment(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        long newRowId = db.insert("payment", null, values);

        // Log the result
        if (newRowId != -1) {
            Log.d("MainActivity", "Payment inserted with row id: " + newRowId);
        } else {
            Log.d("MainActivity", "Error inserting payment");
        }
    }

    public Cursor getAllPaymentsByIban(String iban) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_TO_IBAN,
                COLUMN_FROM_IBAN,
                COLUMN_AMOUNT,
                COLUMN_DETAILS
        };

        String sortOrder = COLUMN_ID + " DESC"; // Ordering by COLUMN_NAME in descending order
        String selection = COLUMN_TO_IBAN + " = ? OR " + COLUMN_FROM_IBAN + " = ?";
        String[] selectionArgs = { iban, iban };

        Cursor cursor = db.query(
                TABLE_PAYMENT,    // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                sortOrder         // The sort order
        );

        return cursor;
    }
}