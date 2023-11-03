package com.example.projetocm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RunPreset::class,HistorySession::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(dbTypeConverters::class)
abstract class ProjetoDatabase: RoomDatabase() {
    abstract fun runPresetDao(): RunPresetDao
    abstract fun historySessionDao(): HistorySessionDao

    companion object {
        @Volatile
        private var Instance: ProjetoDatabase? = null

        fun getDatabase(context: Context): ProjetoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,ProjetoDatabase::class.java,"projeto_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{ Instance = it}
            }
        }
    }
}