package searchengine;

import searchengine.dba.WebPage;
import searchengine.dba.Word;

import static org.junit.Assert.assertTrue;
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
class WebPageTest {
    List<String> content1, content2;
    
    @BeforeEach                                         
    void setUp() {
        content1 = new ArrayList<String>(); 
        content1.add("Title Example1");
        content1.add("This");
        content1.add("is");
        content1.add("a");
        content1.add("test");
        content2 = new ArrayList<String>(); 
        content2.add("Title2");
        content2.add("The");
        content2.add("second");
        content2.add("test");

    }

    @Test
    @DisplayName("contains should check if an specific String is part of the content Arraylist")
    void testContains() {
        var webPage1 = new WebPage(content1, "https:www.example.com");
        assertEquals(webPage1.contains("This"),true,"The String 'This' is part of the content ArrayList and should return true");
        assertEquals(webPage1.contains("NotIncluded"),false,"The String 'NotIncluded' is NOT part of the content ArrayList and should return false");
        assertEquals(webPage1.contains("This is"),false,"The String 'This is' is NOT part of the content ArrayList and should return false");
        assertEquals(webPage1.contains("This is"),false,"The Title is NOT part of the content ArrayList and should return false");
    }

    @Test
    @DisplayName("The class Constructor should correctly split the arrayList received as parameter in title and content")
    void testConstructor() {
        var webPage2 = new WebPage(content2, "https:www.example.com");
        var testContent = new ArrayList<String>(); 
        testContent.add("The");
        testContent.add("second");
        testContent.add("test");
        assertEquals(webPage2.getTitle(),"Title2","Title should be equal to the first element in the ArrayList provided as parameter");
        assertEquals(webPage2.getContent(),testContent,"Content should be equal to an ArrayList with all other elements provided as parameter except for the first one");
    }

}
