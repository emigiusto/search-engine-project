package searchengine;

import java.util.ArrayList;
import java.util.List;

import searchengine.dba.Indexer;
import searchengine.dba.WebPage;

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
        var result = new ArrayList<WebPage>(indexer.getWord(searchTerm).getAllWebPages());
        return result;
    }
}
