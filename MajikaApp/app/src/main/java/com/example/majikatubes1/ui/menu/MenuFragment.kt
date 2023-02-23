package com.example.majikatubes1.ui.menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majikatubes1.R
import com.example.majikatubes1.data.menu.MenuModel
import com.example.majikatubes1.databinding.FragmentMenuBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*

class MenuFragment : Fragment(), SensorEventListener {

    private var _binding : FragmentMenuBinding? = null
    private var _menuAdapter : MenuAdapter? = null
    private var _menuItemSectionDecoration : MenuItemSectionDecoration? = null
    private lateinit var sensorManager: SensorManager
    private var temperature: Float = 0.0f
    private var tempSensor: Sensor? = null

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
        _menuItemSectionDecoration = MenuItemSectionDecoration(requireContext())
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        tempSensor ?: run {
            // If the device has no temp sensor
            // Toast.makeText(this.context, "Sensor suhu saat ini tidak tersedia", Toast.LENGTH_SHORT).show()
        }

        val menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerListMenu!!
        val searchView: SearchView = binding.fragmentMenuSearch!!
        val menuEmpty: TextView = binding.menuEmpty!!

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = menuAdapter
        recyclerView.addItemDecoration(menuItemSectionDecoration)

        menuViewModel.getMenu()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                menuViewModel.menu.observe(viewLifecycleOwner){
                    if (it!=null){
                        var data: List<MenuModel> = it.toList()
                        data = sortData(data)
                        val filteredData = filterData(data, newText)
                        menuAdapter.setMenuList(filteredData)
                        menuAdapter.notifyDataSetChanged()
                        menuItemSectionDecoration.setItemList(filteredData as MutableList<MenuModel>)
                    }
                }
                return true
            }

        })

        menuViewModel.menu.observe(viewLifecycleOwner){
            if (it != null){
                if (it.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    menuEmpty.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.GONE
                    menuEmpty.visibility = View.VISIBLE

                    Toast.makeText(requireContext(),
                        "Tidak ada data menu.",
                        Toast.LENGTH_SHORT).show()
                }

                var data: List<MenuModel> = it.toList()
                data = sortData(data)
                menuAdapter.setMenuList(data)
                menuAdapter.notifyDataSetChanged()
                menuItemSectionDecoration.setItemList(data as MutableList<MenuModel>)
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

    fun sortData(data: List<MenuModel>): List<MenuModel>{
        return data.sortedWith(compareByDescending<MenuModel> {it.type}.thenBy{it.name.toString()})
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        binding.fragmentMenuSuhu.text = "Suhu saat ini ${event?.values?.get(0)} °C"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}