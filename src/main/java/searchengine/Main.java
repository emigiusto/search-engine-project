package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import searchengine.dba.Input;

public class Main {
    static final int PORT = 8080;
    public static void main(final String... args) throws IOException {
        //var filename = Files.readString(Paths.get("config.txt")).strip();
        //var newServer = new WebServer(PORT, filename);

        var input = new Input(",.fIRst    ..seCoNd,..");
        System.out.println(input);
      }
}
