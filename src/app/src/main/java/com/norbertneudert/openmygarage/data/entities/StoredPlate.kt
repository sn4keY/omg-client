package com.norbertneudert.openmygarage.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "stored_plate_table", indices = [Index(value = ["plate"], unique = true)])
data class StoredPlate(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0L,

    @ColumnInfo(name = "plate")
    var plate: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "nationality")
    var nationality: String = "",

    @ColumnInfo(name = "car_manufacturer")
    var carManufacturer: String = "",

    @ColumnInfo(name = "auto_open")
    var autoOpen: Boolean = false
)