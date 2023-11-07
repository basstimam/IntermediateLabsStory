package com.example.dicodingstoryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import com.example.dicodingstoryapp.data.local.room.entity.StoriesEntity


@Dao
interface StoriesDAO {

    @Insert
    fun insertStories(
        stories: List<StoriesEntity>
    )

    @Query("SELECT * FROM stories")
    fun getStories(): List<StoriesEntity>

}