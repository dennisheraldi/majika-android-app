package com.example.majikatubes1.ui.menu

import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.data.keranjang.KeranjangEntity
import com.example.majikatubes1.data.keranjang.KeranjangModel
import com.example.majikatubes1.data.keranjang.KeranjangRepository
import com.example.majikatubes1.data.menu.MenuModel
import org.w3c.dom.Text
import java.util.*

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    private var menuList: List<MenuModel>? = null
    private var context: Context? = null
    private var keranjangRepository: KeranjangRepository? = null

    inner class MenuViewHolder(menuItem: View): RecyclerView.ViewHolder(menuItem){
        val menuNama = menuItem.findViewById<TextView>(R.id.item_menu_nama)
        val menuDeskripsi = menuItem.findViewById<TextView>(R.id.item_menu_deskripsi)
        val menuHarga = menuItem.findViewById<TextView>(R.id.item_menu_harga)
        val menuTerjual = menuItem.findViewById<TextView>(R.id.item_menu_terjual)
        val menuTambah = menuItem.findViewById<Button>(R.id.item_menu_tambah)
        val menuPlus = menuItem.findViewById<Button>(R.id.item_menu_button_plus)
        val menuCounter = menuItem.findViewById<TextView>(R.id.item_menu_counter)
        val menuMinus = menuItem.findViewById<Button>(R.id.item_menu_button_minus)
        val menuPlusMinusLayout = menuItem.findViewById<ConstraintLayout>(R.id.item_menu_plus_minus)
    }

    fun setMenuList(data: List<MenuModel>){
        menuList = data.sortedBy { it.name.toString() }
    }

    fun getContext(): Context{
        return context!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        context = parent.context
        keranjangRepository = KeranjangRepository(context!!)

        return MenuViewHolder(layout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuData: MenuModel = menuList!![position]

        holder.menuNama.text = menuData.name
        holder.menuHarga.text = "Rp${menuData.price}"
        holder.menuTerjual.text = "Terjual ${menuData.sold}"
        holder.menuDeskripsi.text = menuData.description

        var keranjangData : KeranjangModel? = keranjangRepository?.getAllKeranjang()?.value?.find { it.name == menuData.name }

        keranjangData?.let {
            holder.menuTambah.visibility = GONE
            holder.menuPlusMinusLayout.visibility = VISIBLE

            holder.menuCounter.text = keranjangData!!.quantity.toString()

            holder.menuPlus.setOnClickListener {
                keranjangData = keranjangRepository?.getAllKeranjang()?.value?.find { it.name == menuData.name }
                keranjangData?.quantity = keranjangData?.quantity?.plus(1)!!
                holder.menuCounter.text = keranjangData!!.quantity.toString()
                keranjangRepository?.updateKeranjang(keranjangData!!)
                notifyDataSetChanged()
            }

            holder.menuMinus.setOnClickListener {
                keranjangData  = keranjangRepository?.getAllKeranjang()?.value?.find { it.name == menuData.name }
                keranjangData?.quantity = keranjangData?.quantity?.minus(1)!!
                if (keranjangData?.quantity == 0){
                    keranjangRepository?.deleteKeranjang(keranjangData!!)
                    holder.menuTambah.visibility = VISIBLE
                    holder.menuPlusMinusLayout.visibility = GONE
                } else {
                    holder.menuCounter.text = keranjangData!!.quantity.toString()
                    keranjangRepository?.updateKeranjang(keranjangData!!)
                }
                notifyDataSetChanged()
            }

        } ?: run{
            holder.menuTambah.visibility = VISIBLE
            holder.menuPlusMinusLayout.visibility = GONE

            holder.menuTambah.setOnClickListener {
                keranjangRepository?.insertKeranjang(KeranjangModel(menuData.name, menuData.price, 1))
                holder.menuTambah.visibility = GONE
                holder.menuPlusMinusLayout.visibility = VISIBLE
                notifyDataSetChanged();
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList?.size ?: 0
    }
}