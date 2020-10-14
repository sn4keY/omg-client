package com.norbertneudert.openmygarage.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stored_plate_table")
data class StoredPlate(
    @PrimaryKey(autoGenerate = true)
    var plateId: Long = 0L,

    @ColumnInfo(name = "plate")
    var plate: String = "",

    @ColumnInfo(name = "nationality")
    var nationality: String = "",

    @ColumnInfo(name = "car_manufacturer")
    var carManufacturer: String = "",

    @ColumnInfo(name = "auto_open")
    var autoOpen: Boolean = false
)