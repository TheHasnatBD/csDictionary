package xyz.hasnat.csdictionary.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.hasnat.csdictionary.Model.Word;
import xyz.hasnat.csdictionary.R;

public class WordListAdapterRV extends RecyclerView.Adapter<WordListAdapterRV.WordListViewHolder>{

    List<Word> words;
    Context context;

    public WordListAdapterRV(List<Word> words, Context context) {
        this.words = words;
        this.context = context;
    }

    @NonNull
    @Override
    public WordListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_dic_word, viewGroup, false);
        return new WordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListViewHolder viewHolder, int i) {
        String word = words.get(i).getWord();
        viewHolder.wordTv.setText(word);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class WordListViewHolder extends RecyclerView.ViewHolder {
        TextView wordTv;
        public WordListViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTv = itemView.findViewById(R.id.wordTv);
        }
    }

    public void updateCollection(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }
}
