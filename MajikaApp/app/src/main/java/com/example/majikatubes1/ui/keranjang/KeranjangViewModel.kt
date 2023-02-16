package com.example.majikatubes1.ui.keranjang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.majikatubes1.data.keranjang.KeranjangModel
import com.example.majikatubes1.data.keranjang.KeranjangRepository

class KeranjangViewModel(private val repository: KeranjangRepository) : ViewModel() {
    var keranjangList: LiveData<List<KeranjangModel>> = MutableLiveData()

    fun getKeranjang() {
        keranjangList = repository.getAllKeranjang()
    }

}