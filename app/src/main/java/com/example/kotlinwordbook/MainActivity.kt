package com.example.kotlinwordbook

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val wordList = ArrayList<Word>()
    private val otherList = ArrayList<String>()
    private var adapter: WordAdapter? = null
    private var adapter_other: OtherAdapter? = null
    private val dbHelper = MyDataBase(this, "WordBook.db", 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        val layoutManager_land = LinearLayoutManager(this)
        val wordRecyclerView = findViewById<RecyclerView>(R.id.wordRecyclerView)
        val otherRecyclerView = findViewById<RecyclerView>(R.id.otherRecyclerView)
        if (otherRecyclerView != null) {
            otherRecyclerView.layoutManager = layoutManager_land
            adapter_other = OtherAdapter(otherList)
            otherRecyclerView.adapter = adapter_other
        }
        wordRecyclerView.layoutManager = layoutManager
        adapter = WordAdapter(wordList)
        wordRecyclerView.adapter = adapter
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE )
        {
            setContentView(R.layout.activity_main)
            val layoutManager = LinearLayoutManager(this)
            val layoutManager_land = LinearLayoutManager(this)
            val wordRecyclerView = findViewById<RecyclerView>(R.id.wordRecyclerView)
            val otherRecyclerView = findViewById<RecyclerView>(R.id.otherRecyclerView)
            if (otherRecyclerView != null) {
                otherRecyclerView.layoutManager = layoutManager_land
                adapter_other = OtherAdapter(otherList)
                otherRecyclerView.adapter = adapter_other
            }
            wordRecyclerView.layoutManager = layoutManager
            adapter = WordAdapter(wordList)
            wordRecyclerView.adapter = adapter
        }
        else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            setContentView(R.layout.activity_main)
            val layoutManager = LinearLayoutManager(this)
            val layoutManager_land = LinearLayoutManager(this)
            val wordRecyclerView = findViewById<RecyclerView>(R.id.wordRecyclerView)
            val otherRecyclerView = findViewById<RecyclerView>(R.id.otherRecyclerView)
            if (otherRecyclerView != null) {
                otherRecyclerView.layoutManager = layoutManager_land
                adapter_other = OtherAdapter(otherList)
                otherRecyclerView.adapter = adapter_other
            }
            wordRecyclerView.layoutManager = layoutManager
            adapter = WordAdapter(wordList)
            wordRecyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help -> {
                Toast.makeText(this, "这是帮助", Toast.LENGTH_SHORT).show()
            }
            R.id.addWord -> {
                InsertDialog()
            }
            R.id.deleteWord -> {
                DeleteDialog()
            }
            R.id.updateWord -> {
                UpdateDialog()
            }
            R.id.searchWord -> {
                SearchDialog()
            }
            R.id.exit -> {
                finish()
            }
        }
        return true
    }

    private fun InsertDialog() {
        val tableLayout = layoutInflater.inflate(R.layout.insert, null) as TableLayout
        AlertDialog.Builder(this)
            .setTitle("新增单词")
            .setView(tableLayout)
            .setPositiveButton("确定") { dialogInterface, i ->
                val strWord =
                    (tableLayout.findViewById<View>(R.id.txtWord) as EditText).text.toString()
                val strMeaning =
                    (tableLayout.findViewById<View>(R.id.txtMeaning) as EditText).text.toString()
                val strSample =
                    (tableLayout.findViewById<View>(R.id.txtSample) as EditText).text.toString()
                Insert(strWord, strMeaning, strSample)
            }
            .setNegativeButton(
                "取消"
            ) { dialogInterface, i -> }
            .create()
            .show()
    }

    private fun DeleteDialog() {
        val tableLayout = layoutInflater.inflate(R.layout.delete, null) as TableLayout
        AlertDialog.Builder(this)
            .setTitle("删除单词")
            .setView(tableLayout)
            .setPositiveButton("确定") { dialogInterface, i ->
                val strWord =
                    (tableLayout.findViewById<View>(R.id.txtWordDelete) as EditText).text.toString()
                Delete(strWord)
            }
            .setNegativeButton(
                "取消"
            ) { dialogInterface, i -> }
            .create()
            .show()
    }

    private fun UpdateDialog() {
        val tableLayout = layoutInflater.inflate(R.layout.insert, null) as TableLayout
        AlertDialog.Builder(this)
            .setTitle("修改单词")
            .setView(tableLayout)
            .setPositiveButton("确定") { dialogInterface, i ->
                val strNewWord =
                    (tableLayout.findViewById<View>(R.id.txtWord) as EditText).text.toString()
                val strNewMeaning =
                    (tableLayout.findViewById<View>(R.id.txtMeaning) as EditText).text.toString()
                val strNewSample =
                    (tableLayout.findViewById<View>(R.id.txtSample) as EditText).text.toString()
                Update(strNewWord, strNewMeaning, strNewSample)
            }
            .setNegativeButton(
                "取消"
            ) { dialogInterface, i -> }
            .create()
            .show()
    }

    private fun SearchDialog() {
        val tableLayout = layoutInflater.inflate(R.layout.search, null) as TableLayout
        AlertDialog.Builder(this)
            .setTitle("查找单词")
            .setView(tableLayout)
            .setPositiveButton("确定") { dialogInterface, i ->
                val txtSearchWord =
                    (tableLayout.findViewById<View>(R.id.txtSearchWord) as EditText).text.toString()
                var items: Word? = Search(txtSearchWord)
                if (items != null) {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.putExtra("data", items)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "没有找到", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton(
                "取消"
            ) { dialogInterface, i -> }
            .create()
            .show()
    }

    private fun Insert(strWord: String, strMeaning: String, strSample: String) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "insert into Word(word, meaning, sample) values(?,?,?)",
            arrayOf(strWord, strMeaning, strSample)
        )
        val word = Word(strWord, strMeaning, strSample)
        if (adapter_other != null) {
            otherList.add(word.sample)
            adapter_other?.notifyItemInserted(otherList.size - 1)
        }
        wordList.add(word)
        adapter?.notifyItemInserted(wordList.size - 1)

    }

    private fun Delete(strWord: String) {
        val db = dbHelper.writableDatabase
        db.execSQL("delete from Word where word=?", arrayOf(strWord))
        var flag: Int = 0
        for (i in 0 until wordList.size) {
            if (strWord == wordList.get(i).word) {
                flag = i
                break //i就是索引
            }
        }
        if (adapter_other != null) {
            otherList.removeAt(flag)
            adapter_other?.notifyItemRemoved(flag)
            adapter_other?.notifyItemRangeChanged(0, otherList.size)
        }
        wordList.removeAt(flag)
        adapter?.notifyItemRemoved(flag)
        adapter?.notifyItemRangeChanged(0, wordList.size)

    }

    private fun Update(
        strWord: String,
        strMeaning: String,
        strSample: String
    ) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "update Word set word=?,meaning=?,sample=?",
            arrayOf(strWord, strMeaning, strSample)
        )
        var flag: Int = 0
        for (i in 0 until wordList.size) {
            if (strWord == wordList.get(i).word) {
                flag = i
                break //i就是索引
            }
        }
        val word = Word(strWord, strMeaning, strSample)
        if (adapter_other != null) {
            otherList.removeAt(flag)
            otherList.add(flag, word.sample)
            adapter_other?.notifyItemChanged(flag)
        }
        wordList.removeAt(flag)
        wordList.add(flag, word)
        adapter?.notifyItemChanged(flag)

    }

    private fun Search(strWordSearch: String): Word? {
        val db = dbHelper.writableDatabase
        val sql = "select * from Word where word like ?"
        val cursor = db.rawQuery(sql, arrayOf("%$strWordSearch%"))
        if (cursor.moveToFirst()) {
            val word = cursor.getString(cursor.getColumnIndex("word"))
            val meaning = cursor.getString(cursor.getColumnIndex("meaning"))
            val sample = cursor.getString(cursor.getColumnIndex("sample"))
            cursor.close()
            return Word(word, meaning, sample)
        }
        cursor.close()
        return null
    }
}