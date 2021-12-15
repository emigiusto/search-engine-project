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
    private String searchInput;
    private List<List<String>> splittedInput = new ArrayList<>();

    public SearchEngine(String filename) {
        try {
            indexer = new Indexer(filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<WebPage> search(String searchTerm) {
        searchInput = searchTerm;
        splittingInput();
        var result = new ArrayList<WebPage>(indexer.getWord(searchTerm).getAllWebPages());
        return result;
    }

    public void splittingInput() {
        searchInput.split(" ");
        searchInput.split("or");
    }
    
    public double getPageScore(Word word, WebPage webPage) {
        
        return word.getWebPageFrequency(webPage) / word.getTotalFrequency();
        
    }

    public Map<WebPage, Double> frequencyScore(String searchTerm) {
        Word wordSearched = indexer.getWord(searchTerm);

        Set<WebPage> allWebPages = wordSearched.getAllWebPages();
                
        Map <WebPage, Double> frequencyHits = new HashMap<>();
        for (WebPage webPage : allWebPages) {
            var idfScore = getPageScore(wordSearched, webPage); 
            frequencyHits.put(webPage, idfScore);
        }
        return frequencyHits;
    }


}
