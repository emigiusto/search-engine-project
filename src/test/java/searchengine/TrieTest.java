package searchengine;

import searchengine.dba.Trie;
import searchengine.dba.WebPage;
import searchengine.dba.Word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class TrieTest {
    String key;
    WebPage webPage;
    Trie trie;
    Word word;
    
    @BeforeEach                                         
    void setUp() {
        key = "example";
        String title = "Example";
        String url = "https:www.example.com";
        trie = new Trie();
        webPage = new WebPage(title, url);
        word = new Word(key, webPage);
    }
    
    @Test
    @DisplayName("The insert method should update the trie with the given key")
    void testInsert(){
        trie.insert(key, webPage);
        assertEquals(key, trie.getWord(key).getTerm());
    }

    @Test 
    @DisplayName("The search method should return true if the key is founded")
    void testTrieSearch(){
        trie.insert(key, webPage);
        assertTrue(trie.search(key));
    }

    @Test 
    @DisplayName("The search method should return false if the key is not founded")
    void testTrieSearchifWordNonExistant(){
        trie.insert(key, webPage);
        assertFalse(trie.search("nonExistant"));
    }
}
