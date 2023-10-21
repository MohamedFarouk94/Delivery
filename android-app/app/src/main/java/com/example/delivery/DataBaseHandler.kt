package com.example.delivery

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHandler (context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, "
                + NAME_COL + " TEXT NOT NULL,"
                + SELLER_ID_COL + " INTEGER NOT NULL,"
                + SELLER_USERNAME_COL + " TEXT NOT NULL,"
                + DESCRIPTION_COL + " TEXT,"
                + PRICE_COL + " REAL NOT NULL,"
                + IMAGE_COL + " TEXT,"
                + RATING_COL + " REAL,"
                + NUMBER_OF_RATERS_COL + " INTEGER NOT NULL,"
                + NUMBER_OF_ORDERS_COL + " INTEGER NOT NULL,"
                + NUMBER_OF_BUYOUTS + " INTEGER NOT NULL )")

        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(item: Item){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(ID_COL, item.id)
        values.put(NAME_COL, item.name)
        values.put(SELLER_ID_COL, item.sellerId)
        values.put(SELLER_USERNAME_COL, item.sellerUsername)
        values.put(DESCRIPTION_COL, item.description)
        values.put(PRICE_COL, item.price)
        values.put(IMAGE_COL, item.image)
        values.put(RATING_COL, item.rating)
        values.put(NUMBER_OF_RATERS_COL, item.numberOfRaters)
        values.put(NUMBER_OF_ORDERS_COL, item.numberOfOrders)
        values.put(NUMBER_OF_BUYOUTS, item.numberOfBuyouts)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun removeItem(itemId: Int){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME WHERE $ID_COL = $itemId")
        db.close()
    }

    fun contains(itemId: Int) : Boolean{
        return this.getItems().any { item -> item.id == itemId }
    }

    fun getItems() : MutableList<Item>{
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ", null)
        val items = mutableListOf<Item>()

        if(cursor.moveToFirst()){
            do{
               items.add(
                   Item(
                       id = cursor.getInt(0),
                       name = cursor.getString(1),
                       sellerId = cursor.getInt(2),
                       sellerUsername = cursor.getString(3),
                       description = cursor.getString(4),
                       price = cursor.getDouble(5),
                       image = cursor.getString(6),
                       rating = cursor.getDouble(7),
                       numberOfRaters = cursor.getInt(8),
                       numberOfOrders = cursor.getInt(9),
                       numberOfBuyouts = cursor.getInt(10)
                   )
               )
            }while(cursor.moveToNext())
        }

        cursor.close()
        return items
    }

    companion object{
        private const val DB_NAME = "shortlist"
        private const val DB_VERSION = 1

        private const val TABLE_NAME = "shortlistItem"

        private const val ID_COL = "id"
        private const val NAME_COL = "name"
        private const val SELLER_ID_COL = "sellerId"
        private const val SELLER_USERNAME_COL = "sellerUsername"
        private const val DESCRIPTION_COL = "description"
        private const val PRICE_COL = "price"
        private const val IMAGE_COL = "image"
        private const val RATING_COL = "rating"
        private const val NUMBER_OF_RATERS_COL = "numberOfRaters"
        private const val NUMBER_OF_ORDERS_COL = "numberOfOrders"
        private const val NUMBER_OF_BUYOUTS = "numberOfBuyouts"
    }
}
