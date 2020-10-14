package com.norbertneudert.openmygarage.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.norbertneudert.openmygarage.data.entities.EntryLog

@Dao
interface EntryLogDao {
    @Query("SELECT * FROM entry_log_table ORDER BY entry_time DESC")
    fun getEntryLogs(): LiveData<List<EntryLog>>

    @Query("SELECT * FROM entry_log_table ORDER BY entry_time DESC")
    fun getEntryLogsList(): List<EntryLog>

    @Query("SELECT * FROM entry_log_table WHERE :dateFrom < entry_time < :dateUntil ORDER BY entry_time DESC")
    fun getEntryLogsBetween(dateFrom: Long, dateUntil: Long): LiveData<List<EntryLog>>

    @Query("SELECT * FROM entry_log_table ORDER BY entry_time DESC LIMIT 5")
    fun getEntryLogsLimited(): LiveData<List<EntryLog>>

    @Query("SELECT * FROM entry_log_table WHERE :plate == plate ORDER BY entry_time DESC")
    fun getEntryLogsFromPlate(plate: String) : LiveData<List<EntryLog>>

    @Query("SELECT * FROM entry_log_table ORDER BY entry_time DESC LIMIT 1")
    fun getLastEntryLog() : EntryLog?

    @Insert
    fun insert(entryLog: EntryLog)

    @Query("DELETE FROM entry_log_table")
    fun clear()
}