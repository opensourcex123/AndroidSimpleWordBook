package com.example.kotlinwordbook

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri

class DatabaseProvider : ContentProvider() {

    private val authority = "com.example.kotlinwordbook.provider"
    private var dbHelper: MyDataBase? = null
    private val uriMatcher by lazy {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority, "word", 0)
        matcher
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = dbHelper?.let {
        val db = it.writableDatabase
        val deleteRows = when (uriMatcher.match(uri)) {
            0 -> db.delete("word", selection, selectionArgs)
            else -> 0
        }
        deleteRows
    } ?: 0

    override fun getType(uri: Uri) = when(uriMatcher.match(uri)) {
        0 -> "vnd.android.cursor.dir/vnd.com.example.kotlinwordbook.provider.word"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?) = dbHelper?.let {
        val db = it.writableDatabase
        val uriReturn = when (uriMatcher.match(uri)) {
            0 -> {
                val newWordId = db.insert("word", null, values)
                Uri.parse("content://$authority/word/$newWordId")
            }
            else -> null
        }
        uriReturn
    }

    override fun onCreate() = context?.let {
        dbHelper = MyDataBase(it, "WordBook.db", 2)
        true
    } ?: false

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ) = dbHelper?.let {
        val db = it.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            0 -> db.query("word", projection, selection, selectionArgs, null, null, sortOrder)
            else -> null
        }
        cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = dbHelper?.let {
        val db = it.writableDatabase
        val updateRows = when (uriMatcher.match(uri)) {
            0 -> db.update("word", values, selection, selectionArgs)
            else -> 0
        }
        updateRows
    } ?: 0
}