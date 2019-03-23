package xyz.hasnat.csdictionary

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.text.SearchIterator
import android.speech.RecognizerIntent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.Locale

import xyz.hasnat.csdictionary.adapter.WordListAdapterRV
import xyz.hasnat.csdictionary.constant.Constant
import xyz.hasnat.csdictionary.database.DicDatabaseHelper
import xyz.hasnat.csdictionary.database.DicLoadFromFile
import xyz.hasnat.csdictionary.model.Word
import xyz.hasnat.sweettoast.SweetToast

class MainActivity : AppCompatActivity() {

    internal var sharedPreferences: SharedPreferences? = null
    private var databaseHelper: DicDatabaseHelper? = null
    private var wordList: List<Word> = ArrayList()

    private var adapterRV: WordListAdapterRV? = null
    internal var linearLayoutManager: LinearLayoutManager? = null
    internal var word_listRV: RecyclerView? = null
    internal var searchView: SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word_listRV = findViewById(R.id.word_listRV)
        searchView = findViewById(R.id.searchView)

        databaseHelper = DicDatabaseHelper(this, "dictionary", null, 1)
        linearLayoutManager = LinearLayoutManager(this@MainActivity)


        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE)
        val is_installed = sharedPreferences!!.getBoolean(Constant.INSTALLED, false)
        if (!is_installed) {
            Toast.makeText(this, "Installing database", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "new user! installing DB............")
            installDatabase()

            val editor = sharedPreferences!!.edit()
            editor.putBoolean(Constant.INSTALLED, true)
            editor.apply()
            editor.commit()
        } else {
            Log.e("TAG", "Welcome.. DB already installed.")
        }


        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                /*
                List<Word> filteredList = new ArrayList<>();
                Word word = databaseHelper.getSingleWord(s);
                if (word != null){
                    filteredList.add(word);
                    no_wordTv.setVisibility(View.GONE);
                    adapterRV = new WordListAdapterRV(filteredList, MainActivity.this);
                    //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    word_listRV.setLayoutManager(linearLayoutManager);
                    word_listRV.setAdapter(adapterRV);
                    adapterRV.updateCollection(filteredList);
                } else {
                    filteredList.clear();
                    no_wordTv.setVisibility(View.VISIBLE);
                }
                */

                val filteredList = ArrayList<Word>()
                filteredList.clear()
                for (i in wordList.indices) {
                    val text = wordList[i].word
                    if (text == s) {
                        filteredList.add(Word(wordList[i].word!!, wordList[i].definition!!))
                    }
                }
                //no_wordTv.setVisibility(View.GONE);
                adapterRV = WordListAdapterRV(filteredList, this@MainActivity)
                //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                word_listRV!!.layoutManager = linearLayoutManager
                word_listRV!!.adapter = adapterRV
                adapterRV!!.notifyDataSetChanged()

                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                val filteredList = ArrayList<Word>()
                filteredList.clear()
                for (i in wordList.indices) {
                    val text = wordList[i].word

                    if (text!!.contains(s)) {
                        filteredList.add(Word(wordList[i].word!!, wordList[i].definition!!))
                    }

                }
                adapterRV = WordListAdapterRV(filteredList, this@MainActivity)
                //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                word_listRV!!.layoutManager = linearLayoutManager
                word_listRV!!.adapter = adapterRV
                adapterRV!!.notifyDataSetChanged()

                return false
            }
        })


        wordList = databaseHelper!!.allWords // list of all words

        // pass list to the recycler view
        adapterRV = WordListAdapterRV(wordList, this)
        word_listRV!!.layoutManager = linearLayoutManager
        word_listRV!!.adapter = adapterRV
    } //ending onCreate


    private fun installDatabase() {
        val inputStream = resources.openRawResource(R.raw.dictionary)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        DicLoadFromFile.loadData(bufferedReader, databaseHelper!!)
    }

    fun recordVoice(view: View) {
        // implicit intent
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.ACTION_WEB_SEARCH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 999)
        } else {
            SweetToast.error(this@MainActivity, "Sorry, your device doesn't support this feature")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            999 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val result_string = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                searchView!!.setQuery(result_string[0], false)
            }
        }
    }

    // This method is used to detect back button
    override fun onBackPressed() {
        if (TIME_LIMIT + backPressed > System.currentTimeMillis()) {
            super.onBackPressed()
            //Toast.makeText(getApplicationContext(), "Exited", Toast.LENGTH_SHORT).show();
        } else {
            SweetToast.defaultShort(this, "Press back again to exit")
            //Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis()
    } //End Back button press for exit...


    fun moreOptions(view: View) {
        SweetToast.info(this@MainActivity, "Upcoming features for settings")
    }

    companion object {

        private val TIME_LIMIT = 1500
        private var backPressed: Long = 0
    }
}