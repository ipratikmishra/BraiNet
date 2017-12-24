package com.example.born_2_code.brainnet1;

/**
 * Created by Born_2_Code on 11/24/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String BRAINET_TABLE_NAME = "brainet";
    public static final String BRAINET_COLUMN_EMAIL = "email";
    public static final String BRAINET_COLUMN_PASSWORD = "password";

    /*public static final String BRAINET_COLUMN_EMAIL = "email";
    public static final String BRAINET_COLUMN_PASSWORD = "password";
    public static final String BRAINET_COLUMN_EMAIL = "email";
    public static final String BRAINET_COLUMN_PASSWORD = "password";*/

    public static final String BRAINET_COLUMN_BRAINSIGNAL = "brainsignal";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table brainet " +
                        "(email text primary key, password text, brainsignal text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS brainet");
        onCreate(db);
    }

    public boolean insertContact (String email, String password, String brainsignal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BRAINET_COLUMN_EMAIL, email);
        contentValues.put(BRAINET_COLUMN_PASSWORD, password);
        contentValues.put(BRAINET_COLUMN_BRAINSIGNAL, brainsignal);
        db.insertWithOnConflict(BRAINET_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return true;
    }

    public Cursor getData(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from brainet where email=\""+ email +"\" and password=\""+password+"\"", null );
        /*Cursor res =  db.rawQuery( "select * from brainet where email="+email+" and password="+password+"", null );*/
        return res;
    }

    /*public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }*/

    /*public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }*/
}