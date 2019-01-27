package xyz.hasnat.csdictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.hasnat.csdictionary.Model.Word;

public class DicLoadFromFile {
    /**
     *  For first time DATABASE installation,
     * @param bufferedReader
     * @param databaseHelper
     */
    public static void loadData(BufferedReader bufferedReader, DicDatabaseHelper databaseHelper){
        ArrayList<Word> wordArrayList = new ArrayList<>();

        try {
            BufferedReader fileReader = bufferedReader;
            try {
                int c = fileReader.read();
                while (c != (-1)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((char)c != '\n' && c != -1) {
                        try {
                            stringBuilder.append((char)c);
                        } catch (Exception e) {
                            System.out.println(stringBuilder.length());
                            //e.printStackTrace();
                        }
                        c = fileReader.read();
                        if (c == -1) {
                            return;
                        }
                    }
                    String wordString = stringBuilder.toString(); // only word


                    // passing definition
                    ArrayList<String> definition = new ArrayList<>();
                    while (c == '\n' || c == '\t') {
                        c = fileReader.read();
                        if (c == '\n' || c == '\t' || c == '\r') {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            while (c != '\n') {
                                stringBuilder2.append((char)c);
                                c=fileReader.read();
                            }
                            String definitionString = stringBuilder2.toString();
                            definition.add(definitionString);
                        }else {
                            break;
                        }

                    }
                    wordString=wordString.trim();
                    //Log.e("word Load: "+(++counter)+" :"+wordString);
                    wordArrayList.add(new Word(wordString, definition)); // add to array list
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // after adding word to array list, pass these into DATABASE for initial setup
                databaseHelper.firstTimeDatabaseInstallation(wordArrayList);
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
