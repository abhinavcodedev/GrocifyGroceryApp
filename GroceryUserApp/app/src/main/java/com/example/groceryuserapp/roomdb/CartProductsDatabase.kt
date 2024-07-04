package com.example.groceryuserapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CartProductTable::class], version = 1, exportSchema = false)

abstract class CartProductsDatabase: RoomDatabase() {

    abstract fun cartProductsDao() : CartProductDao

    companion object{

        @Volatile
        var INSTANCE : CartProductsDatabase?= null

        fun getDatabaseInstance(context: Context) : CartProductsDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this){
                val roomdb = Room.databaseBuilder(context, CartProductsDatabase::class.java, "CartProductsDatabase").allowMainThreadQueries().build()
                INSTANCE= roomdb
                return roomdb
            }
        }
    }
}