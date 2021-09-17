package com.example.kotlinwordbook

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDataBase(val context: Context, name: String, version: Int) :
SQLiteOpenHelper(context, name, null, version){
    private val createWord = "create table Word (" +
    " id integer primary key autoincrement," +
    "word text," +
    "meaning text," +
    "sample text)"
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createWord)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists Word")
        onCreate(p0)
    }

}