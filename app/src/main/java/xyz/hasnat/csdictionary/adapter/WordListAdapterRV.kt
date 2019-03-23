package xyz.hasnat.csdictionary.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import xyz.hasnat.csdictionary.model.Word
import xyz.hasnat.csdictionary.R

class WordListAdapterRV(private var words: List<Word>, private var context: Context) : RecyclerView.Adapter<WordListAdapterRV.WordListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WordListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.dic_single_row, viewGroup, false)

        return WordListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: WordListViewHolder, i: Int) {
        viewHolder.wordTv.text = "" + words[i].word!!

        val s = words[i].definition
        viewHolder.definitionTv.text = "- " + s!!
        viewHolder.expandableLayout.visibility = View.GONE

        viewHolder.wordLayout.setOnClickListener {
            viewHolder.expandableLayout.visibility = View.VISIBLE
            //viewHolder.expandableLayout.animate().alpha(1.0f).setDuration(1000);
            //viewHolder.expandableLayout.clearAnimation();
        }

        viewHolder.expandableLayout.setOnClickListener {
            //viewHolder.expandableLayout.animate().alpha(0.0f).setDuration(1000);
            //viewHolder.expandableLayout.clearAnimation();

            viewHolder.expandableLayout.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return words.size
    }


    inner class WordListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wordTv: TextView = itemView.findViewById(R.id.wordTv)
        var definitionTv: TextView = itemView.findViewById(R.id.fullDefinitionTV)
        var expandableLayout: RelativeLayout = itemView.findViewById(R.id.expandableLayout)
        var wordLayout: LinearLayout = itemView.findViewById(R.id.word_layout)
    }


    fun updateCollection(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }
}
