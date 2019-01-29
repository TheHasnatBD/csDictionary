package xyz.hasnat.csdictionary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import xyz.hasnat.csdictionary.Model.Word;

public class DicDatabaseHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "dictionary";
    final static String ITEM_ID_COLUMN = "id";
    final static String WORD_COLUMN = "word";
    final static String DEFINITION_COLUMN = "definition";

    final static String CREATE_TABLE_QUERY =
            "CREATE TABLE "+ DB_NAME + " ( "+ ITEM_ID_COLUMN+
                    " INTEGER PRIMARY KEY AUTOINCREMENT, "+WORD_COLUMN+
                    " TEXT, "+ DEFINITION_COLUMN +" TEXT )";

    final static String ON_UPGRADE_QUERY = "DROP TABLE "+DB_NAME;

    Context context;

    public DicDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL(ON_UPGRADE_QUERY);
        onCreate(sqLiteDatabase);
    }


    //get ALL words from db >> for showing on LIST
    public ArrayList<Word> getAllWrods(){
        ArrayList<Word> wordArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String select_all_query = "SELECT * FROM " + DB_NAME;
        Cursor cursor = database.rawQuery(select_all_query, null);
        if (cursor.moveToFirst()){
            do {
                Word word = new Word(cursor.getString(cursor.getColumnIndex(WORD_COLUMN)), cursor.getString(cursor.getColumnIndex(DEFINITION_COLUMN)));
                wordArrayList.add(word);
            } while (cursor.moveToNext());
        }
        return wordArrayList;
    }


    // selecting for a single word, for SEARCHING
    public Word getSingleWord(String searchString){
        SQLiteDatabase database = this.getReadableDatabase();
        Word word = null;
        String select_query_single = "SELECT * FROM "+ DB_NAME +" WHERE "+WORD_COLUMN+" = '"+ searchString +"'";
        Cursor cursor = database.rawQuery(select_query_single, null);
        if (cursor.moveToFirst()){
            word = new Word(cursor.getString(cursor.getColumnIndex(WORD_COLUMN)), cursor.getString(cursor.getColumnIndex(DEFINITION_COLUMN)));
        }
        return word;
    }


    //INSERT into DB For 1st time
    public void firstTimeDatabaseInstallation(ArrayList<Word> words){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("BEGIN");
        ContentValues contentValues = new ContentValues();
        for (Word word : words) {
            contentValues.put(WORD_COLUMN, word.getWord());
            contentValues.put(DEFINITION_COLUMN, word.getDefinition());

            database.insert(DB_NAME, null, contentValues);
        }
        database.execSQL("COMMIT");
    }

}
