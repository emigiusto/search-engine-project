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
class SearchEngineTest {
    String filename;
    String searchInput1, searchInput2;

    //String title1, title2;
    //WebPage webPage1,webPage2;
    
    @BeforeEach                                         
    void setUp() {
        //SearchEngine searchengine1 = new SearchEngine("Example one");
        //SearchEngine searchengine2 = new SearchEngine("Example two");
    }
    
    @Test
    @DisplayName("splittingInput should split the input %20 between words")
    void testsplittingInput() {
        
        var searchengine1 = new SearchEngine("data/enwiki-tiny.txt");
        searchengine1.setSearchInput("Danish%20university");
        searchengine1.splittingInput();
        List<List<String>> testList = new ArrayList();
        List<String> testList2 = new ArrayList();
        testList2.add("Danish");
        testList2.add("university");
        testList.add(testList2);
        assertEquals(searchengine1.getSplittedInput(),testList);
    }

    @Test
    @DisplayName("splittingInput should split the input by the Or operator between words")
    void testsplittingInput2() {
        
        var searchengine1 = new SearchEngine("data/enwiki-tiny.txt");
        searchengine1.setSearchInput("Danish or university");
        searchengine1.splittingInput();
        List<List<String>> testList = new ArrayList();
        List<String> testList2 = new ArrayList();
        List<String> testList3 = new ArrayList();
        testList2.add("Danish");
        testList3.add("university");
        testList.add(testList2);
        testList.add(testList3);
        assertEquals(searchengine1.getSplittedInput(),testList);
    }


    @Test
    @DisplayName("getPageScore should calculate the score of the input")
    void testgetPageScore() {
        
        

    }

    @Test
    @DisplayName("getScoreMapWithANDlogic should calculate score of input without 'or' operators and put in List")
    void testgetScoreMapWithANDlogic() {
       
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

