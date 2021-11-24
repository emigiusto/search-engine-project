package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchEngine {
    private List<List<String>> pages = new ArrayList<>();
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
                pages.add(lines.subList(i, lastIndex));
                lastIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  List<List<String>> getPages() {
        return pages;
    }

    public List<String> getLines() {
        return lines;
    }


}
