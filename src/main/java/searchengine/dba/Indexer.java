package searchengine.dba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {
    private Map<String,Word> invertedIndex = new HashMap<String,Word>();
    private Stemmer stemmer = new Stemmer();
    private List<String> stopWords;

    public Indexer(String filename){
        try {
            stopWords = Files.readAllLines(Paths.get("data/stop-words.txt"));
            long start = System.currentTimeMillis(); //Manual BenchMarking
            fetchDatabase(filename);
            long timeElapsed = System.currentTimeMillis() - start; //Manual BenchMarking
            System.out.println("Took " + timeElapsed/1000 + " seconds to complete the Index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
    * fetchDatabase iterates into every line of the database file separating them by webpage by starting line "*PAGE"
    * If the page has content, updates invertedIndex hashmap using updateInvertedIndex function
    * @param  filename String containing the path of the database txt file
    */
    public void fetchDatabase(String filename) throws IOException{
        try(BufferedReader bufferReader = new BufferedReader(new FileReader(filename))) 
        {
            String line = bufferReader.readLine();
            int pageCounter = 0;
            while (line != null) {
                if (line.startsWith("*PAGE")){
                    List<String> content = new ArrayList<>();
                    var nextLine = bufferReader.readLine();
                    boolean condition = (nextLine != null) && (!(nextLine.startsWith("*PAGE")));
                    while (condition) {
                        content.add(nextLine);
                        nextLine = bufferReader.readLine();
                        condition = (nextLine != null) && (!(nextLine.startsWith("*PAGE")));
                    }
                    if (content.size()>1) {
                        updateInvertedIndex(new WebPage(content.get(0), line.substring(6)), content.subList(1, content.size()));
                        pageCounter++;
                    } else {
                        System.out.println("page " + line.substring(6) + " has not been indexed");
                    }
                    line = nextLine;
                }
            }
            bufferReader.close();
            System.out.println(pageCounter + " pages have been indexed");
            System.out.println("The inverted Index is complete");
        }
        catch(Exception e){
            System.err.println("Error: Target File Cannot Be Read");
        }
    }

    /** 
    * updateInvertedIndex iterates into the content of a webpage and creates or updates a record 
    * in the invertedIndex HashMap mapping each string in the context with the Word object (creating a new one or updating the existent one)
    * @param  content List of String containing the content of the specific webpage
    * @param  webPage Represents the webpage stored in a database.
    */
    public void updateInvertedIndex(WebPage webPage, List<String> content) {
        try {
            for (String word : content) {
                var reformattedWord = cleanWord(word);
                if (reformattedWord.length()>0) {
                    if (invertedIndex.containsKey(reformattedWord)) {
                        invertedIndex.get(reformattedWord).addOcurrence(webPage);
                    } else {
                        invertedIndex.put(reformattedWord,new Word(reformattedWord,webPage));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error in updateInvertedIndex");
            System.out.println(e.getMessage());
        }
    }

    /**
    * Remove all signs or punctuation marks from an input string, transforms it to lowercase and reduces it to a root form (Stemming).
    * Also, checks if the resulting string is not contained in the list of "Stop Words". If it is contained, then returns null
    * @param  word It is a word from the inverted index.
    * @return Returns the resulting string or null (if the word is considered an "Stop Word")
    */
    public String cleanWord(String word) {
        var wordTrimmedLowerCase = word.replaceAll("[.,!\\?´¨^*$:;{&¤}/+á¼ï()»î±î¿ä]", "").toLowerCase();
        return stopWords.contains(wordTrimmedLowerCase) ? "" : stemmer.stemWord(wordTrimmedLowerCase);
    }

    public Word getWord(String word){
        return invertedIndex.get(word);
    }

    public Map<String,Word> getInvertedIndex(){
        return invertedIndex;
    }
}
