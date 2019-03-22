package xyz.hasnat.csdictionary;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SearchIterator;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xyz.hasnat.csdictionary.Adapter.WordListAdapterRV;
import xyz.hasnat.csdictionary.Constant.Constant;
import xyz.hasnat.csdictionary.Database.DicDatabaseHelper;
import xyz.hasnat.csdictionary.Database.DicLoadFromFile;
import xyz.hasnat.csdictionary.Model.Word;
import xyz.hasnat.sweettoast.SweetToast;

   public class MainActivity extends AppCompatActivity {

    private static final int TIME_LIMIT = 1500;
    private static long backPressed;

    SharedPreferences sharedPreferences;
    private DicDatabaseHelper databaseHelper;
    private List<Word> wordList = new ArrayList<>();

    private WordListAdapterRV adapterRV;
    LinearLayoutManager linearLayoutManager;
    RecyclerView word_listRV;
    SearchView searchView;
    TextView no_wordTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        word_listRV = findViewById(R.id.word_listRV);
        searchView = findViewById(R.id.searchView);
        no_wordTv = findViewById(R.id.no_wordTv);

        databaseHelper = new DicDatabaseHelper(this, "dictionary", null, 1);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);


        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF, MODE_PRIVATE);
        boolean is_installed = sharedPreferences.getBoolean(Constant.INSTALLED, false);
        if (!is_installed){
            Toast.makeText(this, "Installing database", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "new user! installing DB............");
            installDatabase();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constant.INSTALLED, true);
            editor.apply();
            editor.commit();
        } else {
            Log.e("TAG", "Welcome.. DB already installed.");
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
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

                ArrayList<Word> filteredList = new ArrayList<>();
                filteredList.clear();
                for (int i = 0; i < wordList.size(); i++) {
                    String text = wordList.get(i).getWord();
                    if (text.equals(s)) {
                        no_wordTv.setVisibility(View.GONE);
                        filteredList.add(new Word(wordList.get(i).getWord(),wordList.get(i).getDefinition()));
                    } else {
                        //no_wordTv.setVisibility(View.VISIBLE);
                    }
                }
                //no_wordTv.setVisibility(View.GONE);
                adapterRV = new WordListAdapterRV(filteredList, MainActivity.this);
                //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                word_listRV.setLayoutManager(linearLayoutManager);
                word_listRV.setAdapter(adapterRV);
                adapterRV.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Word> filteredList = new ArrayList<>();
                filteredList.clear();
                    for (int i = 0; i < wordList.size(); i++) {
                        String text = wordList.get(i).getWord();

                        if (text.contains(s)) {
                            no_wordTv.setVisibility(View.GONE);
                            filteredList.add(new Word(wordList.get(i).getWord(),wordList.get(i).getDefinition()));
                        } else {
                            //no_wordTv.setVisibility(View.VISIBLE);

                        }
                       // no_wordTv.setVisibility(View.VISIBLE);

                    }
                    //no_wordTv.setVisibility(View.GONE);
                    adapterRV = new WordListAdapterRV(filteredList, MainActivity.this);
                    //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    word_listRV.setLayoutManager(linearLayoutManager);
                    word_listRV.setAdapter(adapterRV);
                    adapterRV.notifyDataSetChanged();

                return false;
            }
        });


        wordList = databaseHelper.getAllWrods(); // list of all words
        no_wordTv.setVisibility(View.GONE);

        // pass list to the recycler view
        adapterRV = new WordListAdapterRV(wordList, this);
        word_listRV.setLayoutManager(linearLayoutManager);
        word_listRV.setAdapter(adapterRV);
    } //ending onCreate


       private void installDatabase() {
           InputStream inputStream = getResources().openRawResource(R.raw.dictionary);
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
           DicLoadFromFile.loadData(bufferedReader, databaseHelper);
       }

       public void recordVoice(View view) {
            // implicit intent
           Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
           intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.ACTION_WEB_SEARCH);
           intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

           if (intent.resolveActivity(getPackageManager()) != null){
               startActivityForResult(intent, 999);
           } else {
               SweetToast.error(MainActivity.this, "Sorry, your device doesn't support this feature");
           }
       }

       @Override
       protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           switch (requestCode){
               case 999:
                   if (resultCode == RESULT_OK &&  data != null){
                       ArrayList<String> result_string = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                       searchView.setQuery(result_string.get(0), false);
                   }
                   break;
           }
       }

       // This method is used to detect back button
       @Override
       public void onBackPressed() {
           if(TIME_LIMIT + backPressed > System.currentTimeMillis()){
               super.onBackPressed();
               //Toast.makeText(getApplicationContext(), "Exited", Toast.LENGTH_SHORT).show();
           }
           else {
               SweetToast.defaultShort(this, "Press back again to exit");
               //Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
           }
           backPressed = System.currentTimeMillis();
       } //End Back button press for exit...


       public void moreOptions(View view) {
        SweetToast.info(MainActivity.this, "Upcoming features for settings");
       }
   }