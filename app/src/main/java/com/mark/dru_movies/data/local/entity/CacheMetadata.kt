package com.mark.dru_movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_metadata")
data class CacheMetadata(
    @PrimaryKey val id: Int = 0,
    val lastFetchTime: Long
)