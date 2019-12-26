package com.example.karthickmadasamy.myapplication.sqlite;

/**
 * Created by Karthick.Madasamy on 12/7/2019.
 */


        import android.annotation.SuppressLint;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.AsyncTask;
        import android.util.Log;
        import com.example.karthickmadasamy.myapplication.models.Rows;
        import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "feederInfo";

    // Feeder table name
    private static final String TABLE_FEEDER = "feeders";

    // Feeder Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MAIN_TITLE = "main_title";
    private static final String KEY_TITLE = "feeder_title";
    private static final String KEY_DESCRIPTION = "feeder_description";
    private static final String KEY_IMAGE = "feeder_image";


    /*public DBHandler(DisposableObserver<FeederModel> context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSI);
    }*/

    private static DBHandler instance;
    public static synchronized DBHandler getInstance(Context context){
        if(instance== null){
            instance = new DBHandler(context);
        }
        return instance;
    }
    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FEEDER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MAIN_TITLE + " TEXT,"
                + KEY_TITLE + " TEXT," + KEY_DESCRIPTION + " TEXT,"
                + KEY_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDER);
        // Creating tables again
        onCreate(db);
    }

    public void insertOrUpdateFeederRows(DBModel feederModel) {
        new AsyncTask<DBHandler, Void, Void>() {
            @Override
            protected Void doInBackground(final DBHandler... params) {
                boolean isAdded = false;
                SQLiteDatabase db = params[0].getWritableDatabase();

                ContentValues values = new ContentValues();
                for (Rows dbRowModel : feederModel.getFeederModel()) {
                    values.put(KEY_MAIN_TITLE, feederModel.getTitle());
                    if (dbRowModel.getTitle() == null) {
                        dbRowModel.setTitle("Title");
                    }
                    Cursor c = db.rawQuery("SELECT * FROM feeders WHERE feeder_title = '" + dbRowModel.getTitle() + "'", null);
                    c.moveToFirst();
                    if (c.getCount() == 0) {
                        isAdded = false;

                    } else {
                        isAdded = true;
                    }

                    values.put(KEY_TITLE, dbRowModel.getTitle()); //  Row Title
                    values.put(KEY_DESCRIPTION, dbRowModel.getDescription()); //  Row Description
                    values.put(KEY_IMAGE, dbRowModel.getImageHref());
                    if (!isAdded) {
                        db.insert(TABLE_FEEDER, null, values);

                    } else {
                        db.update(TABLE_FEEDER, values, KEY_TITLE + " = ?",
                                new String[]{dbRowModel.getTitle()});
                    }
                }

            return null;
            }

        }.execute(this);
    }
    // Getting All Feeders
    public ArrayList<DBRowModel> getAllFeederRows() throws Exception {

        return new GetDataTask().execute(this).get();
   }
    // Getting feeder Count
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FEEDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    // Getting Toolbar title
    public String getToolBarTitle() {
        String tooldBarTitle = "" ;
        String countQuery = "SELECT  * FROM " + TABLE_FEEDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tooldBarTitle = cursor.getString(1);

            } while (cursor.moveToNext());
        }
        // return title
        return tooldBarTitle != null ? tooldBarTitle : "About Canadaa";
    }

   @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends AsyncTask<DBHandler, Void, ArrayList<DBRowModel>> {
        @Override
        protected ArrayList<DBRowModel> doInBackground(DBHandler... params) {
            ArrayList<DBRowModel> rowsList = new ArrayList<DBRowModel>();
            String selectQuery = "SELECT  * FROM " + TABLE_FEEDER;

            SQLiteDatabase db = params[0].getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    DBRowModel rows = new DBRowModel();
                    rows.setTitle(cursor.getString(2));
                    rows.setDescription(cursor.getString(3));
                    rows.setImageHref(cursor.getString(4));
                    rowsList.add(rows);
                } while (cursor.moveToNext());
            }
            Log.d("Sqlite","get rowsList");
            return rowsList;
        }
    }
}

