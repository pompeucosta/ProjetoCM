package com.example.projetocm.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HistorySessions")
data class HistorySession(
    val location: String,
    val day: Int,
    val month: Int,
    val year: Int,
    @Embedded
    val sessionInfo: SessionInfo,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
