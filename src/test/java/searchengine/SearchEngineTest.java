package searchengine;

import searchengine.dba.WebPage;
import searchengine.dba.Word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class SearchEngineTest {
    String title1, title2;
    WebPage webPage1,webPage2;
    
    @BeforeEach                                         
    void setUp() {
        title1 = "Example1";
        title2 = "Example2";

        SearchEngine searchengine1 = new SearchEngine("Test1");
        SearchEngine searchengine2 = new SearchEngine("Test2");
    }

    @Test
    @DisplayName("search should return results from the input in an ranked order")
    void testSearch() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }

    @Test
    @DisplayName("splittingInput should split the input either according to an 'or' operator, or spaces between words")
    void testsplittingInput() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }

    @Test
    @DisplayName("getScoreMapWithANDlogic should calculate score of input without 'or' operators and put in List")
    void testgetScoreMapWithANDlogic() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }

    @Test
    @DisplayName("getScoreMapWithORlogic should calculate score of input without 'or' operators and put in Map")
    void testgetScoreMapWithORlogic() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }

    @Test
    @DisplayName("getPageScore should calculate the score of an input")
    void testgetPageScore() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }
    
    @Test
    @DisplayName("gatherWebpages should gather webpages and scores corresponding to the input ")
    void testgatherWebpages() {
        var word = new Word("example", webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),1,"Frequency of the webPage passed on to the constructor should be 1");
        assertEquals(word.getWebPageFrequency(webPage2),0,"Frequency of a webPage not added to the Word should be 0");
        word.addOcurrence(webPage1);
        word.addOcurrence(webPage1);
        assertEquals(word.getWebPageFrequency(webPage1),3,"Frequency of a webPage added two extra times to the Word should be 0");
    }
    
}

