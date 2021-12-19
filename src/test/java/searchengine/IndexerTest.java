package searchengine;

import searchengine.dba.Indexer;
import searchengine.dba.WebPage;
import searchengine.dba.Word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class IndexerTest {
    Indexer indexerMock,indexerMockEmpty;
    WebPage testWebPage1, testWebPage2;
    
    @BeforeEach                                         
    void setUp() {
        indexerMock = new Indexer("data/enwiki-mockdba2.txt");
        indexerMockEmpty = new Indexer("data/enwiki-emptymockdba.txt");
        testWebPage1 = new WebPage("Title1", "www.webpage1.com");
        testWebPage1 = new WebPage("Title2", "www.webpage2.com");
    }
    
    @Test
    @DisplayName("updateInvertedIndex should add every word contained in the webpage received as parameter as new records in the inverted index or ocurrences")
    void testUpdateInvertedIndex() {
        //First case - using testWebPage1
        List<String> contentList1 = new ArrayList<String>();
        contentList1.add("two");
        contentList1.add("look");
        indexerMockEmpty.updateInvertedIndex(testWebPage1, contentList1);
        assertEquals(1,indexerMockEmpty.getInvertedIndex().get("two").getWebPageFrequency(testWebPage1));
        assertEquals(1,indexerMockEmpty.getInvertedIndex().get("look").getWebPageFrequency(testWebPage1));

        //Second case - using testWebPage2
        List<String> contentList2 = new ArrayList<String>();
        contentList2.add("pork");
        contentList2.add("run");
        contentList2.add("run");
        contentList2.add("eat");
        indexerMockEmpty.updateInvertedIndex(testWebPage2, contentList2);
        assertEquals(1,indexerMockEmpty.getInvertedIndex().get("pork").getWebPageFrequency(testWebPage2));
        assertEquals(2,indexerMockEmpty.getInvertedIndex().get("run").getWebPageFrequency(testWebPage2));
        assertEquals(1,indexerMockEmpty.getInvertedIndex().get("eat").getWebPageFrequency(testWebPage2));
        //Old frequencies should remain the same:
        assertEquals(1,indexerMockEmpty.getInvertedIndex().get("two").getWebPageFrequency(testWebPage1));
        assertEquals(1,indexerMockEmpty.getInvertedIndex().get("look").getWebPageFrequency(testWebPage1));
    }

    @Test
    @DisplayName("fetchDatabase index all content in the path of the database provided in the input string")
    void testFetchDatabase() {
        try {
            var indexerMockEmpty2 = new Indexer("data/enwiki-emptymockdba.txt");
            //Starting from an empty indexer, fetchs a mock database
            indexerMockEmpty2.fetchDatabase("data/enwiki-mockdba2.txt");

            //Checks total frequencies
            assertEquals(3,indexerMockEmpty2.getWord("pork").getTotalFrequency());
            assertEquals(1,indexerMockEmpty2.getWord("run").getTotalFrequency());
            assertEquals(1,indexerMockEmpty2.getWord("eat").getTotalFrequency());

            //Checks specific frequencies
            WebPage examplePage1 = indexerMockEmpty2.getWord("run").getAllWebPages().iterator().next();
            WebPage examplePage2 = indexerMockEmpty2.getWord("bat").getAllWebPages().iterator().next();

            assertEquals(1,indexerMockEmpty2.getWord("pork").getWebPageFrequency(examplePage1));
            assertEquals(2,indexerMockEmpty2.getWord("pork").getWebPageFrequency(examplePage2));

            assertEquals(1,indexerMockEmpty2.getWord("run").getWebPageFrequency(examplePage1));
            assertEquals(0,indexerMockEmpty2.getWord("run").getWebPageFrequency(examplePage2));

            assertEquals(1,indexerMockEmpty2.getWord("eat").getWebPageFrequency(examplePage1));
            assertEquals(0,indexerMockEmpty2.getWord("eat").getWebPageFrequency(examplePage2));

            assertEquals(0,indexerMockEmpty2.getWord("bat").getWebPageFrequency(examplePage1));
            assertEquals(1,indexerMockEmpty2.getWord("bat").getWebPageFrequency(examplePage2));

        } catch (IOException e) {
            System.out.println("Error in testInvertedIndex");
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("cleanWord should return a stemmed lowercase version of the input word with all surrounding signs removed. Also if the final word is a stop word, should return null")
    void testCleanWord() {
        String input1 = ",,between!";
        String input2 = "beliEVed";
        String input3 = "peacefuL";
        String input4 = "!Misleading";
        String input5 = "}Of(";
        String input6 = "?aT";
        String input7 = "aND/";
        assertEquals(indexerMockEmpty.cleanWord(input1),"between");
        assertEquals(indexerMockEmpty.cleanWord(input2),"believ");
        assertEquals(indexerMockEmpty.cleanWord(input3),"peac");
        assertEquals(indexerMockEmpty.cleanWord(input4),"mislead");
        assertEquals(indexerMockEmpty.cleanWord(input5),"");
        assertEquals(indexerMockEmpty.cleanWord(input6),"");
        assertEquals(indexerMockEmpty.cleanWord(input7),"");
    }

    public static void main(String[] args) {
        var indexerMockEmpty = new Indexer("data/enwiki-emptymockdba.txt");
        var testWebPage1 = new WebPage("Title1", "www.webpage1.com");
        List<String> contentList = new ArrayList<String>();
        contentList.add("one");
        contentList.add("look");
        indexerMockEmpty.updateInvertedIndex(testWebPage1, contentList);
        Word one = new Word("one",testWebPage1);
        //Word two = new Word("one",testWebPage2);
        var a = indexerMockEmpty.getInvertedIndex().get("castle");
        System.out.println("");

    }
}
