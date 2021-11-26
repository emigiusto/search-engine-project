package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    private List<WebPage> pages = new ArrayList<>();
    private List<String> lines = new ArrayList<>();

    public SearchEngine(String filename) {
        try {
            fetchDatabase(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchDatabase(String filename) throws IOException{
        try {
            lines = Files.readAllLines(Paths.get(filename));
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

    public  List<WebPage> getPages() {
        return pages;
    }

    public List<String> getLines() {
        return lines;
    }


}
