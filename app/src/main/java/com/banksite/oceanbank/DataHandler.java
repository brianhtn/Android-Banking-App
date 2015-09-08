package com.banksite.oceanbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/*
class that defines an object that can manipulate the SQL Database with insertions, deletes, or
updates
 */
public class DataHandler {
    //all of the information that we want to store about a user
    public static final String FNAME = "fname";  //user's first name
    public static final String LNAME = "lname";  //user's last name
    public static final String EMAIL = "email";  //user's email
    public static final String MONTH = "month";  //user's month of birth
    public static final String DAY = "day";      //user's day of birth
    public static final String YEAR = "year";    //user's year of birth
    public static final String PHONE = "phone";  //user's phone number
    public static final String ADDRESS = "address";  //user's address
    public static final String USER = "user";        //user's username in order to sign in
    public static final String PASSWORD = "password";  //user's password in order to sign in
    public static final String SECURITYQ = "securityq";  //user's security question
    public static final String SECURITYA = "securitya";  //user's security answer
    public static final String ACC1 = "acc1";     //string saying whether user has checking account
    public static final String ACCBAL1 = "accbal1";  //balance of user's checking account
    public static final String ACC2 = "acc2";    //string saying whether user has savings account
    public static final String ACCBAL2 = "accbal2";  //balance of user's savings account
    public static final String ADMIN = "admin";  //string saying whether user is an administrator
    public static final String HISTORY = "history";  //user's transaction history
    public static final String INTEREST1 = "interest1";  //time used for checking account interest
    public static final String INTEREST2 = "interest2";  //time used for savings account interest
    public static final String PENTIME1 = "pentime1";  //time used for checking account penalties
    public static final String PENTIME2 = "pentime2";  //time used for savings account penalties
    public static final String DAYCOUNTER1 = "daycounter1";  //time used to find DAB for checkings account
    public static final String DAYCOUNTER2 = "daycounter2";  //time used to find DAB for savings account
    public static final String DAB1 = "dab1";  //daily average balance for checking account
    public static final String DAB2 = "dab2";  //daily average balance for savings account


    //strings used to create the database
    public static final String TABLE_NAME = "mytable";
    public static final String DATABASE_NAME = "mydatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CREATE = "create table mytable (fname, lname, email, month, day, year, phone, address, user, password, securityq, securitya, acc1, accbal1, acc2, accbal2, interest1, interest2, pentime1, pentime2, daycounter1, daycounter2, dab1, dab2, admin, history);";

    DataBaseHelper dbhelper;
    Context ctx;
    SQLiteDatabase db;

    //constructor for DataHandler which sets the instance variables
    public DataHandler(Context ctx)
    {
        this.ctx = ctx;
        dbhelper = new DataBaseHelper(ctx);
    }

    //a helper class used to link the DataHandler with the SQL database
    private static class DataBaseHelper extends SQLiteOpenHelper {

        //constructor than links to the SQL database
        public DataBaseHelper(Context ctx)
        {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);  //creates the database with the specified rows

