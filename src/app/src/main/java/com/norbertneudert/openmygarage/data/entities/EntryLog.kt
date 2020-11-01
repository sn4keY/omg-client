package com.norbertneudert.openmygarage.data.entities

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(logId)
        parcel.writeLong(entryTime)
        parcel.writeString(plate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntryLog> {
        override fun createFromParcel(parcel: Parcel): EntryLog {
            return EntryLog(parcel)
        }

        override fun newArray(size: Int): Array<EntryLog?> {
            return arrayOfNulls(size)
        }
    }
}