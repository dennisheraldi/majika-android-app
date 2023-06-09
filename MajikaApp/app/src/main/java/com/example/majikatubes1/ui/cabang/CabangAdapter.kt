package com.example.majikatubes1.ui.cabang

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
    private var context: Context? =  null

    inner class CabangViewHolder(cabangItem : View) : RecyclerView.ViewHolder(cabangItem) {
        val cabangItem = cabangItem.findViewById<CardView>(R.id.item_cabang)
        val cabangNama = cabangItem.findViewById<TextView>(R.id.item_cabang_nama)
        val cabangAlamat = cabangItem.findViewById<TextView>(R.id.item_cabang_alamat)
        val cabangTelp = cabangItem.findViewById<TextView>(R.id.item_cabang_telp)
        val cabangMaps = cabangItem.findViewById<Button>(R.id.item_cabang_maps)
    }

    fun setCabangList(data: List<CabangModel>){
        cabangList = data.sortedBy { it.name.toString() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CabangViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_cabang, parent, false)
        context = parent.context;

        return CabangViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CabangViewHolder, position: Int) {
        val cabangData: CabangModel = cabangList!![position]

        holder.cabangNama.text = cabangData.name
        holder.cabangAlamat.text = cabangData.address
        holder.cabangTelp.text = cabangData.phone_number

        holder.cabangMaps.setOnClickListener {
            val gmmIntentUri = Uri.parse("https://maps.google.com/?q=${cabangData.latitude},${cabangData.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            val bundle = Bundle()
            startActivity(context!!, mapIntent, bundle)
        }

    }

    override fun getItemCount(): Int {
        return cabangList?.size ?: 0
    }


}