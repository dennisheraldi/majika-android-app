package com.example.majikatubes1.ui.cabang

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.data.cabang.CabangModel


class CabangAdapter : RecyclerView.Adapter<CabangAdapter.CabangViewHolder>() {
    private var cabangList: List<CabangModel>? = null

    inner class CabangViewHolder(cabangItem : View) : RecyclerView.ViewHolder(cabangItem) {
        val cabangItem = cabangItem.findViewById<CardView>(R.id.item_cabang)
        val cabangNama = cabangItem.findViewById<TextView>(R.id.item_cabang_nama)
        val cabangAlamat = cabangItem.findViewById<TextView>(R.id.item_cabang_alamat)
        val cabangTelp = cabangItem.findViewById<TextView>(R.id.item_cabang_telp)
        val cabangMaps = cabangItem.findViewById<Button>(R.id.item_cabang_maps)
    }

    fun setCabangList(data: List<CabangModel>){
        cabangList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CabangViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_cabang, parent, false)

        return CabangViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CabangViewHolder, position: Int) {
        val cabangData: CabangModel = cabangList!![position]

        holder.cabangNama.text = cabangData.name
        holder.cabangAlamat.text = cabangData.address
        holder.cabangTelp.text = cabangData.phone_number

        holder.cabangMaps.setOnClickListener{
            TODO("Add intent implementation for google maps")
//
        }
    }

    override fun getItemCount(): Int {
        return cabangList?.size ?: 0
    }


}