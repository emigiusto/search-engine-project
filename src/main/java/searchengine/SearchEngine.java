package searchengine;

import java.nio.file.Files;
import java.nio.file.Paths;
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
    private List<String> stopWords;

    public SearchEngine(String filename) {
        try {
            indexer = new Indexer(filename);
            stemmer = new Stemmer();
            stopWords = Files.readAllLines(Paths.get("data/stop-words.txt"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
    * Compiles and return a List of WebPages in descending order by each Webpage combined Score.
    * @param  searchTerm An string that can have one or multiple words divided by %20 (as it is obtained from a URL)
    * @return List<WebPage> a List of WebPages in descending order by each Webpage combined Score.
    */
    public List<WebPage> search(String searchTerm) {
        originalSearchInput = searchTerm;
        List<List<String>> splittedInput = splitInput(searchTerm);
        HashMap<WebPage, Double> unorderedHashMap = compileHashMaps(splittedInput);
        List<WebPage> result = unorderedHashMap.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        return result;
    }

    /**
    * Splits the input string by OR operator and then by space (%20).
    * @returnList <List<String>> a List of List of input strings (First level -> OR Logic; Second level -> AND Logic).
    */
    public List<List<String>> splitInput(String searchTerm) {
        var splittedInput = new ArrayList<List<String>>();
        String[] splittedByOr = searchTerm.split("(?i) or ");
        for (String andSentence : splittedByOr) {
            splittedInput.add(Arrays.asList(andSentence.split("%20")));
        }
        return splittedInput;
    }

    /**
    * Compiles multiple hashmaps obtained in createANDLogicMap using mergeORLogicMaps
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
    * Creates a hashmap that relates webpages with their corresponding score.
    * Combines the score of different words by the And logic. (In case a webpage is duplicated it will add the score)
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
    * Merges a list of one or multiple hashmaps into a single hashmap using OR Logic (Keep the max score when a page is duplicated).
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
    * Calculates the score of a word of a webpage.
    * @param  word It is a word from the inverted index.
    * @param  webPage Represents a real webpage crawled stored in a database.
    * @return It returns the score of a searched word for ranking websites by relevance.
    */
    public double getPageScore(Word word, WebPage webPage) {
        return (double) word.getWebPageFrequency(webPage) /(double) word.getTotalFrequency();
    } 

    /**
    * Remove all signs or punctuation marks from an input string, transforms it to lowercase and reduces it to a root form (Stemming).
    * @param  word It is a word from the inverted index.
    * @return Returns the resulting string
    */
    public String cleanWord(String word) {
        var wordTrimmedLowerCase = word.replaceAll("[.,!\\?´¨^*$:;{&¤}/+á¼ï()»î±î¿ä]", "").toLowerCase();
        return stopWords.contains(wordTrimmedLowerCase) ? "" : stemmer.stemWord(wordTrimmedLowerCase);
    }

    // Getters and Setters
    public void setSearchInput(String searchInput) {
        this.originalSearchInput = searchInput;
    }
    public String getSearchInput(){
        return originalSearchInput;
    }
    public Indexer getIndexer(){
        return indexer;
    }
}
