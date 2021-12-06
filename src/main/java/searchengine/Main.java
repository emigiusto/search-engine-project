package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/** 
   * Configures default webserver port */  

public class Main {
    static final int PORT = 8080;
    public static void main(final String... args) throws IOException {
        var filename = Files.readString(Paths.get("config.txt")).strip();
        new WebServer(PORT, filename);
      }
}
