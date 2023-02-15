package com.example.majikatubes1.data.keranjang

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class KeranjangEntity (
    @PrimaryKey val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "sold") val sold: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name= " quantity") val quantity: Int? = 0
    )