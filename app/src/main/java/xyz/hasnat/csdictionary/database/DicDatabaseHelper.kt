package xyz.hasnat.csdictionary.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

import xyz.hasnat.csdictionary.model.Word

class DicDatabaseHelper(var context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DB_NAME, factory, version) {

    companion object {
        val DB_NAME = "dictionary"
        val ITEM_ID_COLUMN = "id"
        val WORD_COLUMN = "word"
        val DEFINITION_COLUMN = "definition"

        val CREATE_TABLE_QUERY = "CREATE TABLE " + DB_NAME + " ( " + ITEM_ID_COLUMN +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORD_COLUMN +
                " TEXT, " + DEFINITION_COLUMN + " TEXT )"

        val ON_UPGRADE_QUERY = "DROP TABLE $DB_NAME"
    }

    //get ALL words from db >> for showing on LIST
    val allWords: ArrayList<Word>
        get() {
            val wordArrayList = ArrayList<Word>()
            val database = this.readableDatabase
            val selectAllQuery = "SELECT * FROM $DB_NAME"
            val cursor = database.rawQuery(selectAllQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val word = Word(cursor.getString(cursor.getColumnIndex(WORD_COLUMN)), cursor.getString(cursor.getColumnIndex(DEFINITION_COLUMN)))
                    wordArrayList.add(word)
                } while (cursor.moveToNext())
            }
            return wordArrayList
        }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVer: Int, newVer: Int) {
        sqLiteDatabase.execSQL(ON_UPGRADE_QUERY)
        onCreate(sqLiteDatabase)
    }


    // selecting for a single word, for SEARCHING
    fun getSingleWord(searchString: String): Word? {
        val database = this.readableDatabase
        var word: Word? = null
        val selectQuerySingle = "SELECT * FROM $DB_NAME WHERE $WORD_COLUMN = '$searchString'"
        val cursor = database.rawQuery(selectQuerySingle, null)
        if (cursor.moveToFirst()) {
            word = Word(cursor.getString(cursor.getColumnIndex(WORD_COLUMN)), cursor.getString(cursor.getColumnIndex(DEFINITION_COLUMN)))
        }
        return word
    }


    //INSERT into DB For 1st time
    fun firstTimeDatabaseInstallation(words: ArrayList<Word>) {
        val database = this.writableDatabase
        database.execSQL("BEGIN")
        val contentValues = ContentValues()
        for (word in words) {
            contentValues.put(WORD_COLUMN, word.word)
            contentValues.put(DEFINITION_COLUMN, word.definition)

            database.insert(DB_NAME, null, contentValues)
        }
        database.execSQL("COMMIT")
    }


}
