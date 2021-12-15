package searchengine;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
    private Map<WebPage, Double> resultMap = new HashMap<>();

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
        Set<WebPage>setOfWebpages = gatherWebpages();
        var result = new ArrayList<WebPage>(indexer.getWord(searchTerm).getAllWebPages());
        return result;
    }

    public void splittingInput() {
        String[] splittedByOr = searchInput.split("(?i) or ");
    
        for (String andSentence : splittedByOr) {
            splittedInput.add(Arrays.asList(andSentence.split(" ")));
        }
    }

    public Set<WebPage> gatherWebpages() {
        Set<WebPage> setOfWebpages = new HashSet<>();

        for (List<String> listJoinedByAnd : splittedInput) {
            //("Danish university or something else OR cookies oR mate");
            HashMap<WebPage,Double> andHashmap = getScoreWithANDlogic(listJoinedByAnd);
        }
        return setOfWebpages;
    }
    
    public HashMap<WebPage,Double> getScoreWithANDlogic(List<String> listWithANDogic){
        HashMap<WebPage,Double> mapOfWebPages = new HashMap<>();
        for (String term : listWithANDogic) {
            Word wordSearched = indexer.getWord(term);
            //("Danish university or something else OR cookies oR mate");
            Set<WebPage> allWebPages = wordSearched.getAllWebPages();
            for (WebPage webPage : allWebPages) {
                if (mapOfWebPages.containsKey(webPage)) {
                    Double totalScore = mapOfWebPages.get(webPage)+getPageScore(wordSearched,webPage);
                    mapOfWebPages.put(webPage,totalScore);
                } else {
                    mapOfWebPages.put(webPage, getPageScore(wordSearched,webPage));
                }
            }
        }
        return mapOfWebPages;
    }

    public Map<WebPage, Double> frequencyScore(String searchTerm) {
        Word wordSearched = indexer.getWord(searchTerm);

        Set<WebPage> allWebPages = wordSearched.getAllWebPages();
                
        Map <WebPage, Double> frequencyHits = new HashMap<>();
        for (WebPage webPage : allWebPages) {
            var idfScore = getPageScore(wordSearched, webPage); 
            resultMap.put(webPage, idfScore);
        }
        return frequencyHits;
    }

    public double getPageScore(Word word, WebPage webPage) {
        
        return word.getWebPageFrequency(webPage) / word.getTotalFrequency();
    }
    
}
