package com.example.majikatubes1.data.keranjang

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface KeranjangDao {
    @Query("SELECT * from KeranjangEntity")
    fun getAll(): List<KeranjangEntity>

    @Query("SELECT * from KeranjangEntity WHERE name = :name")
    fun getKeranjangInfo(name: String): KeranjangEntity

    @Insert
    fun insertKeranjang(vararg keranjang: KeranjangEntity)

    @Delete
    fun deleteKeranjang(keranjang: KeranjangEntity)

    @Update
    fun updateKeranjang(vararg keranjang: KeranjangEntity)
}