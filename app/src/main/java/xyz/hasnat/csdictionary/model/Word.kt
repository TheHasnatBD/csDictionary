package xyz.hasnat.csdictionary.model

import java.util.ArrayList

class Word {
    var word: String? = null
        private set
    var definition: String? = null
        private set

    constructor(word: String, definition: String) {
        this.word = word
        this.definition = definition
    }

    constructor(word: String, definition: ArrayList<String>) {
        this.word = word
        val stringBuilder = StringBuilder()
        for (s in definition) {
            stringBuilder.append(s)
        }
        this.definition = stringBuilder.toString()
    }


    override fun toString(): String {
        return "Word{" +
                "word='" + word + '\''.toString() +
                ", definition='" + definition + '\''.toString() +
                '}'.toString()
    }
}
