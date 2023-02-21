package com.example.majikatubes1.ui.cabang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.databinding.FragmentCabangBinding


class CabangFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentCabangBinding? = null
    private var _cabangAdapter: CabangAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val cabangAdapter get() = _cabangAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCabangBinding.inflate(inflater, container, false)
        _cabangAdapter = CabangAdapter()

        val cabangViewModel = ViewModelProvider(this)[CabangViewModel::class.java]
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.recyclerListCabang
        val cabangEmpty: TextView = binding.cabangEmpty

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = cabangAdapter

        cabangViewModel.getCabang()
        cabangViewModel.cabang?.observe(viewLifecycleOwner) {
            if (it != null){
                if (it.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    cabangEmpty.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.GONE
                    cabangEmpty.visibility = View.VISIBLE

                    Toast.makeText(requireContext(),
                        "Tidak ada data cabang.",
                        Toast.LENGTH_SHORT).show()
                }

                cabangAdapter.setCabangList(it.toList())
                cabangAdapter.notifyDataSetChanged()
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