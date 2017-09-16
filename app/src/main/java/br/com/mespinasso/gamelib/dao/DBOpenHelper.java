package br.com.mespinasso.gamelib.dao;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.mespinasso.gamelib.R;

/**
 * Created by MatheusEspinasso on 03/09/17.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gameslib.db";
    private static final int DB_VERSION = 1;

    private Context ctx;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        readAndExecuteSQLScript(db, ctx, R.raw.db_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i = oldVersion; i < newVersion; i++) {
            String migrationFileName = String. format ( "from_%d_to_%d" , i, (i+ 1));
            log( "Looking for migration file: "  + migrationFileName);

            int migrationFileResId = ctx.getResources()
                    .getIdentifier(migrationFileName, "raw", ctx.getPackageName());

            if(migrationFileResId != 0) {
                log("Executing found script");
                readAndExecuteSQLScript(db, ctx, migrationFileResId);
            } else {
                log("Script not found");
            }
        }
    }

    /**
     * Auxiliary method to read scripts
     * @param db
     * @param ctx
     * @param sqlScriptResId
     */
    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, Integer sqlScriptResId) {
        Resources res = ctx.getResources();

        try {
            InputStream is = res.openRawResource(sqlScriptResId);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            executeSQLScript(db, br);

            br.close();
            is.close();

        } catch (IOException e) {
            throw new RuntimeException("Unable to read the SQLite file", e);
        }
    }

    /**
     * Auxiliary method to execute scripts
     * @param db
     * @param br
     * @throws IOException
     */
    private void executeSQLScript(SQLiteDatabase db, BufferedReader br) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();

        while ((line = br.readLine()) != null) {
            statement.append(line);
            statement.append("\n");

            if(line.endsWith(";")) {
                String toExecute = statement.toString();
                log("Executing script: " + toExecute);
                db.execSQL(toExecute);
                statement =  new  StringBuilder();
            }
        }
    }

    /**
     * Creates log entry
     * @param msg
     */
    private void log(String msg) {
        Log.d(DBOpenHelper.class.getSimpleName(), msg);
    }
}
