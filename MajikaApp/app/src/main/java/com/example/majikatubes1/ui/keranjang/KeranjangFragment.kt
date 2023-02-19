package com.example.majikatubes1.ui.keranjang

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.data.keranjang.KeranjangModel
import com.example.majikatubes1.data.keranjang.KeranjangRepository
import com.example.majikatubes1.data.pembayaran.PembayaranStatus
import com.example.majikatubes1.databinding.FragmentKeranjangBinding
import com.example.majikatubes1.ui.pembayaran.PembayaranActivity
import java.util.*


class KeranjangFragment : Fragment(), KeranjangAdapter.cartUpdateCallback {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentKeranjangBinding? = null
    private var _keranjangAdapter: KeranjangAdapter? = null

    private val binding get() = _binding!!
    private val keranjangAdapter get() = _keranjangAdapter!!


    override fun updateTotalTextView(){
        val totalBayar : TextView = binding.keranjangTotalPrice
        totalBayar.text = keranjangAdapter.getKeranjangList()?.let {
            calculateTotalPrice(it)
        }.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        _keranjangAdapter = KeranjangAdapter(object : KeranjangAdapter.cartUpdateCallback{
            @SuppressLint("SetTextI18n")
            override fun updateTotalTextView() {
                val totalBayar : TextView = binding.keranjangTotalPrice
                totalBayar.text = "Rp${
                    keranjangAdapter.getKeranjangList()?.let {
                        calculateTotalPrice(it)
                    }.toString()
                }".format(Locale.GERMAN)

            }
        })

        val keranjangViewModel = KeranjangViewModel(KeranjangRepository(requireContext()))
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.recyclerListKeranjang
        val totalBayar : TextView = binding.keranjangTotalPrice
        val buttonBayar : Button = binding.keranjangButtonBayar

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = keranjangAdapter

        keranjangViewModel.getKeranjang()
        keranjangViewModel.keranjangList.observe(viewLifecycleOwner) {
            var total : Int = 0

            if (it != null){
                keranjangAdapter.setKeranjangList(it.toList())
                keranjangAdapter.notifyDataSetChanged()
                total           = calculateTotalPrice(it)
                totalBayar.text = "Rp$total"
            }

            buttonBayar.setOnClickListener {
                if (total > 0) {
                    val pembayaranActivityIntent = Intent(activity, PembayaranActivity::class.java)
                    pembayaranActivityIntent.putExtra("totalBayar", totalBayar.text)
                    activity?.startActivity(pembayaranActivityIntent)
                } else {
                    Toast.makeText(requireContext(),
                        "Keranjang is empty.",
                        Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }



        // Inflate the layout for this fragment
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun calculateTotalPrice(keranjangList : List<KeranjangModel>): Int {
        var total : Int = 0
        for (i in keranjangList){
            val subTotal = i.price * i.quantity
            total = total.plus(subTotal)
        }
        return total
    }
}