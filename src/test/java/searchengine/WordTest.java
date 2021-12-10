package searchengine;

import searchengine.dba.WebPage;
import searchengine.dba.Word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class WordTest {
    String title1, title2;
    WebPage webPage1,webPage2;
    
    @BeforeEach                                         
    void setUp() {
        title1 = "Example1";
        title2 = "Example2";

        webPage1 = new WebPage(title1, "https:www.example.com");
        webPage2 = new WebPage(title2, "https:www.example2.com");
    }

    @Test
    @DisplayName("addOcurrence should add 1 to the frequency of the webpage passed in the parameters")
    void testAddOcurrence() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }

    @Test
    @DisplayName("getAllFrequencies should return the complete HashMap of a Word")
    void testGetAllFrequencies() {
        var word = new Word("example", webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getAllFrequencies().size(),1,"Size of the hashmap of a Word with only 1 WebPage should be 1");
        word.addOcurrence(webPage2);
        assertEquals(word.getAllFrequencies().size(),2,"Size of the hashmap of a Word with 2 different webPages should be 2");
        
        var testHashMap = new HashMap<WebPage,Integer>(); 
        testHashMap.put(webPage1,2);
        testHashMap.put(webPage2,1);
        assertEquals(word.getAllFrequencies(),testHashMap,"Frequencies Hashmap of the Word should have 2 WebPage1 and 1 WebPage2");
    }

    @Test
    @DisplayName("getAllWebPages should return a set with all WebPages in the Word")
    void testGetAllWebPages() {
        var word = new Word("example", webPage1);
        var testHashSet = new HashSet<WebPage>(); 
        testHashSet.add(webPage1);
        assertEquals(word.getAllWebPages().size(),1,"Size of the Set of a Word with only 1 WebPage should be 1");
        assertEquals(word.getAllWebPages(),testHashSet,"The set of a new Word with webPage1 in the constructor should be a 1 element set with webPage1 inside");
    
        var testHashSet2 = new HashSet<WebPage>(); 
        word.addOcurrence(webPage2);
        word.addOcurrence(webPage2);
        word.addOcurrence(webPage1);
        testHashSet2.add(webPage1);
        testHashSet2.add(webPage2);
        assertEquals(word.getAllWebPages().size(),2,"Size of the Set of a Word with 2 WebPage should be 2");
        assertEquals(word.getAllWebPages(),testHashSet2,"The set of a new Word with webPage1 in the constructor and webPage2 added afterwards should be a 2-element set with webPage1 and webPage2 inside");
    }


    @Test
    @DisplayName("getTotalFrequency should return an integer with the sum of all webPage frequencies")
    void testGetTotalFrequency() {
        var word = new Word("example", webPage1);
        assertEquals(word.getTotalFrequency(),1,"TotalFrequency of a Word with only 1 webPage1 should be 1");
        word.addOcurrence(webPage2);
        word.addOcurrence(webPage2);
        word.addOcurrence(webPage1);
        assertEquals(word.getTotalFrequency(),4,"TotalFrequency of a Word with only 2 webPage1 and 2 webPage2 should be 4");
    }


}
