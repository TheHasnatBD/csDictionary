package xyz.hasnat.csdictionary.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dic_single_row, viewGroup, false);

        return new WordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordListViewHolder viewHolder, int i) {

        viewHolder.wordTv.setText(""+words.get(i).getWord());

        String s = words.get(i).getDefinition();
        viewHolder.definitionTv.setText("- "+s);
        viewHolder.expandableLayout.setVisibility(View.GONE);

        viewHolder.word_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.expandableLayout.setVisibility(View.VISIBLE);
                //viewHolder.expandableLayout.animate().alpha(1.0f).setDuration(1000);
                //viewHolder.expandableLayout.clearAnimation();
            }
        });

        viewHolder.expandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewHolder.expandableLayout.animate().alpha(0.0f).setDuration(1000);
                //viewHolder.expandableLayout.clearAnimation();

                viewHolder.expandableLayout.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return words.size();
    }


    class WordListViewHolder extends RecyclerView.ViewHolder {
        TextView wordTv, definitionTv;
        RelativeLayout expandableLayout;
        LinearLayout word_layout;
        public WordListViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTv = itemView.findViewById(R.id.wordTv);
            definitionTv = itemView.findViewById(R.id.fullDefinitionTV);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            word_layout = itemView.findViewById(R.id.word_layout);
        }
    }



    public void updateCollection(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }
}
