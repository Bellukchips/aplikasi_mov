package com.belluk.movapps.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.BIOSKOP
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.CINEMA
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.DATE
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.GENRE
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.ID
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.KODE
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.NAME
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.RATING
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.TABLE_NAME
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.TIMER
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.USER
import com.belluk.movapps.model.History

class MovieHelper(context: Context) {
    private val databaseHelper:DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABSE_NAME = TABLE_NAME
        private var INSTANCE:MovieHelper?=null
        fun getInstance(context: Context):MovieHelper{
            if (INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if (INSTANCE == null){
                        INSTANCE = MovieHelper(context)
                    }
                }
            }
            return INSTANCE as MovieHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    //crud
    fun queryAll():Cursor{
        return database.query(
            DATABSE_NAME,
            null,
            null,
            null,
            KODE,
            null,
            "$ID ASC"

        )
    }
    fun queryById(id:String):Cursor{
        return database.query(
            DATABSE_NAME,
            null,
            "$ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }
    fun insert(history: History): Long {
        val values = ContentValues()
        values.put(NAME, history.nama)
        values.put(ID, history.id)
        values.put(BIOSKOP, history.bioskop)
        values.put(GENRE, history.genre)
        values.put(USER, history.user)
        values.put(DATE, history.date)
        values.put(POSTER, history.poster)
        values.put(RATING, history.rating)
        values.put(TIMER, history.timer)
        values.put(KODE, history.kodeTiket)
        values.put(CINEMA, history.cinema)
        return database.insert(DATABSE_NAME,null,values)
    }

    fun insertData(content:ContentValues?):Long{
        return database.insert(DATABSE_NAME,null,content)
    }
    fun update(id:String,values: ContentValues?):Int{
        return database.update(DATABSE_NAME,values,"$ID = '$id'",null)
    }
    fun deleteById(id:String):Int{
        return database.delete(DATABSE_NAME,"$ID = '$id'",null)
    }

}