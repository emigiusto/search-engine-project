package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import searchengine.dba.WebPage;
import searchengine.dba.Word;

public class Main {
    static final int PORT = 8080;
    public static void main(final String... args) throws IOException {
        var filename = Files.readString(Paths.get("config.txt")).strip();
        var newServer = new WebServer(PORT, filename);
      }
}
