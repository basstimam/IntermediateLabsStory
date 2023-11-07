package com.example.dicodingstoryapp.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stories")
data class StoriesEntity(

    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "storyId") val storyId: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "photoUrl") val photoUrl: String?,
    @ColumnInfo(name = "createdAt") val createdAt: String?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "lon") val lon: Double?

)
