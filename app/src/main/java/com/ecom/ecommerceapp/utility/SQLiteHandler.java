package com.ecom.ecommerceapp.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ecom.ecommerceapp.model.CartData;

import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EcommerceData";
    private static final String TABLE_NAME = "Products";
    private static final String KEY_ID = " cart_id ";
    private static final String ITEM_NAME = " itemname ";
    private static final String PRICE = " price ";
    private static final String QTY = " qty ";
    private static final String IMAGE = " image ";
    private static final String DESCRIPTION = " description ";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ITEM_NAME + " TEXT," + PRICE + " TEXT," + QTY + " TEXT, " + IMAGE + " BLOB, " + DESCRIPTION + " TEXT " + " )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }

    public boolean CheckIfNameExist(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_NAME + " like '" + fieldValue + "' ";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public boolean addtoCart(String aitemname, String itemprice, String aitemqty, String desc, byte[] pimage) {


        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ITEM_NAME, aitemname);
            values.put(PRICE, String.valueOf(itemprice));
            values.put(QTY, aitemqty);
            values.put(IMAGE, pimage);
            values.put(DESCRIPTION, desc);

            long id = db.insert(TABLE_NAME, null, values);
            if (id == -1) {
                return false;
            }
            return true;
    }

    public boolean UpdateData(String itemname, String itemsellprice, String desc, String mqty) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, itemname);
        values.put(PRICE, String.valueOf(itemsellprice));
        values.put(QTY, mqty);
        values.put(DESCRIPTION, desc);
       // values.put(IMAGE , pimage);

        db.update(TABLE_NAME,values, "ITEMNAME = ?",new String[] {itemname});
          return  true;
       }

    public ArrayList<CartData> getAllData() {
        ArrayList<CartData> d = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String select = "SELECT * FROM " + TABLE_NAME;
        cursor  = db.rawQuery(select, null);

        try{
            if (cursor != null && cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        CartData dta = new CartData(cursor.getString(1),
                                                    cursor.getString(2),
                                                    cursor.getString(3),
                                                    cursor.getBlob(4),
                                                    cursor.getString(5));
                        d.add(dta);
                    } while (cursor.moveToNext());
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("Log",e.toString());
        }

        return d;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        Log.e("ffde", cnt + "");
        db.close();
        return cnt;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public float countProduct(){

        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        float position = 0;

        if (cursor!=null) {
            if (cursor.moveToFirst()) {

                do {
                    position = position + Float.parseFloat(cursor.getString(2)) * Float.parseFloat(cursor.getString(3));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return position;
    }

    public String DeleteData(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        return String.valueOf(db.delete(TABLE_NAME,"ITEMNAME = ?",new String[] {name}));
    }
    public boolean UpdateQty(String itemname,String mqty) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QTY, mqty);
        db.update(TABLE_NAME,values, "ITEMNAME = ?",new String[] {itemname});
        return  true;
    }

}
