package com.example.bankingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CustomerRep extends SQLiteDatabaseHelper{

    public CustomerRep(Context context) {
        super(context);
    }

    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_ID,
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_IBAN,
                COLUMN_PIN,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_BALANCE
        };

        // Query the customer table for all records
        Cursor cursor = db.query(
                TABLE_CUSTOMER,   // The table to query
                projection,       // The columns to return
                null,             // The columns for the WHERE clause
                null,             // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor;
    }

    public long insertCustomer(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.insert("customer", null, values);
    }

    public void updateCustomer(int id, ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CUSTOMER, values, "ID = ?", new String[]{String.valueOf(id)});
    }

    public void updateCustomerBalance(int id, double balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BALANCE, balance);
        db.update(TABLE_CUSTOMER, values, "ID = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getCustomerIdByUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID
        };

        // Filter results WHERE "id" = id
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };

        // Perform a query on the customer table
        Cursor cursor = db.query(
                TABLE_CUSTOMER,   // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor;
    }

    public Cursor getCustomerById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_IBAN,
                COLUMN_PIN,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_BALANCE
        };

        // Filter results WHERE "id" = id
        String selection = COLUMN_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        // Perform a query on the customer table
        Cursor cursor = db.query(
                TABLE_CUSTOMER,   // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor;
    }

    public Cursor getCustomerByIban(String iban) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_BALANCE
        };

        // Filter results WHERE "id" = id
        String selection = COLUMN_IBAN + " = ? ";
        String[] selectionArgs = { iban };

        // Perform a query on the customer table
        Cursor cursor = db.query(
                TABLE_CUSTOMER,   // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor;
    }

    public boolean doesCustomerExistsByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID
        };

        // Filter results WHERE "id" = id
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };

        // Perform a query on the customer table
        Cursor cursor = db.query(
                TABLE_CUSTOMER,   // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor.moveToFirst();
    }

    public boolean doesCustomerExistsByIban(String iban) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID
        };

        // Filter results WHERE "id" = id
        String selection = COLUMN_IBAN + " = ?";
        String[] selectionArgs = { iban };

        // Perform a query on the customer table
        Cursor cursor = db.query(
                TABLE_CUSTOMER,   // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        return cursor.moveToFirst();
    }

}
