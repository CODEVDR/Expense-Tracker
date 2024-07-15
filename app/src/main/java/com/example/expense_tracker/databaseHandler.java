package com.example.expense_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databaseHandler extends SQLiteOpenHelper {

    public databaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE userData(uid varchar(20) PRIMARY KEY, password int(60));";
        sqLiteDatabase.execSQL(query);
        String query1 = "CREATE TABLE userExpense(item varchar(80), price varchar(80), user varchar(90), time varchar(90));";
        sqLiteDatabase.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Handle database upgrades if necessary
    }

    // For Storing User Id And Password
    public void addUserRec(String userID, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        // Setting Values
        ContentValues ct_val = new ContentValues();
        ct_val.put("uid", userID);
        ct_val.put("password", password);
        // Inserting data
        long dataStore = db.insert("userData", null, ct_val);
        db.close();
    }

    // For Storing Expense Data
    public void addExpenseRec(String item, String price, String user, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        // Setting Values
        ContentValues ct_val = new ContentValues();
        ct_val.put("item", item);
        ct_val.put("price", price);
        ct_val.put("user", user);
        ct_val.put("time", time);
        // Inserting data
        long dataStore = db.insert("userExpense", null, ct_val);
        db.close();
    }

    public String[] getUserRec(String uid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query("userData", new String[]{"uid", "password"}, "uid=?", new String[]{uid}, null, null, null);
        if (cur != null && cur.moveToFirst()){
            return new String[]{cur.getString(0), cur.getString(1)};
        } else {
            return new String[]{null, null};
        }
    }

    // For Getting Expense Record
    public String[] getExpenseRec(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM userExpense WHERE user='" + userName + "';", null);
        cur.moveToFirst();
        String[] array = new String[cur.getCount()];
        int i = 0;
        try {
            do {
                String text = "Item\t\t\tPrice\n" + cur.getString(0) + " : ₹" + cur.getString(1) + "\nAt Time : " + cur.getString(3);
                array[i] = text;
                i++;
            } while (cur.moveToNext());
            return array;
        } catch (Exception e) {
            return new String[]{};
        }
    }

    // For Getting Total Price
    public int getTotalPrice(String userName){
        int sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM userExpense WHERE user='" + userName + "';", null);
        cur.moveToFirst();
        try {
            do {
                sum += Integer.parseInt(cur.getString(1));
            } while (cur.moveToNext());
            return sum;
        } catch (Exception e) {
            return 0;
        }
    }

    public void ClearAllExpense(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("userExpense", "user=?", new String[]{userName});
        db.close();
    }

    // Deleting Last Expense
    public void deleteLastExpense(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT rowid FROM userExpense WHERE user=? ORDER BY rowid DESC LIMIT 1;", new String[]{userName});
        if (cur != null && cur.moveToFirst()) {
            long rowid = cur.getLong(0); // The first column in the result is the rowid
            db.delete("userExpense", "rowid=?", new String[]{String.valueOf(rowid)});
        }
        if (cur != null) {
            cur.close();
        }
        db.close();
    }
}
