package com.example.majikatubes1.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.majikatubes1.data.menu.MenuModel
import com.example.majikatubes1.data.menu.MenuRepository

class MenuViewModel : ViewModel() {
    lateinit var menu: LiveData<List<MenuModel>>

    private var menuRepository = MenuRepository()

    fun getMenu() {
        val res = menuRepository.getMenu()
        menu = res
    }
}