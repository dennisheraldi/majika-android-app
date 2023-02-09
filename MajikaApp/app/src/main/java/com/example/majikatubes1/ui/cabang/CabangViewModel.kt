package com.example.majikatubes1.ui.cabang

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.majikatubes1.data.cabang.CabangModel
import com.example.majikatubes1.data.cabang.CabangRepository

class CabangViewModel : ViewModel() {
    lateinit var cabang: LiveData<List<CabangModel>>

    private var cabangRepository = CabangRepository()

    fun getCabang() {
        val res = cabangRepository.getCabang()
        cabang = res
    }

}