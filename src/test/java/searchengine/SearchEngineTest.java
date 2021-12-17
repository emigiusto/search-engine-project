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
    String filename;
    String searchInput1, searchInput2;

    //String title1, title2;
    //WebPage webPage1,webPage2;
    
    @BeforeEach                                         
    void setUp() {
        SearchEngine searchengine1 = new SearchEngine("Example one");
        SearchEngine searchengine2 = new SearchEngine("Example two");
    }

    
    @Test
    @DisplayName("splittingInput should split the input either according to an 'or' operator, or spaces between words")
    void testsplittingInput() {
        
        var searchengine1 = new Searchengine("Splitting the input string");
        assertEquals(searchengine1.splittingInput(searchInput),"Splitting" + "the" + "input" + "string");
        

    }

    @Test
    @DisplayName("getScoreMapWithANDlogic should calculate score of input without 'or' operators and put in List")
    void testgetScoreMapWithANDlogic() {
       

    @Test
    @DisplayName("getScoreMapWithORlogic should calculate score of input without 'or' operators and put in Map")
    void testgetScoreMapWithORlogic() {
   
    }

    @Test
    @DisplayName("getPageScore should calculate the score of the input")
    void testgetPageScore() {
 
    }
    
    @Test
    @DisplayName("gatherWebpages should gather webpages and scores corresponding to the input ")
    void testgatherWebpages() {

    }
    
}

