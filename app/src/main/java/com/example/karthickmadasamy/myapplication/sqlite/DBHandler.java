package com.example.karthickmadasamy.myapplication.sqlite;

/**
 * Created by Karthick.Madasamy on 12/7/2019.
 */


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.inputmethodservice.Keyboard;
        import android.util.Log;

        import com.example.karthickmadasamy.myapplication.models.Rows;

        import java.util.ArrayList;
        import java.util.List;

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

    public DBHandler(Context context) {
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
        boolean isAdded = false;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (Rows dbRowModel : feederModel.getFeederModel()) {
            values.put(KEY_MAIN_TITLE, feederModel.getTitle());
            if(dbRowModel.getTitle() == null){
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
    }

    // Adding new feeder
    public void addFeeder(DBModel feederModel) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for(Rows dbRowModel: feederModel.getFeederModel()){
            values.put(KEY_MAIN_TITLE, feederModel.getTitle());

            Cursor c = db.rawQuery("SELECT * FROM feeders WHERE feeder_title = '"+dbRowModel.getTitle()+"'", null);

            if(c.moveToFirst())

            // Feeder Main Title
            values.put(KEY_TITLE, dbRowModel.getTitle()); //  Row Title
            values.put(KEY_DESCRIPTION, dbRowModel.getDescription()); //  Row Description
            values.put(KEY_IMAGE, dbRowModel.getImageHref()); //  Row Image
            db.insert(TABLE_FEEDER, null, values);

        }

        // Inserting Row
        db.close(); // Closing database connection
    }

    // Updating feeder
    public void updateFeeder(DBModel feederModel) {
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDER ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                int primaryKey = Integer.parseInt(cursor.getString(0));
                ContentValues values = new ContentValues();
                values.put(KEY_TITLE, feederModel.getFeederModel().get(primaryKey-1).getTitle());
                values.put(KEY_DESCRIPTION, feederModel.getFeederModel().get(primaryKey-1).getDescription());
                values.put(KEY_IMAGE, feederModel.getFeederModel().get(primaryKey-1).getImageHref());

                db.update(TABLE_FEEDER, values, KEY_ID + " = ?",
                        new String[]{String.valueOf(primaryKey)});

            } while ((cursor.moveToNext()));
        }

    }

//    Getting one feeder
/*    public DBHandler getFeeders(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FEEDER, new String[]{KEY_ID,
                        KEY_NAME, KEY_SH_ADDR}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBHandler contact = new DBHandler(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return shop
        return contact;
    }*/


    // Getting All Feeders
    public List<DBRowModel> getAllFeederRows() {
        List<DBRowModel> rowsList = new ArrayList<DBRowModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBRowModel rows = new DBRowModel();
                rows.setTitle(cursor.getString(2));
                rows.setDescription(cursor.getString(3));
                rows.setImageHref(cursor.getString(4));
                rowsList.add(rows);
            } while (cursor.moveToNext());
        }
        // return row list
        return rowsList;
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

    /*// Updating a feeders
    public int updateFeeder(FeederModel feederModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MAIN_TITLE, feederModel.getTitle());

        // updating row
        return db.update(TABLE_FEEDER, values, KEY_MAIN_TITLE + " = ?",
                new String[]{String.valueOf(feederModel.getTitle())});
    }

    // Deleting a feeder
    public void deleteFeeder(FeederModel feederModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEEDER, KEY_MAIN_TITLE + " = ?",
                new String[] { String.valueOf(feederModel.getTitle()) });
        db.close();
    }*/
}

