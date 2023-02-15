package com.example.majikatubes1.ui.menu

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import com.example.majikatubes1.R

class MenuSectionViewHolder(context: Context) : FrameLayout(context){
    private lateinit var textView: TextView

    init {
        inflate(context, R.layout.item_menu_section, this)
        findView()
    }

    private fun findView(){
        textView = findViewById(R.id.item_menu_section)
    }

    fun setSection(section: String) {
        textView.text = section
    }


}