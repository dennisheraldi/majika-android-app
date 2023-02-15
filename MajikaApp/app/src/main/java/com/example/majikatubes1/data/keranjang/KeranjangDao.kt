package com.example.majikatubes1.data.keranjang

import androidx.room.*

@Dao
interface KeranjangDao {
    @Query("SELECT * from keranjang")
    fun getAll(): List<KeranjangEntity>

    @Insert
    fun insertKeranjang(vararg keranjang_all: KeranjangEntity)

    @Delete
    fun deleteKeranjang(keranjang: KeranjangEntity)

    @Update
    fun updateKeranjang(vararg keranjang_all: KeranjangEntity)
}