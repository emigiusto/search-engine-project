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
            long start = System.currentTimeMillis();
            stopWords = Files.readAllLines(Paths.get("data/stop-words.txt"));
            fetchDatabase(filename);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Took " + timeElapsed/1000 + " seconds to complete the Index");
        } catch (IOException e) {
            System.out.println("Indexer Error");
            e.printStackTrace();
        }
    }

    /** 
   * Retrieve all lines in database starting with "*PAGE" */  
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
                        createInvertedIndex(new WebPage(content.get(0), line.substring(6)), content.subList(1, content.size()));
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
   * invertedIndex maps words from webPage using addOccurence */  
    public void createInvertedIndex(WebPage webPage, List<String> content) {
        try {
            for (String word : content) {
                var reformattedWord = clean(word);
                if (reformattedWord.length()>0) {
                    if (invertedIndex.containsKey(reformattedWord)) {
                        invertedIndex.get(reformattedWord).addOcurrence(webPage);
                    } else {
                        invertedIndex.put(reformattedWord,new Word(reformattedWord,webPage));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error in createInvertedIndex");
            System.out.println(e.getMessage());
        }
    }

    public String clean(String word) {
        var wordTrimmedLowerCase = word.replaceAll("[.,!\\?´¨^*:;{&¤}+á¼ï»î±î¿ä]", "").toLowerCase();
        return stopWords.contains(wordTrimmedLowerCase) ? "" : stemmer.stemWord(wordTrimmedLowerCase);
    }

    public Word getWord(String word){
        return invertedIndex.get(word);
    }
}
