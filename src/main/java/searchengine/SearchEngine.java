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
import searchengine.dba.Stemmer;
import searchengine.dba.WebPage;
import searchengine.dba.Word;

public class SearchEngine {
    private Indexer indexer;
    private String originalSearchInput;
    private Stemmer stemmer;

    public SearchEngine(String filename) {
        try {
            indexer = new Indexer(filename);
            stemmer = new Stemmer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
    * Search compiles and return a List of WebPages in descending order by each Webpage combined Score.
    * @param  searchTerm An string that can have one or multiple words divided by %20 (as it is obtained from a URL)
    * @return List<WebPage> a List of WebPages in descending order by each Webpage combined Score.
    */
    public List<WebPage> search(String searchTerm) {
        long start = System.currentTimeMillis();

        originalSearchInput = searchTerm;
        List<List<String>> splittedInput = splitInput(searchTerm);
        HashMap<WebPage, Double> unorderedHashMap = compileHashMaps(splittedInput);
        List<WebPage> result = unorderedHashMap.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Took " + timeElapsed + " miliseconds to run the  search");

        return result;
    }

    /**
    * Splits and stores input string by OR operator in splittedByOr List,
    and then it splits the elements in splittedByOr List with %20.*/  
    public List<List<String>> splitInput(String searchTerm) {
        var splittedInput = new ArrayList<List<String>>();
        String[] splittedByOr = searchTerm.split("(?i) or ");
        for (String andSentence : splittedByOr) {
            splittedInput.add(Arrays.asList(andSentence.split("%20")));
        }
        return splittedInput;
    }

    /**
    * It compiles multiple hashmaps obtained in getScoreMapWithANDlogic using getScoreMapWithORlogic
    * @return It returns a hashmap that relates webpages with their corresponding score after being merged.
    * @param listOfTerms A list of lists of searchTerms
    */
    public HashMap<WebPage, Double> compileHashMaps(List<List<String>> listOfTerms) {
        List<HashMap<WebPage, Double>> mapsWithOrLogic = new ArrayList<>();
        for (List<String> listJoinedByAnd : listOfTerms) {
            mapsWithOrLogic.add(createANDLogicMap(listJoinedByAnd));
        }
        return mergeORLogicMaps(mapsWithOrLogic);
    }
    
    /**
    * It creates a hashmap that relates webpages with their corresponding score.
    * It combines the score of different words by the And logic. (In case a webpage is duplicated it will add the score)
    * @param  listWithANDLogic A List of type String that stores one or multiple searchTerms.
    * @return It returns a hashmap with webpages and their corresponding score based on AND Score logic (Add the scores when a page is duplicated). 
    */
    public HashMap<WebPage,Double> createANDLogicMap(List<String> listWithANDLogic){
        HashMap<WebPage,Double> mapOfWebPages = new HashMap<>();
        for (String term : listWithANDLogic) {
            var termProcessed =  cleanWord(term);
            Word wordSearched = indexer.getWord(termProcessed);
            if (wordSearched != null) {
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
    * It merges a list of one or multiple hashmaps into a single hashmap using OR Logic (Keep the max score when a page is duplicated).
    * The criteria for merging is keeping the maximum double value. 
    * @param  mapsWithOrLogic It is a List of Hashmaps with webpages and their corresponding score according to ADD Logic.
    * @return It returns the merged hashmap.
    */
    public HashMap<WebPage,Double> mergeORLogicMaps(List<HashMap<WebPage,Double>> mapsWithOrLogic){
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
    * It calculates the score of a word of a webpage.
    * @param  word It is a word from the inverted index.
    * @param  webPage Represents a real webpage crawled stored in a database.
    * @return It returns the score of a searched word for ranking websites by relevance.
    */
    public double getPageScore(Word word, WebPage webPage) {
        return (double) word.getWebPageFrequency(webPage) /(double) word.getTotalFrequency();
    } 

    public String cleanWord(String word){
        return stemmer.stemWord(word.replaceAll("[.,!\\?´¨^*:;{&¤}+á¼ï»î±î¿ä]", "").toLowerCase());
    }

    // Getters and Setters
    public void setSearchInput(String searchInput) {
        this.originalSearchInput = searchInput;
    }
    public String getSearchInput(){
        return originalSearchInput;
    }
}
