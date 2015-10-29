package app.ewallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Shows number of items at a certain shop terminal | Inventory
 * Created by Seth Legaspi on 10/29/2015.
 */
public class LocalStockHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "LocalDB_BuyTrans";

    //Table name
    private static final String TABLE_STOCK = "stock";

    //Students column names
    private static final String KEY_ID_SHOPTERMINAL = "Shop_Terminal_ID"; //1st column
    private static final String KEY_ID_ITEM = "Item_ID";
    private static final String KEY_TS_STOCK = "stock_ts";

    public LocalStockHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_STOCK + "(" +
                KEY_ID_SHOPTERMINAL + " INTEGER PRIMARY KEY," +
                KEY_ID_ITEM+ " INT," +
                KEY_TS_STOCK + " DATETIME" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(db);
    }

    public void addStock(Stock stock) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_SHOPTERMINAL, stock.getShopID()); //1st col
        values.put(KEY_ID_ITEM, stock.getItemID()); //2nd col
        values.put(KEY_TS_STOCK, stock.getTimeStamp()); //3rd col

        db.insert(TABLE_STOCK, null, values);
        db.close();
    }

    public Stock getStock(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_STOCK, new String[]{KEY_ID_SHOPTERMINAL, KEY_ID_ITEM,
                        KEY_TS_STOCK}, KEY_ID_SHOPTERMINAL + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Stock stock = new Stock();
        stock.setShopID(Integer.parseInt(cursor.getString(0)));
        stock.setItemID(Integer.parseInt(cursor.getString(1)));
        stock.setTimeStamp(cursor.getString(2));

        db.close();
        return stock;
    }

    public boolean checkExist(int ID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * from " + TABLE_STOCK + " where " + KEY_ID_SHOPTERMINAL + " = " + ID;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() < 1) {
                db.close();
                cursor.close();
                return false;
            } else
                db.close();
            cursor.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }
}
