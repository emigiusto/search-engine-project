package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;

  private HttpServer server;
  private SearchEngine searchEngine;

  public WebServer(int port, String filename) throws IOException{
    initializeServer(port);
    searchEngine = new SearchEngine(filename);
  }

    /**
   * <p>This methods initializes a Web server with a given port</p>
   * <p>Defining Routing</p>
   * @param port port where the server will be running
   */
  public void initializeServer(int port){
    try {
      server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
      server.createContext("/", io -> respond(io, 200, "text/html", getFile("web/index.html")));
      server.createContext("/search", io -> search(io));
      server.createContext(
          "/favicon.ico", io -> respond(io, 200, "image/x-icon", getFile("web/favicon.ico")));
      server.createContext(
          "/code.js", io -> respond(io, 200, "application/javascript", getFile("web/code.js")));
      server.createContext(
          "/style.css", io -> respond(io, 200, "text/css", getFile("web/style.css")));
          server.createContext(
          "/autocomplete.txt", io -> respond(io, 200, "text/plain", getFile("web/autocomplete.txt")));
      server.start();
      String msg = " WebServer running on http://localhost:" + port + " ";
      System.out.println("│"+msg+"│");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
  }
  
  public void search(HttpExchange io) {
    var searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
    var response = new ArrayList<String>();
    for (var page : searchEngine.search(searchTerm)) {
      response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",page.getURL(), page.getTitle()));
    }
    var bytes = response.toString().getBytes(CHARSET);
    respond(io, 200, "application/json", bytes);
  }


  public byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  public void respond(HttpExchange io, int code, String mime, byte[] response) {
    try {
      io.getResponseHeaders()
          .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
      io.sendResponseHeaders(200, response.length);
      io.getResponseBody().write(response);
    } catch (Exception e) {
    } finally {
      io.close();
    }
  }

  public HttpServer getServer(){
    return server;
  }
}