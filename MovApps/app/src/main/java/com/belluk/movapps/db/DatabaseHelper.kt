package com.belluk.movapps.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context:Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("Drop TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    companion object{
        private const val DATABASE_NAME = "dbmovie"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $TABLE_NAME"+
                "(${DatabaseContract.MovieColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "${DatabaseContract.MovieColumns.KODE} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.BIOSKOP} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.CINEMA} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.DATE} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.GENRE} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.NAME} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.POSTER} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.RATING} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.TIMER} TEXT NOT NULL,"+
                "${DatabaseContract.MovieColumns.USER} TEXT NOT NULL)"

    }

}