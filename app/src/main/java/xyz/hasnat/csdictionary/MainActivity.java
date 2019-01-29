   package xyz.hasnat.csdictionary;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import xyz.hasnat.csdictionary.Adapter.WordListAdapterRV;
import xyz.hasnat.csdictionary.Constant.Constant;
import xyz.hasnat.csdictionary.Database.DicDatabaseHelper;
import xyz.hasnat.csdictionary.Database.DicLoadFromFile;
import xyz.hasnat.csdictionary.Model.Word;

   public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DicDatabaseHelper databaseHelper;
    private List<Word> wordList = new ArrayList<>();

    private WordListAdapterRV adapterRV;
    RecyclerView word_listRV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        word_listRV = findViewById(R.id.word_listRV);

        databaseHelper = new DicDatabaseHelper(this, "dictionary", null, 1);

        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF, MODE_PRIVATE);
        boolean is_installed = sharedPreferences.getBoolean(Constant.INSTALLED, false);
        if (!is_installed){
            Toast.makeText(this, "Installing Database", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "new user! installing DB............");
            installDatabase();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constant.INSTALLED, true);
            editor.commit();
        } else {
            Log.e("TAG", "Welcome.. DB already installed.");
        }
        wordList = databaseHelper.getAllWrods(); // list of all words


        // add to adapter
        if (adapterRV == null){
            adapterRV = new WordListAdapterRV(wordList, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            word_listRV.setLayoutManager(linearLayoutManager);
            word_listRV.setAdapter(adapterRV);
        } else {
            adapterRV.updateCollection(wordList);
        }


    } //ending onCreate

       private void installDatabase() {
           InputStream inputStream = getResources().openRawResource(R.raw.dictionary);
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
           DicLoadFromFile.loadData(bufferedReader, databaseHelper);
       }




   }