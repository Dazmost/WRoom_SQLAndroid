package com.example.waitingroomlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLInput;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Patient_test.db";
    public static final String TABLE_NAME="patient_tabletest";

    public static final String COL_1="CODE";
    public static final String COL_2="NAME";
    public static final String COL_3="LINENUMBER";
    public static final String COL_4="TIME";
    public static final String COL_5="APPOINTMENT";

    //call constructor to create DATABASE
    public DatabaseHelper(@Nullable Context context) {
        //super(context, name, factory, version);
        super(context, DATABASE_NAME, null, 1);
    }

    //use onCreate to create a table
    @Override
    public void onCreate(SQLiteDatabase db) {
        //executes whatever (String) query inside the brackets
        db.execSQL("CREATE TABLE "+ TABLE_NAME+ "(CODE INTEGER PRIMARY KEY, NAME TEXT, LINENUMBER INTEGER, TIME LONG, APPOINTMENT LONG)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String code, String name, String linenumber, String time, String appointment){
        //creates database and table
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(COL_1,code);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,linenumber);
        contentValues.put(COL_4,time);
        contentValues.put(COL_5,appointment);
        //result is -1 or return value
        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }


    }

    //query to get all the data
    public Cursor getAllData(){
        SQLiteDatabase db= this.getWritableDatabase();
        //result stored in cursor instance and access to the data
        //public Cursor rawQuery (String sql, String[] selectionArgs)
        //Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        //public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        Cursor res = db.query(TABLE_NAME,new String[]{COL_1,COL_2,COL_3,COL_4,COL_5},null,null,null,null,COL_3+" ASC");
        return res;
    }

    public boolean updateData(String code, String name, String linenumber, String time, String appointment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,code);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,linenumber);
        contentValues.put(COL_4,time);
        contentValues.put(COL_5,appointment);
        db.update(TABLE_NAME,contentValues,"CODE = ?", new String[] {code});
        return true;
    }

    public Integer deleteData (String code){
        SQLiteDatabase db = this.getWritableDatabase();
        //ID=? shows which column to delete
        return db.delete(TABLE_NAME, "CODE = ?", new String[]{code});
    }
}