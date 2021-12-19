package searchengine;

import searchengine.dba.WebPage;
import searchengine.dba.Word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class SearchEngineTest {
    String filename;
    String searchInput1, searchInput2;
    SearchEngine searchEngine;
    
    @BeforeEach                                         
    void setUp() {
        searchEngine = new SearchEngine("data/enwiki-tiny.txt");
    }
    
    @Test
    @DisplayName("splitInput should split the input %20 between words")
    void testsplitInput() {
        List<List<String>> testList = new ArrayList<List<String>>();
        List<String> testList2 = new ArrayList<String>();
        testList2.add("Danish");
        testList2.add("university");
        testList.add(testList2);
        assertEquals(searchEngine.splitInput("Danish%20university"),testList);
    }

    @Test
    @DisplayName("splitInput should split the input by the Or operator between words")
    void testsplitInput2() {   
        List<List<String>> testList = new ArrayList<List<String>>();
        List<String> testList2 = new ArrayList<String>();
        List<String> testList3 = new ArrayList<String>();
        testList2.add("Danish");
        testList3.add("university");
        testList.add(testList2);
        testList.add(testList3);
        assertEquals(searchEngine.splitInput("Danish or university"),testList);
    }


    @Test
    @DisplayName("getPageScore should calculate the score of the input")
    void testGetPageScore() {
        WebPage testWebPage = new WebPage("Danish universities", "www.wikipedia.com");
        WebPage testWebPage2 = new WebPage("Danish universities", "www.wikipedia.com");
        Word testWord = new Word("danish", testWebPage);
        //First Case
        testWord.addOcurrence(testWebPage2);
        searchEngine.getPageScore(testWord, testWebPage);
        assertEquals(searchEngine.getPageScore(testWord, testWebPage), 0.5);
        //Second Case
        testWord.addOcurrence(testWebPage2);
        testWord.addOcurrence(testWebPage2);
        assertEquals(searchEngine.getPageScore(testWord, testWebPage), 0.25);
    }

    @Test
    @DisplayName("getScoreMapWithANDlogic should calculate score of input without 'or' operators and put in List")
    void testgetScoreMapWithANDlogic() {
    /*    var searchengine1 = new SearchEngine("data/enwiki-tiny.txt");


        HashMap<WebPage,Double> actual = new HashMap<>();
        
        WebPage testWebPage = new WebPage("Danish universities", "www.wikipedia.com");
        WebPage testWebPage2 = new WebPage("Danish universities", "www.wikipedia.com");
        Word testWord = new Word("danish", testWebPage);
        testWord.addOcurrence(testWebPage2);
        
        searchengine1.getPageScore(testWord, testWebPage);
        assertEquals(searchengine1.getPageScore(testWord, testWebPage), 0.5);

        testWord.addOcurrence(testWebPage2);
        testWord.addOcurrence(testWebPage2);
        assertEquals(searchengine1.getPageScore(testWord, testWebPage), 0.25);*/
    }

    @Test
    @DisplayName("getScoreMapWithORlogic should calculate score of input without 'or' operators and put in Map")
    void testgetScoreMapWithORlogic() {
   
    }
 
    
    @Test
    @DisplayName("gatherWebpages should gather webpages and scores corresponding to the input ")
    void testgatherWebpages() {

    }
    
}

