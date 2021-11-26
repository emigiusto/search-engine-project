package searchengine;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    private DatabaseHandler database;

    public SearchEngine(String filename) {
        try {
            database = new DatabaseHandler(filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<WebPage> search(String searchTerm) {
        var result = new ArrayList<WebPage>();
        for (var page : database.getPages()) {
            if (page.contains(searchTerm)) {
                result.add(page);
            }
        }
        return result;
    }
}
