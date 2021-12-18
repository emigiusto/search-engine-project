package searchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import searchengine.dba.Indexer;
import searchengine.dba.WebPage;
import searchengine.dba.Word;

public class SearchEngine {
    private Indexer indexer;
    private String searchInput;
    private List<List<String>> splittedInput = new ArrayList<>();

  
  /** 
   * Creates new indexer object from String filename */  

    public SearchEngine(String filename) {
        try {
            indexer = new Indexer(filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
   * Creates a list of webpages containing searchTerm */  

    public List<WebPage> search(String searchTerm) {
        searchInput = searchTerm;
        splittingInput();
        HashMap<WebPage, Double> unorderedHashMap = gatherWebpages();

        List<WebPage> result = unorderedHashMap.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        return result;
    }

    /**
    * Splits and stores input string by OR operator in splittedByOr List,
    and then it splits the elements in splittedByOr List with %20.*/  

    public void splittingInput() {
        String[] splittedByOr = searchInput.split("(?i) or ");
    
        for (String andSentence : splittedByOr) {
            splittedInput.add(Arrays.asList(andSentence.split("%20")));
        }
    }

    /**
    * It merges multiple hashmaps obtained in getScoreMapWithANDlogic using getScoreMapWithORlogic
    * @return It returns a hashmap that relates webpages with their corresponding score after being merged.
    */

    public HashMap<WebPage, Double> gatherWebpages() {
        List<HashMap<WebPage, Double>> mapsWithOrLogic = new ArrayList<>();
        for (List<String> listJoinedByAnd : splittedInput) {
            mapsWithOrLogic.add(getScoreMapWithANDlogic(listJoinedByAnd));
        }
        return getScoreMapWithORlogic(mapsWithOrLogic);
    }
    
    /**
    * It creates a hashmap that relates webpages with their corresponding score.
    * It combines the score of different words by the And logic. (in case a webpage is duplicated it will add the score)
    * @param  listWithANDLogic A List of type String that stores webpages and their score.
    * @return It returns a hashmap with webpages and their corresponding score. 
    */

    public HashMap<WebPage,Double> getScoreMapWithANDlogic(List<String> listWithANDLogic){
        HashMap<WebPage,Double> mapOfWebPages = new HashMap<>();
        for (String term : listWithANDLogic) {
            if (indexer.getWord(term) != null) {
                
                Word wordSearched = indexer.getWord(term);
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
        }
        return mapOfWebPages;
    }

    /**
    * It merges mulitple hashmaps consisting of splitted strings by an Or operator, into a single hashmap.
    * The criteria for merging is keeping the maximum double value. 
    * @param  mapsWithOrLogic It is a List of Hashmaps with webpages and their corresponding score.
    * @return It returns the merged hashmap.
    */

    public HashMap<WebPage,Double> getScoreMapWithORlogic(List<HashMap<WebPage,Double>> mapsWithOrLogic){
        HashMap<WebPage,Double> mapOfWebPages = new HashMap<>();
        for (HashMap<WebPage,Double> hashMap : mapsWithOrLogic) {
            for (WebPage webPage : hashMap.keySet()) {
                if (mapOfWebPages.containsKey(webPage)) {
                    
                    mapOfWebPages.put(webPage,Math.max(hashMap.get(webPage) , mapOfWebPages.get(webPage)));
                
                } else {
                    mapOfWebPages.put(webPage, hashMap.get(webPage));
                }
            }
        }
        return mapOfWebPages;
}

    /**
    * It calculates the score of a @word of a webpage.
    * @param  word It is a word from the inverted index.
    * @param  webPage Represents a real webpage crawled stored in a database.
    * @return It returns the score of a searched word for ranking websites by relevance.
    */

    public double getPageScore(Word word, WebPage webPage) {
        return (double) word.getWebPageFrequency(webPage) /(double) word.getTotalFrequency();
    } 
}
