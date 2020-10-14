package com.norbertneudert.openmygarage.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry_log_table")
data class EntryLog (
    @PrimaryKey(autoGenerate = true)
    var logId: Long = 0L,

    @ColumnInfo(name = "entry_time")
    val entryTime: Long = Long.MIN_VALUE,

    @ColumnInfo(name = "plate")
    var plate: String = ""
)