package com.example.majikatubes1.data.keranjang

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KeranjangEntity::class], version = 1)
abstract class KeranjangDatabase : RoomDatabase() {
    abstract fun keranjangDao() : KeranjangDao
}