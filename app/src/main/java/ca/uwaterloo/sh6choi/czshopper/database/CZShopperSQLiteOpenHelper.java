package ca.uwaterloo.sh6choi.czshopper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Samson on 2015-10-14.
 */
public class CZShopperSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = CZShopperSQLiteOpenHelper.class.getCanonicalName();

    public static final String TABLE_ITEMS = "items";
    public static final String TABLE_TEMP_ITEMS = "temp_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    private static final String DATABASE_NAME = "CZShopper.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_ITEM_CREATE = "CREATE TABLE " + TABLE_ITEMS + "(" +
            COLUMN_ID + " INT PRIMARY KEY, " +
            COLUMN_CATEGORY + " TEXT NOT NULL, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_USER_ID + " INTEGER, " +
            COLUMN_CREATED_AT + " INT, " +
            COLUMN_UPDATED_AT + " INT);";

    public CZShopperSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ITEM_CREATE);
        Log.d(TAG, "Database Created");
    }

    @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public void clearTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
}
