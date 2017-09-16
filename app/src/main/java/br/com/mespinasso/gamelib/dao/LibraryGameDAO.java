package br.com.mespinasso.gamelib.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.mespinasso.gamelib.models.CatalogGame;
import br.com.mespinasso.gamelib.models.LibraryGame;

/**
 * Created by MatheusEspinasso on 11/09/17.
 */
public class LibraryGameDAO {

    public static final String TABLE_LIBRARY_GAME = "library_game";
    public static final String COLUMN_GAME_ID = "game_id";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_NOTES = "notes";

    private DBOpenHelper dbOpenHelper;

    public LibraryGameDAO(Context ctx) { dbOpenHelper = new DBOpenHelper(ctx); }

    public void add(LibraryGame game) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_GAME_ID, game.getGame().getId());
        values.put(COLUMN_RATING, game.getRating());
        values.put(COLUMN_NOTES, game.getNotes());

        long result = db.insert(TABLE_LIBRARY_GAME, null, values);
        db.close();

        if(result == -1)
            throw new RuntimeException("Error while adding library game to database");
    }

    public void update(LibraryGame game) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_RATING, game.getRating());
        values.put(COLUMN_NOTES, game.getNotes());

        db.update(TABLE_LIBRARY_GAME, values, COLUMN_GAME_ID + " = " + game.getGame().getId(), null);

        db.close();
    }

    public void delete(LibraryGame game) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        db.delete(TABLE_LIBRARY_GAME, COLUMN_GAME_ID + " = " + game.getGame().getId(), null);

        db.close();
    }


    public boolean checkLibraryGame(int gameId) {
        String[] columns = { COLUMN_GAME_ID };

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selection = COLUMN_GAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(gameId) };

        Cursor cursor = db.query(TABLE_LIBRARY_GAME,
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

    public List<LibraryGame> getAll() {
        List<LibraryGame> games = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_LIBRARY_GAME;

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        LibraryGame game = null;

        if(cursor.moveToFirst()) {
            do {
                CatalogGame catalogGame = new CatalogGame(cursor.getInt(cursor.getColumnIndex(COLUMN_GAME_ID)));
                Double rating = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING));
                String notes = cursor.getString(cursor.getColumnIndex(COLUMN_NOTES));

                game = new LibraryGame(catalogGame, rating, notes);

                games.add(game);
            } while (cursor.moveToNext());
        }

        return games;
    }
}
