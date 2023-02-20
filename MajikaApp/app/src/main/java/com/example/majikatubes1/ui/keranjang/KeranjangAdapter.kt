package com.example.majikatubes1.ui.keranjang

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.data.keranjang.KeranjangModel
import com.example.majikatubes1.data.keranjang.KeranjangRepository
import com.example.majikatubes1.utils.NumSperator
import org.w3c.dom.Text

class KeranjangAdapter(private val cartupdatecallback: cartUpdateCallback): RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder>() {
    private var keranjangList: List<KeranjangModel>? = null
    private var context: Context? = null
    private var keranjangRepository: KeranjangRepository? = null

    interface cartUpdateCallback{
        fun updateTotalTextView()
    }

    class KeranjangViewHolder(keranjangItem : View): RecyclerView.ViewHolder(keranjangItem){
        val keranjangItem = keranjangItem.findViewById<CardView>(R.id.item_keranjang)
        val keranjangNama = keranjangItem.findViewById<TextView>(R.id.item_keranjang_nama)
        val keranjangHarga = keranjangItem.findViewById<TextView>(R.id.item_keranjang_harga)
        val keranjangPlus = keranjangItem.findViewById<Button>(R.id.item_keranjang_button_plus)
        val keranjangCounter = keranjangItem.findViewById<TextView>(R.id.item_keranjang_counter)
        val keranjangMinus = keranjangItem.findViewById<Button>(R.id.item_keranjang_button_minus)
    }

    fun setKeranjangList(data: List<KeranjangModel>?){
        if (data != null) {
            keranjangList = data.sortedBy { it.name.toString() }
        }
    }

    fun getKeranjangList(): List<KeranjangModel>? {
        return keranjangList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        context = parent.context
        keranjangRepository = KeranjangRepository(context!!)
        return KeranjangViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return keranjangList?.size ?: 0
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: KeranjangViewHolder, position: Int) {
        val keranjangData: KeranjangModel = keranjangList!![position]
        val numSperator = NumSperator(keranjangData.price)

        holder.keranjangNama.text = keranjangData.name
        holder.keranjangHarga.text = numSperator.parse()
        holder.keranjangCounter.text = keranjangData.quantity.toString()

        holder.keranjangCounter.text = keranjangData.quantity.toString()

        holder.keranjangPlus.setOnClickListener{
            keranjangData.quantity = keranjangData.quantity.plus(1)
            holder.keranjangCounter.text = keranjangData.quantity.toString()
            keranjangRepository?.updateKeranjang(keranjangData)
            setKeranjangList(keranjangRepository?.getAllKeranjang()?.value)
            cartupdatecallback.updateTotalTextView()
            notifyDataSetChanged()
        }

        holder.keranjangMinus.setOnClickListener{
            keranjangData.quantity = keranjangData.quantity.minus(1)
            if (keranjangData.quantity == 0){
                keranjangRepository?.deleteKeranjang(keranjangData)
                setKeranjangList(keranjangRepository?.getAllKeranjang()?.value)
                cartupdatecallback.updateTotalTextView()
            } else {
                holder.keranjangCounter.text = keranjangData.quantity.toString()
                keranjangRepository?.updateKeranjang(keranjangData)
                cartupdatecallback.updateTotalTextView()
            }
            notifyDataSetChanged()
        }
        setKeranjangList(keranjangRepository?.getAllKeranjang()?.value)
    }


}

