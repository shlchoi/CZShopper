package ca.uwaterloo.sh6choi.czshopper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sh6choi.czshopper.bus.BusProvider;
import ca.uwaterloo.sh6choi.czshopper.events.ItemsFetchedEvent;
import ca.uwaterloo.sh6choi.czshopper.model.Item;

/**
 * Created by Samson on 2015-10-14.
 */
public class ItemDataSource {

    private SQLiteDatabase mDatabase;
    private CZShopperSQLiteOpenHelper mHelper;
    private String[] mColumns = { CZShopperSQLiteOpenHelper.COLUMN_ID,
            CZShopperSQLiteOpenHelper.COLUMN_CATEGORY,
            CZShopperSQLiteOpenHelper.COLUMN_NAME,
            CZShopperSQLiteOpenHelper.COLUMN_USER_ID,
            CZShopperSQLiteOpenHelper.COLUMN_CREATED_AT,
            CZShopperSQLiteOpenHelper.COLUMN_UPDATED_AT};

    public ItemDataSource(Context context) {
        mHelper = new CZShopperSQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

    public void addItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(CZShopperSQLiteOpenHelper.COLUMN_ID, item.getId());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_CATEGORY, item.getCategory());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_NAME, item.getName());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_USER_ID, item.getUserId());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_CREATED_AT, item.getCreatedAt().getTime());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_UPDATED_AT, item.getUpdatedAt().getTime());
        mDatabase.insertWithOnConflict(CZShopperSQLiteOpenHelper.TABLE_ITEMS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(CZShopperSQLiteOpenHelper.COLUMN_ID, item.getId());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_CATEGORY, item.getCategory());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_NAME, item.getName());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_USER_ID, item.getUserId());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_CREATED_AT, item.getCreatedAt().getTime());
        values.put(CZShopperSQLiteOpenHelper.COLUMN_UPDATED_AT, item.getUpdatedAt().getTime());
        mDatabase.update(CZShopperSQLiteOpenHelper.TABLE_ITEMS, values, CZShopperSQLiteOpenHelper.COLUMN_ID + " = " + item.getId(), null);
    }

    public void deleteItem(Item item) {
        mDatabase.delete(CZShopperSQLiteOpenHelper.TABLE_ITEMS, CZShopperSQLiteOpenHelper.COLUMN_ID + " = " + item.getId(), null);
    }

    public void queryItems() {
        List<Item> items = new ArrayList<>();

        Cursor cursor = mDatabase.query(CZShopperSQLiteOpenHelper.TABLE_ITEMS, mColumns, null, null,
                CZShopperSQLiteOpenHelper.COLUMN_CATEGORY, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }

        cursor.close();

        BusProvider.getDatabaseBus().post(new ItemsFetchedEvent(items));
    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getLong(4), cursor.getLong(4));
        return item;
    }
}
