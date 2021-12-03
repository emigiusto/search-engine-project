package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import searchengine.dba.Indexer;
import searchengine.dba.WebPage;
import searchengine.dba.Word;

public class SearchEngine {
    private Indexer indexer;

    public SearchEngine(String filename) {
        try {
            indexer = new Indexer(filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<WebPage> search(String searchTerm) {
        //Converts HashSet to ArrayList and returns result
        var result = new ArrayList<WebPage>(indexer.getWord(searchTerm).getAllWebPages());
        return result;
    }
    
    public Map<WebPage, Double> frequencyScore(String searchTerm) {
        Word wordSearched = indexer.getWord(searchTerm);

        Set<WebPage> allWebPages = wordSearched.getAllWebPages();
        double totalFrequency = wordSearched.getTotalFrequency();
        
        Map <WebPage, Double> frequencyHits = new HashMap<>();
        for (WebPage webPage : allWebPages) {
            double frequency = wordSearched.getWebPageFrequency(webPage);
            double idfScore = frequency / totalFrequency;
            frequencyHits.put(webPage, idfScore);
        }
        return frequencyHits;
    }
}
