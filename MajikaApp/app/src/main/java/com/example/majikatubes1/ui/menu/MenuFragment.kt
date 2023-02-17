package com.example.majikatubes1.ui.menu

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.data.menu.MenuModel
import com.example.majikatubes1.databinding.FragmentMenuBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*

class MenuFragment : Fragment() {

    private var _binding : FragmentMenuBinding? = null
    private var _menuAdapter : MenuAdapter? = null
    private var _menuItemSectionDecoration : MenuItemSectionDecoration? = null

    private val binding get() = _binding!!
    private val menuAdapter get() = _menuAdapter!!
    private val menuItemSectionDecoration get() = _menuItemSectionDecoration!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        _menuAdapter = MenuAdapter()


        val menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerListMenu!!
        val searchView: SearchView = binding.fragmentMenuSearch!!

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = menuAdapter

        menuViewModel.getMenu()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                menuViewModel.menu.observe(viewLifecycleOwner){
                    if (it!=null){
                        val data: List<MenuModel> = it.toList()
                        val filteredData = filterData(data, newText)
                        menuAdapter.setMenuList(filteredData)
                        menuAdapter.notifyDataSetChanged()
                        _menuItemSectionDecoration = MenuItemSectionDecoration(requireContext(),
                            menuAdapter.getMenuList() as MutableList<MenuModel>
                        )
                    }
                }
                return true
            }

        })

        menuViewModel.menu.observe(viewLifecycleOwner){
            if (it!=null){
                val data: List<MenuModel> = it.toList()
                menuAdapter.setMenuList(data)
                menuAdapter.notifyDataSetChanged()
                _menuItemSectionDecoration = MenuItemSectionDecoration(requireContext(),
                    menuAdapter.getMenuList() as MutableList<MenuModel>
                )
            }
        }
        // Inflate the layout for this fragment
        return root
    }

    fun filterData(data: List<MenuModel>, query : String?) : List<MenuModel>{
        val filteredData = ArrayList<MenuModel>()
        if (query != null){
            for (i in data){
               if (i.name.lowercase(Locale.ROOT).contains(query)){
                   filteredData.add(i)
               }
            }
            if (filteredData.isEmpty()){
                Toast.makeText(this.context, "Makanan atau minuman tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                return filteredData.toList()
            }
        }
        return data
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}