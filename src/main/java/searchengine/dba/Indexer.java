package searchengine.dba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

 
public class Indexer {
    private List<WebPage> pages = new ArrayList<>();
    private Map<String,Word> invertedIndex = new TreeMap<String,Word>();

    public Indexer(String filename){
        try {
            long start = System.currentTimeMillis();
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
                    if (content.size()>2) {
                        createInvertedIndex(new WebPage(content.get(0), line.substring(6)), content);
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
                if (invertedIndex.containsKey(word)) {
                    invertedIndex.get(word).addOcurrence(webPage);
                } else {
                    invertedIndex.put(word,new Word(word,webPage));
                }
            }
        } catch (Exception e) {
            System.out.println("error in createInvertedIndex");
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
