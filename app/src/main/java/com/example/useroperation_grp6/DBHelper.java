package com.example.useroperation_grp6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //database information
    private final static String dbname = "StudentDB.s3db";
    private final static int dbversion = 1;
    private final static String tblname = "students";
    private final static String column1 = "SID";
    private final static String column2 = "FN";
    private final static String column3 = "LN";
    private final static String column4 = "COURSE";
    private final static String column5 = "YRLVL";
    private final static String column6 = "DELETED"; // YES or NO - if deleted or not

    //create the database
    public DBHelper(Context context) { //constructor
        super(context, dbname, null, dbversion);
    }
    //create the table with its fields
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + tblname + " ( " +
                column1 + " TEXT (6) PRIMARY KEY, " +
                column2 + " TEXT (30), " +
                column3 + " TEXT (30), " +
                column4 + " TEXT (20), " +
                column5 + " INT, " +
                column6 + " TEXT (3) )";
        sqLiteDatabase.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tblname);
        onCreate(sqLiteDatabase);
    }
    //retrieve all records from the students table
    public Cursor getAllRecords(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from " + tblname + " where deleted='NO'";
        return db.rawQuery(query, null);
    }
    //retrieve a single record from the students table according to the SID
    public Cursor getRecord(String sid){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + tblname + " where sid='" + sid + "'";
        return db.rawQuery(query, null);
    }
    //generate the Student ID (SID) base on the number of records
    public String getSID(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select sid from " + tblname, null);;
        int count = cursor.getCount();
        String sid = "000000";
        if(count >= 0 && count < 9){
            sid = "00000" + (count+1);
        }
        else if(count >= 9 && count < 99){
            sid = "0000" + (count+1);
        }
        else if(count >= 99 && count < 999){
            sid = "000" + (count+1);
        }
        else if(count >= 999 && count < 9999){
            sid = "00" + (count+1);
        }
        else if(count >= 9999 && count < 99999){
            sid = "0" + (count+1);
        }
        else if(count >= 99999 && count < 999999){
            sid = "" + (count+1);

        }
        return sid;
    }
    //add a record into the students table
    public void addNewRecord(String fn, String ln, String course, int yrlvl){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column1,getSID());
        values.put(column2, fn);
        values.put(column3, ln);
        values.put(column4, course);
        values.put(column5, yrlvl);
        values.put(column6, "NO");
        db.insert(tblname, null, values);
        db.close();
    }
    //update the record base on the SID value
    public void editRecord(String sid, String fn, String ln, String course, int yrlvl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column2, fn);
        values.put(column3, ln);
        values.put(column4, course);
        values.put(column5, yrlvl);
        values.put(column6, "NO");
        String args[] = {sid};
        db.update(tblname, values,"sid=?",args);
        db.close();
    }
    //update the record base on the SID value
    public void deleteRecord(String sid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column6, "YES");
        String args[] = {sid};
        db.update(tblname, values,"sid=?",args);
        db.close();
    }
}