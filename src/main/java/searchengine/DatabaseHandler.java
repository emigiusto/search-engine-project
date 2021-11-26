package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

public class DatabaseHandler {
    private List<WebPage> pages = new ArrayList<>();
    private Map<String,HashMap<WebPage,Integer>> invertedIndex = new TreeMap<String,HashMap<WebPage,Integer>>();

    public DatabaseHandler(String filename){
        try {
            fetchDatabase(filename);
            createInvertedIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void createInvertedIndex() {
        try {
            for (WebPage webPage : pages) {
                System.out.println("aaaa");
                for (String word : webPage.getContent()) {
                    if (invertedIndex.containsKey(word)) {

                        var currentRecord = invertedIndex.get(word);
                        if (currentRecord.containsKey(webPage)) {
                            currentRecord.put(webPage,currentRecord.get(webPage)+1);
                            invertedIndex.put(word,currentRecord);
                        } else {
                            currentRecord.put(webPage,1);
                            invertedIndex.put(word,currentRecord);
                        }
                    } else {
                        var newEntry = new HashMap<WebPage,Integer>();
                            newEntry.put(webPage,1);
                        invertedIndex.put(word,newEntry);
                    }
                    
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    public  List<WebPage> getPages() {
        return pages;
    }
}
