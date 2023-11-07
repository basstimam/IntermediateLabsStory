package com.example.dicodingstoryapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dicodingstoryapp.data.local.room.dao.StoriesDAO
import com.example.dicodingstoryapp.data.local.room.entity.StoriesEntity



/*Note: if we want to update app db like adding more column or smthing, should upgrade version
* to avoid conflicts
* */
@Database(entities = [StoriesEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun storiesDAO(): StoriesDAO






    companion object{
        private var instance : AppDatabase? = null


        fun getInstance(context: Context) : AppDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "story_db"
                )

                    .build()
            }
            return instance as AppDatabase
        }

    }


}