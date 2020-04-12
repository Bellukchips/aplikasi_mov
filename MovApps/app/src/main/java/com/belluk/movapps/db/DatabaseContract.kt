package com.belluk.movapps.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class MovieColumns:BaseColumns{
        companion object{
            const val TABLE_NAME = "movie"
            const val ID = "id"
            const val KODE = "kodeTiket"
            const val NAME = "nama"
            const val USER = "user"
            const val BIOSKOP = "bioskop"
            const val CINEMA = "cinema"
            const val DATE = "date"
            const val GENRE = "genre"
            const val POSTER = "poster"
            const val RATING = "rating"
            const val TIMER = "timer"
        }
    }
}