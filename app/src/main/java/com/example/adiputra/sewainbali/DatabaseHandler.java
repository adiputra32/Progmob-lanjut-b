package com.example.adiputra.sewainbali;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rahul.m on 11-04-2017.
 */


public class DatabaseHandler extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "sewain";
    static final String TABLE_USERS = "users";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_EMAIL = "email";
    static final String KEY_PASSWORD = "password";
    static final String KEY_BIRTHDATE = "birthdate";
    static final String KEY_ADDRESS = "address";
    static final String KEY_PHONE = "phone";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_BIRTHDATE + " DATE,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PHONE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        Log.d("DataSelect", "onCreate: " + CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("DataSelect", "up: oldversion: " + oldVersion + " newversion : " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE users");
        onCreate(sqLiteDatabase);
//        if (oldVersion < 2) {
//            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " +
//                    KEY_BIRTHDAY + " DATE");
//            Log.d("DataSelect", "onCreate: " + KEY_BIRTHDAY);
//        }
//
//        if (oldVersion < 3) {
//            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " +
//                    KEY_ADDRESS + " TEXT");
//            Log.d("DataSelect", "onCreate: " + KEY_ADDRESS);
//        }
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DataSelect", "down: oldversion: " + oldVersion + " newversion : " + newVersion);
        db.execSQL("DROP TABLE users");
        onCreate(db);
    }

    //    public void addUser(String name, String email, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, name);
//        values.put(KEY_EMAIL, email);
//        values.put(KEY_PASSWORD, password);
//        // Inserting Row
//        db.insert(TABLE_USERS, null, values);
//        db.close(); // Closing database connection
//    }
//
//    public User getUser(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
//                        KEY_NAME, KEY_EMAIL, KEY_PASSWORD }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        User usr = new User(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2), cursor.getString(1));
//        // return contact
//        return usr;
//    }
//
//

//    public void readUser(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
//                        KEY_NAME, KEY_EMAIL, KEY_USERNAME, KEY_PASSWORD }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        UserModels contact = new UserModels(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return contact;
//    }
}