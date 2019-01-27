package xyz.hasnat.csdictionary.Model;

import java.util.ArrayList;

public class Word {
    private String word;
    private String definition;

    public Word(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public Word(String word, ArrayList<String> definition) {
        this.word = word;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : definition){
            stringBuilder.append(s);
        }
        this.definition = stringBuilder.toString();
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }


    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
