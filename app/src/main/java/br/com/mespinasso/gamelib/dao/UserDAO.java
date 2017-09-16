package br.com.mespinasso.gamelib.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.mespinasso.gamelib.models.User;

/**
 * Created by MatheusEspinasso on 07/09/17.
 */

public class UserDAO {

    public static final String TABLE_USER = "sys_user";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";

    private DBOpenHelper dbOpenHelper;

    public UserDAO(Context ctx) { dbOpenHelper = new DBOpenHelper(ctx); }

    public void add(User user) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_NAME, user.getName());

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        if(result == -1)
            throw new RuntimeException("Error while adding user to database");
    }

    public boolean checkUser(String username){
        String[] columns = { COLUMN_USERNAME };

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0)
            return true;

        return false;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USERNAME
        };
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0)
            return true;

        return false;
    }
}
