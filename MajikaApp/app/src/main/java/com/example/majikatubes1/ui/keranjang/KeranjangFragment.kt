package com.example.majikatubes1.ui.keranjang

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.data.keranjang.KeranjangRepository
import com.example.majikatubes1.databinding.FragmentKeranjangBinding


class KeranjangFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentKeranjangBinding? = null
    private var _keranjangAdapter: KeranjangAdapter? = null

    private val binding get() = _binding!!
    private val keranjangAdapter get() = _keranjangAdapter!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        _keranjangAdapter = KeranjangAdapter()

        val keranjangViewModel = KeranjangViewModel(KeranjangRepository(requireContext()))
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.recyclerListKeranjang
        val totalBayar : TextView = binding.keranjangTotalPrice

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = keranjangAdapter

        keranjangViewModel.getKeranjang()
        keranjangViewModel.keranjangList.observe(viewLifecycleOwner) {
            if (it != null){
                keranjangAdapter.setKeranjangList(it.toList())
                keranjangAdapter.notifyDataSetChanged()
            }
        }
        // Inflate the layout for this fragment
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}