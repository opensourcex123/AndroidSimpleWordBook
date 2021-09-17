package com.example.kotlinwordbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(val wordList: List<Word>): RecyclerView.Adapter<WordAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var word = view.findViewById<TextView>(R.id.word)
        var meaning = view.findViewById<TextView>(R.id.meaning)
        var sample = view.findViewById<TextView>(R.id.sample)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {

        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val words = wordList[position]
        holder.word.text = words.word
        holder.meaning.text = words.meaning
        holder.sample.text = words.sample
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

}