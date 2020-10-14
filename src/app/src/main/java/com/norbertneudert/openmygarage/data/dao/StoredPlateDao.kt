package com.norbertneudert.openmygarage.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.norbertneudert.openmygarage.data.entities.StoredPlate

@Dao
interface StoredPlateDao {
    @Query("SELECT * FROM stored_plate_table")
    fun getStoredPlates(): LiveData<List<StoredPlate>>

    @Query("SELECT * FROM stored_plate_table WHERE plateId = :key")
    fun get(key: Long): StoredPlate?

    @Query("DELETE FROM stored_plate_table")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(storedPlate: StoredPlate)
}