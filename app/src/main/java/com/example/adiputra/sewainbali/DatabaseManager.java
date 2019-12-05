package com.example.adiputra.sewainbali;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHandler dbHelper;

    public DatabaseManager(Context c) {
        this.context = c;
    }

//    public DatabaseManager open() throws SQLException {
//        this.dbHelper = new DatabaseHandler(this.context);
//        this.database = this.dbHelper.getWritableDatabase();
//        return this;
//    }
//
//    public void close() {
//        this.dbHelper.close();
//    }
//
//    public void insert(String name, String email, String password) {
//        ContentValues contentValue = new ContentValues();
//        contentValue.put(DatabaseHandler.KEY_NAME, name);
//        contentValue.put(DatabaseHandler.KEY_EMAIL, email);
//        contentValue.put(DatabaseHandler.KEY_PASSWORD, password);
//        this.database.insert(DatabaseHandler.TABLE_USERS, null, contentValue);
//    }
//
//
//    public Cursor fetch() {
//        Cursor cursor = this.database.query(DatabaseHandler.TABLE_USERS,
//                new String[]{DatabaseHandler.KEY_ID, DatabaseHandler.KEY_NAME,
//                        DatabaseHandler.KEY_EMAIL, DatabaseHandler.KEY_PASSWORD}, null, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }
//
//    public int update(long id, String name, String email, String password) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHandler.KEY_NAME, name);
//        contentValues.put(DatabaseHandler.KEY_EMAIL, email);
//        contentValues.put(DatabaseHandler.KEY_PASSWORD, password);
//        return this.database.update(DatabaseHandler.TABLE_USERS, contentValues, "id = " + id, null);
//    }

//    public void delete(long id) {
//        this.database.delete(DatabaseHandler.CREATE_USERS_TABLE, "id=" + id, null);
//    }

//    UPDATE
//    String name = nameText.getText().toString();
//    String age = ageText.getText().toString();
//    dbManager.update(_id, name, age);
//    Toast.makeText(getApplicationContext(), "Updated successfully!", Toast.LENGTH_SHORT).show();

//    DELETE
//    dbManager.delete(_id);
//    Toast.makeText(getApplicationContext(), "Deleted successfully!", Toast.LENGTH_SHORT).show();
}
