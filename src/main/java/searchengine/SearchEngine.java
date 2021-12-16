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
    private List<List<String>> splittedInput;

    public SearchEngine(String filename) {
        try {
            indexer = new Indexer(filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<WebPage> search(String searchTerm) {
        long start = System.currentTimeMillis();
        searchInput = searchTerm;
        splittedInput = new ArrayList<>();
        splittingInput();
        HashMap<WebPage, Double> unorderedHashMap = gatherWebpages();
        List<WebPage> result = unorderedHashMap.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
            .map(Map.Entry::getKey)
            .limit(30)
            .collect(Collectors.toList());
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Took " + timeElapsed/1000 + " seconds to run the  search");
        return result;
    }

    public void splittingInput() {
        String[] splittedByOr = searchInput.split("(?i) or ");
    
        for (String andSentence : splittedByOr) {
            splittedInput.add(Arrays.asList(andSentence.split("%20")));
        }
    }

    public HashMap<WebPage, Double> gatherWebpages() {
        List<HashMap<WebPage, Double>> mapsWithOrLogic = new ArrayList<>();
        for (List<String> listJoinedByAnd : splittedInput) {
            mapsWithOrLogic.add(getScoreMapWithANDlogic(listJoinedByAnd));
        }
        return getScoreMapWithORlogic(mapsWithOrLogic);
    }
    
    public HashMap<WebPage,Double> getScoreMapWithANDlogic(List<String> listWithANDogic){
        HashMap<WebPage,Double> mapOfWebPages = new HashMap<>();
        for (String term : listWithANDogic) {
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


    public double getPageScore(Word word, WebPage webPage) {
        return (double) word.getWebPageFrequency(webPage) /(double) word.getTotalFrequency();
    }
}
