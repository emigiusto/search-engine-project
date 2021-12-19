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
    SearchEngine searchEngine,searchEngineMock;
    
    @BeforeEach                                         
    void setUp() {
        searchEngine = new SearchEngine("data/enwiki-tiny.txt");
        searchEngineMock = new SearchEngine("data/enwiki-mockdba.txt");
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
        WebPage testWebPage2 = new WebPage("Danish universities2", "www.wikipedia.com2");
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
    @DisplayName("createANDLogicMap should return a HashMap with all webpages related to each of the input strings and their corresponding score using AND logic")
    void testCreateANDLogicMap() {
        WebPage colombiaPage = new WebPage("title", "url");
        WebPage ironPage = new WebPage("title", "url");

        List<String> testList = new ArrayList<String>();
        testList.add("colombia");
        testList.add("iron");

        //Case Colombia Iron -> mockdba.txt database
        Word colombiaWord = searchEngineMock.getIndexer().getWord("colombia");
        List<WebPage> webPageList = new ArrayList<WebPage>();
        webPageList.addAll(colombiaWord.getAllWebPages());
        for (WebPage webPage : webPageList) {
            if (webPage.getTitle().equals("ColombiaTitle")) {
                colombiaPage = webPage;
            }
            if (webPage.getTitle().equals("IronTitle")) {
                ironPage = webPage;
            }
        }

        assertEquals(searchEngineMock.createANDLogicMap(testList).get(colombiaPage), 0.75); //0.75 from Colombia Word + 0 from Iron Word
        assertEquals(searchEngineMock.createANDLogicMap(testList).get(ironPage), 1.25); //1 from Iron Word + 0.25 from Colombia Word
    }

    @Test
    @DisplayName("mergeORLogicMaps should return a single HashMap from one or multiple HashMaps as input, keeping the maximun value when finding duplicate entries")
    void testMergeORLogicMaps() {
        WebPage testWebPage1 = new WebPage("Title1", "URL1");
        WebPage testWebPage2 = new WebPage("Title2", "URL2");
        var hashmap1 = new HashMap<WebPage,Double>();
            hashmap1.put(testWebPage1, (double) 8);
            hashmap1.put(testWebPage2, (double) 1);
        var hashmap2 = new HashMap<WebPage,Double>();
            hashmap2.put(testWebPage1, (double) 1);
            hashmap2.put(testWebPage2, (double) 10);
        //Create Input List
        var inputHashmapList = new ArrayList<HashMap<WebPage,Double>>();
            inputHashmapList.add(hashmap1);
            inputHashmapList.add(hashmap2);
        //Create expected hashmap with Maximun values
        var expectedHashmap = new HashMap<WebPage,Double>();
            expectedHashmap.put(testWebPage1,(double) 8);
            expectedHashmap.put(testWebPage2, (double) 10);

        assertEquals(searchEngine.mergeORLogicMaps(inputHashmapList),expectedHashmap);
    }

    @Test
    @DisplayName("cleanWord should return a stemmed lowercase version of the input word with all surrounding signs removed")
    void testCleanWord() {
        String input1 = ",,between!";
        String input2 = "beliEVed";
        String input3 = "peacefuL";
        String input4 = "!Misleading";
        assertEquals(searchEngine.cleanWord(input1),"between");
        assertEquals(searchEngine.cleanWord(input2),"believ");
        assertEquals(searchEngine.cleanWord(input3),"peac");
        assertEquals(searchEngine.cleanWord(input4),"mislead");
    }

    @Test
    @DisplayName("compileHashMaps should the final hashmap with AND and OR logic operated in the values")
    void testCompileHashMaps() {
        //searchInput is "colombia notarealword or iron"
        List<String> testList1 = new ArrayList<String>();
            testList1.add("colombia");
            testList1.add("notarealword");
        List<String> testList2 = new ArrayList<String>();
            testList2.add("iron");
        List<List<String>> testListListInput = new ArrayList<List<String>>();
            testListListInput.add(testList1);
            testListListInput.add(testList2);
        
        //Case Colombia Iron -> mockdba.txt database
        Word colombiaWord = searchEngineMock.getIndexer().getWord("colombia");
        List<WebPage> webPageList = new ArrayList<WebPage>();
        webPageList.addAll(colombiaWord.getAllWebPages());
        //Selects the only 2 webpages present in the database
        WebPage colombiaPage = new WebPage("title", "url");
        WebPage ironPage = new WebPage("title", "url");
        for (WebPage webPage : webPageList) {
            if (webPage.getTitle().equals("ColombiaTitle")) {
                colombiaPage = webPage;
            }
            if (webPage.getTitle().equals("IronTitle")) {
                ironPage = webPage;
            }
        }
        
        assertEquals(searchEngineMock.compileHashMaps(testListListInput).get(colombiaPage), 1.25);
        assertEquals(searchEngineMock.compileHashMaps(testListListInput).get(ironPage), 1);
    }

    @Test
    @DisplayName("search should return a list of two pages 1)Colombia 2)Iron when searching for 'colombia notarealword or iron'")
    void testSearch() {
        //Case Colombia Iron -> mockdba.txt database
        Word colombiaWord = searchEngineMock.getIndexer().getWord("colombia");
        List<WebPage> webPageList = new ArrayList<WebPage>();
        webPageList.addAll(colombiaWord.getAllWebPages());
        //Selects the only 2 webpages present in the database
        WebPage colombiaPage = new WebPage("title", "url");
        WebPage ironPage = new WebPage("title", "url");
        for (WebPage webPage : webPageList) {
            if (webPage.getTitle().equals("ColombiaTitle")) {
                colombiaPage = webPage;
            }
            if (webPage.getTitle().equals("IronTitle")) {
                ironPage = webPage;
            }
        }

        //Colombia WebPage should be second (Score = 1.25)
        //Iron WebPage should be second (Score = 1)
        assertEquals(searchEngineMock.search("colombia%20notarealword or iron").get(0), colombiaPage);
        assertEquals(searchEngineMock.search("colombia%20notarealword or iron").get(1), ironPage);
    }


    public static void main(String[] args) {
        var searchEngineMock = new SearchEngine("data/enwiki-mockdba.txt");

        WebPage colombiaPage = new WebPage("title", "url");
        WebPage ironPage = new WebPage("title", "url");

        List<String> testList = new ArrayList<String>();
        testList.add("colombia");
        testList.add("iron");

        //Case Colombia Iron -> mockdba.txt database
        Word colombiaWord = searchEngineMock.getIndexer().getWord("colombia");
        List<WebPage> webPageList = new ArrayList<WebPage>();
        webPageList.addAll(colombiaWord.getAllWebPages());
        for (WebPage webPage : webPageList) {
            if (webPage.getTitle().equals("ColombiaTitle")) {
                colombiaPage = webPage;
            }
            if (webPage.getTitle()=="IronTitle") {
                ironPage = webPage;
            }
        }
    }
}