            //when the database is first created, enters in the root administrator so that there
            //is an admin that can create other admins
            ContentValues content = new ContentValues();
            content.put(FNAME, "");
            content.put(LNAME, "");
            content.put(EMAIL, "!@#$%^&*()QWERTYUIOP");
            content.put(MONTH, "");
            content.put(DAY, "");
            content.put(YEAR, "");
            content.put(PHONE, "");
            content.put(ADDRESS, "");
            content.put(USER, "AdminOBANK");
            content.put(PASSWORD, "oceanbank10");
            content.put(SECURITYQ, "");
            content.put(SECURITYA, "");
            content.put(ACC1, "Checkings");
            content.put(ACCBAL1, "0");
            content.put(ACC2, "Savings");
            content.put(ACCBAL2, "0");
            content.put(INTEREST1, "");
            content.put(INTEREST2, "");
            content.put(PENTIME1, "");
            content.put(PENTIME2, "");
            content.put(DAYCOUNTER1, "5");
            content.put(DAYCOUNTER2, "5");
            content.put(DAB1, "5");
            content.put(DAB2, "5");
            content.put(ADMIN, "1");
            content.put(HISTORY, "");
            db.insertOrThrow(TABLE_NAME, null, content);

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS mytable ");
            onCreate(db);
        }
    }

    //opens the database for accessing database
    public DataHandler open()
    {
        db = dbhelper.getWritableDatabase();
        return this;
    }

    //closes access to the database
    public void close()
    {
        dbhelper.close();
    }

    //inserts a new row into the database based off the specified parameters
    public long insertData(String fname, String lname, String email, String month, String day, String year, String phone, String address, String user, String password, String securityq, String securitya, String acc1, String accbal1, String acc2, String accbal2, String interest1, String interest2, String pentime1, String pentime2, String daycounter1, String daycounter2, String dab1, String dab2, String admin, String history)
    {
        ContentValues content = new ContentValues();
        content.put(FNAME, fname);
        content.put(LNAME, lname);
        content.put(EMAIL, email);
        content.put(MONTH, month);
        content.put(DAY, day);
        content.put(YEAR, year);
        content.put(PHONE, phone);
        content.put(ADDRESS, address);
        content.put(USER, user);
        content.put(PASSWORD, password);
        content.put(SECURITYQ, securityq);
        content.put(SECURITYA, securitya);
        content.put(ACC1, acc1);
        content.put(ACCBAL1, accbal1);
        content.put(ACC2, acc2);
        content.put(ACCBAL2, accbal2);
        content.put(INTEREST1, interest1);
        content.put(INTEREST2, interest2);
        content.put(PENTIME1, pentime1);
        content.put(PENTIME2, pentime2);
        content.put(DAYCOUNTER1, daycounter1);
        content.put(DAYCOUNTER2, daycounter2);
        content.put(DAB1, dab1);
        content.put(DAB2, dab2);
        content.put(ADMIN, admin);
        content.put(HISTORY, history);
        return db.insertOrThrow(TABLE_NAME, null, content);
    }

    //updates a row in the database based off the parameters, updates the row by searching in the
    //database for the specified email and once that email is found, updates the row that email
    //is in
    public long updateData(String fname, String lname, String email, String month, String day, String year, String phone, String address, String user, String password, String securityq, String securitya, String acc1, String accbal1, String acc2, String accbal2, String interest1, String interest2, String pentime1, String pentime2, String daycounter1, String daycounter2, String dab1, String dab2, String admin, String history)
    {
        ContentValues content = new ContentValues();
        content.put(FNAME, fname);
        content.put(LNAME, lname);
        content.put(EMAIL, email);
        content.put(MONTH, month);
        content.put(DAY, day);
        content.put(YEAR, year);
        content.put(PHONE, phone);
        content.put(ADDRESS, address);
        content.put(USER, user);
        content.put(PASSWORD, password);
        content.put(SECURITYQ, securityq);
        content.put(SECURITYA, securitya);
        content.put(ACC1, acc1);
        content.put(ACCBAL1, accbal1);
        content.put(ACC2, acc2);
        content.put(ACCBAL2, accbal2);
        content.put(INTEREST1, interest1);
        content.put(INTEREST2, interest2);
        content.put(PENTIME1, pentime1);
        content.put(PENTIME2, pentime2);
        content.put(DAYCOUNTER1, daycounter1);
        content.put(DAYCOUNTER2, daycounter2);
        content.put(DAB1, dab1);
        content.put(DAB2, dab2);
        content.put(ADMIN, admin);
        content.put(HISTORY, history);
        db.update(TABLE_NAME, content, "email" + " = ?", new String[] { email } );
        return 1;
    }

    //method that deletes a row in the table by searching for the specified email and deleting
    //the row with that email
    public Integer deleteInfo(String email)
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "email" + " = ?", new String[] { email });
    }

    //method to pull the data from a row and put in in a cursor which allows for access to each
    //column
    public Cursor returnData()
    {
        return db.query(TABLE_NAME, new String[] {FNAME, LNAME, EMAIL, MONTH, DAY, YEAR, PHONE, ADDRESS, USER, PASSWORD, SECURITYQ, SECURITYA, ACC1, ACCBAL1, ACC2, ACCBAL2, INTEREST1, INTEREST2, PENTIME1, PENTIME2, DAYCOUNTER1, DAYCOUNTER2, DAB1, DAB2, ADMIN, HISTORY}, null, null, null, null, null);
    }


}