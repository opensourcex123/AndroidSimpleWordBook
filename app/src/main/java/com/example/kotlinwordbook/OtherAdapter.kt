package com.example.kotlinwordbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OtherAdapter(val otherList: List<String>): RecyclerView.Adapter<OtherAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var other = view.findViewById<TextView>(R.id.other)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.other_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val other = otherList[position]
        holder.other.text = other
    }

    override fun getItemCount(): Int {
        return otherList.size
    }

}