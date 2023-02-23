package com.example.majikatubes1.data.keranjang

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room

class KeranjangRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        KeranjangDatabase::class.java,
        "Keranjang"
    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()

    fun convertToKeranjangEntity(keranjangModel: KeranjangModel ) : KeranjangEntity {
        return KeranjangEntity(
            name = keranjangModel.name,
            price = keranjangModel.price,
            quantity = keranjangModel.quantity
        )
    }

    fun convertToKeranjangModel(keranjangEntity: KeranjangEntity) : KeranjangModel {
        return KeranjangModel(
            name = keranjangEntity.name,
            price = keranjangEntity.price,
            quantity = keranjangEntity.quantity!!
        )
    }

    fun getAllKeranjang() : MutableLiveData<List<KeranjangModel>> {
        val keranjangDao : KeranjangDao = db.keranjangDao()
        val result = MutableLiveData<List<KeranjangModel>>()

        result.value = keranjangDao.getAll().map { keranjangEntity -> convertToKeranjangModel(keranjangEntity) }

        return result
    }

    fun insertKeranjang(keranjangModel: KeranjangModel) {
        val keranjangDao: KeranjangDao = db.keranjangDao()

        keranjangDao.insertKeranjang(convertToKeranjangEntity(keranjangModel))
    }

    fun deleteKeranjang(keranjangModel: KeranjangModel){
        val keranjangDao: KeranjangDao = db.keranjangDao()

        keranjangDao.deleteKeranjang(convertToKeranjangEntity(keranjangModel))
    }

    fun deleteAllKeranjang() {
        val keranjangData = getAllKeranjang().value
        if (keranjangData != null) {
            for (keranjang in keranjangData) {
                deleteKeranjang(keranjang)
            }
        }
    }

    fun updateKeranjang(keranjangModel: KeranjangModel){
        val keranjangDao: KeranjangDao = db.keranjangDao()

        keranjangDao.updateKeranjang(convertToKeranjangEntity(keranjangModel))
    }

    fun getKeranjang(name: String) : KeranjangModel{
        val keranjangDao: KeranjangDao = db.keranjangDao()
        val keranjang : KeranjangEntity = keranjangDao.getKeranjangInfo(name)
        return convertToKeranjangModel(keranjang)
    }

}