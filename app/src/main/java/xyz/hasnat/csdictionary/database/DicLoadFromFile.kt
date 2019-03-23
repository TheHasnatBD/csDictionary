package xyz.hasnat.csdictionary.database

import java.io.BufferedReader
import java.io.IOException
import java.util.ArrayList

import xyz.hasnat.csdictionary.model.Word

object DicLoadFromFile {
    /**
     * For first time DATABASE installation,
     * @param bufferedReader
     * @param databaseHelper
     */

    fun loadData(bufferedReader: BufferedReader, databaseHelper: DicDatabaseHelper) {
        val wordArrayList = ArrayList<Word>()

        try {
            try {
                var c = bufferedReader.read()
                while (c != -1) {
                    val stringBuilder = StringBuilder()
                    while (c.toChar() != '\n' && c != -1) {
                        try {
                            stringBuilder.append(c.toChar())
                        } catch (e: Exception) {
                            println(stringBuilder.length)
                            //e.printStackTrace();
                        }

                        c = bufferedReader.read()
                        if (c == -1) {
                            return
                        }
                    }
                    var wordString = stringBuilder.toString() // only word


                    // passing definition
                    val definition = ArrayList<String>()
                    while (c == '\n'.toInt() || c == '\t'.toInt()) {
                        c = bufferedReader.read()
                        if (c == '\n'.toInt() || c == '\t'.toInt() || c == '\r'.toInt()) {
                            val stringBuilder2 = StringBuilder()
                            while (c != '\n'.toInt()) {
                                stringBuilder2.append(c.toChar())
                                c = bufferedReader.read()
                            }
                            val definitionString = stringBuilder2.toString()
                            definition.add(definitionString)
                        } else {
                            break
                        }

                    }
                    wordString = wordString.trim { it <= ' ' }
                    //Log.e("word Load: "+(++counter)+" :"+wordString);
                    wordArrayList.add(Word(wordString, definition)) // add to array list
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                // after adding word to array list, pass these into DATABASE for initial setup
                databaseHelper.firstTimeDatabaseInstallation(wordArrayList)
                bufferedReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
