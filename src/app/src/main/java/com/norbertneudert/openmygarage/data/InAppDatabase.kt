package com.norbertneudert.openmygarage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.norbertneudert.openmygarage.data.dao.EntryLogDao
import com.norbertneudert.openmygarage.data.dao.StoredPlateDao
import com.norbertneudert.openmygarage.data.entities.EntryLog
import com.norbertneudert.openmygarage.data.entities.StoredPlate

@Database(entities = [EntryLog::class, StoredPlate::class], version = 1, exportSchema = false)
abstract class InAppDatabase : RoomDatabase() {
    abstract val entryLogDao: EntryLogDao
    abstract val storedPlateDao: StoredPlateDao

    companion object {
        @Volatile
        private var INSTANCE: InAppDatabase? = null

        fun getInstance(context: Context) : InAppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InAppDatabase::class.java,
                        "in_app_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}