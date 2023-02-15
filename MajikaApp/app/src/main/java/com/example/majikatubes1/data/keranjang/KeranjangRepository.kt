package com.example.majikatubes1.data.keranjang

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.majikatubes1.data.menu.MenuModel

class KeranjangRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        KeranjangDatabase::class.java,
        "Keranjang"
    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()

    fun convertMenuToKeranjang(menuModel: MenuModel ) : KeranjangEntity {
        return KeranjangEntity(
            name = menuModel.name,
            description = menuModel.description,
            currency = menuModel.currency,
            price = menuModel.price,
            sold = menuModel.sold,
            type = menuModel.type
        )
    }

    fun convertKeranjangToMenu(keranjangEntity: KeranjangEntity) : MenuModel {
        return MenuModel(
            name = keranjangEntity.name.toString(),
            description = keranjangEntity.description.toString(),
            currency = keranjangEntity.currency.toString(),
            price = keranjangEntity.price,
            sold = keranjangEntity.sold,
            type = keranjangEntity.type
        )
    }

    fun getAllKeranjang() : MutableLiveData<List<MenuModel>> {
        val keranjangDao : KeranjangDao = db.keranjangDao()
        val result = MutableLiveData<List<MenuModel>>()

        result.value = keranjangDao.getAll().map { keranjangEntity -> convertKeranjangToMenu(keranjangEntity) }

        return result
    }

    fun insertKeranjang(menuModel : MenuModel) {
        val keranjangDao: KeranjangDao = db.keranjangDao()

        keranjangDao.insertKeranjang(convertMenuToKeranjang(menuModel))
    }

    fun deleteKeranjang(menuModel : MenuModel){
        val keranjangDao: KeranjangDao = db.keranjangDao()

        keranjangDao.deleteKeranjang(convertMenuToKeranjang(menuModel))
    }

    fun updateKeranjang(menuModel : MenuModel){
        val keranjangDao: KeranjangDao = db.keranjangDao()

        keranjangDao.updateKeranjang(convertMenuToKeranjang(menuModel))
    }


}