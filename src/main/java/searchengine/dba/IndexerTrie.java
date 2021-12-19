package searchengine.dba;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class IndexerTrie {
    private Trie indexer;

    public IndexerTrie(String filename){
        indexer = new Trie();
        try {
            long start = System.currentTimeMillis();
            fetchDatabase(filename);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Took " + timeElapsed/1000 + " seconds to complete the Index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /** Loads the database into the trie data structure
     * @param filename
     * @throws IOException
     */
    public void fetchDatabase(String filename) throws IOException{
        try(BufferedReader bufferReader = new BufferedReader(new FileReader(filename))) 
        {
            String line = bufferReader.readLine();
            int pageCounter = 0;
            while(line != null) {
                if (line.startsWith("*PAGE")) {
                    var nextLine = bufferReader.readLine();
                    WebPage webPage = new WebPage(nextLine, line.substring(6));
                    boolean condition = (nextLine != null) && (!(nextLine.startsWith("*PAGE")));
                    while(condition){
                        indexer.insert(nextLine, webPage);
                        nextLine = bufferReader.readLine();
                        condition = (nextLine != null) && (!(nextLine.startsWith("*PAGE")));
                    }
                    pageCounter++;
                    line = nextLine;
                }
                System.out.println(pageCounter + " pages have been indexed");
            } 
            bufferReader.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    /** searchs a word in the trie
     * @param searchTerm
     * @return Word
     */
    public Word getWord(String searchTerm){
        return indexer.getWord(searchTerm);
    }
}