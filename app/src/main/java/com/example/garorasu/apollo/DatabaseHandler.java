package com.example.garorasu.apollo;

/**
 * Created by garorasu on 20/10/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by garorasu on 18/10/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "carsManager";

    // Contacts table name
    private static final String TABLE_CARS = "cars";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VID = "vehicle_Id";
    private static final String KEY_IN_TIME = "in_time";
    private static final String KEY_OUT_TIME = "out_time";
    private static final String KEY_OCP = "occupancy";
    private static final String KEY_IMG = "image";
    private static final String KEY_FEE = "parking_fee";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + KEY_ID + " LONG PRIMARY KEY," + KEY_VID + " TEXT,"
                + KEY_IN_TIME + " DATETIME,"
                + KEY_OUT_TIME + " DATETIME,"
                + KEY_OCP + " TEXT,"
                + KEY_IMG + " BOOLEAN,"
                + KEY_FEE + " INTEGER" + ")";
        db.execSQL(CREATE_CARS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);

        // Create tables again
        onCreate(db);
    }
    // Adding new car
    public void addCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, car.get_id()); // row id based on car id fare and in time
        values.put(KEY_VID, car.get_vid()); // Car Vehicle number
        values.put(KEY_IN_TIME, car.get_inTime()); // Car entry time
        values.put(KEY_OCP, car.get_ocp());
        values.put(KEY_IMG,car.get_img());
        values.put(KEY_FEE,car.get_pFee());

        // Inserting Row
        db.insert(TABLE_CARS, null, values);
        db.close(); // Closing database connection
    }


    // Getting single car
    public Car getCar(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CARS, new String[] { KEY_ID,
                        KEY_VID, KEY_IN_TIME,KEY_OUT_TIME,KEY_OCP,KEY_IMG,KEY_FEE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Car car = new Car(Long.parseLong(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),Boolean.parseBoolean(cursor.getString(5)),Integer.parseInt(cursor.getString(6)));
        // return contact
        return car;
    }

    // Getting All Cars
    public List<Car> getAllCars() {
        List<Car> carList = new ArrayList<Car>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CARS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Car car = new Car(Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),
                        Boolean.parseBoolean(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)));
                // Adding contact to list
                carList.add(car);
            } while (cursor.moveToNext());
        }

        // return car list
        return carList;
    }

    // Getting total cars Count
    public int getCarsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Car single car
    public int carOut(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OUT_TIME, car.get_outTime());
        values.put(KEY_OCP, car.get_ocp());

        // updating row
        return db.update(TABLE_CARS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(car.get_id()) });
    }

    // Deleting single car
    public void deleteCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARS, KEY_ID + " = ?",
                new String[] { String.valueOf(car.get_id()) });
        db.close();
    }
}

