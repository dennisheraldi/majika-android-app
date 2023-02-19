package com.example.majikatubes1.ui.pembayaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.majikatubes1.data.pembayaran.PembayaranModel
import com.example.majikatubes1.data.pembayaran.PembayaranRepository

class PembayaranViewModel : ViewModel() {
    var statusPembayaran: LiveData<PembayaranModel> = MutableLiveData()

    private var pembayaranRepository = PembayaranRepository()

    fun getStatusPembayaran(transactionId: String) {
        val res = pembayaranRepository.getStatusPembayaran(transactionId)
        statusPembayaran = res
    }
}