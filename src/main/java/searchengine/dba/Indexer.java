package searchengine.dba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

 
public class Indexer {
    private List<WebPage> pages = new ArrayList<>();
    private Map<String,Word> invertedIndex = new TreeMap<String,Word>();

    public Indexer(String filename){
        try {
            fetchDatabase(filename);
            createInvertedIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 

/** 
   * Retrieve all lines in database starting with "*PAGE" */  
    public void fetchDatabase(String filename) throws IOException{
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            var lastIndex = lines.size();
            for (var i = lines.size() - 1; i >= 0; --i) {
                if (lines.get(i).startsWith("*PAGE")) {
                    pages.add(new WebPage(lines.subList(i+1, lastIndex), lines.get(i).substring(6,lines.get(i).length())));
                    lastIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



/** 
   * Foreach webpage  */  
    public void createInvertedIndex() {
        try {
            for (WebPage webPage : pages) {
                for (String word : webPage.getContent()) {
                    if (invertedIndex.containsKey(word)) {
                        invertedIndex.get(word).addOcurrence(webPage);
                    } else {
                        invertedIndex.put(word,new Word(word,webPage));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    public Word getWord(String word){
        return invertedIndex.get(word);
    }

    public  List<WebPage> getAllPages() {
        return pages;
    }
}
