package com.belluk.movapps.mapping

import android.database.Cursor
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.BIOSKOP
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.CINEMA
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.DATE
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.GENRE
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.ID
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.KODE
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.NAME
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.RATING
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.TIMER
import com.belluk.movapps.db.DatabaseContract.MovieColumns.Companion.USER
import com.belluk.movapps.model.History
import com.belluk.movapps.model.Tiket

object MappingHelper {
    fun mapCursorToArrayList(movieCursor: Cursor):ArrayList<History>{
        val movieList = ArrayList<History>()
        while (movieCursor.moveToNext()){
            val id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(ID))
            val kode = movieCursor.getString(movieCursor.getColumnIndexOrThrow(KODE))
            val bioskop = movieCursor.getString(movieCursor.getColumnIndexOrThrow(BIOSKOP))
            val cinema = movieCursor.getString(movieCursor.getColumnIndexOrThrow(CINEMA))
            val date = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DATE))
            val genre = movieCursor.getString(movieCursor.getColumnIndexOrThrow(GENRE))
            val name = movieCursor.getString(movieCursor.getColumnIndexOrThrow(NAME))
            val poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER))
            val rating = movieCursor.getString(movieCursor.getColumnIndexOrThrow(RATING))
            val timer = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TIMER))
            val user = movieCursor.getString(movieCursor.getColumnIndexOrThrow(USER))
            movieList.add(History(id,name,bioskop,genre, user, date, poster, rating, timer,kode,cinema))
        }
        return movieList
    }
}