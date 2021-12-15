package searchengine;

import searchengine.dba.WebPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class WebPageTest {
    String title1, title2;
    
    @BeforeEach                                         
    void setUp() {
        title1 = "Example1";
        title2 = "Example2";
    }

    /* Function doesn't exist anymore 
    @Test
    @DisplayName("contains should check if an specific String is part of the content Arraylist")
    
    void testContains() {
        var webPage1 = new WebPage(content1.get(0), "https:www.example.com");
        assertEquals(webPage1.contains("This"),true,"The String 'This' is part of the content ArrayList and should return true");
        assertEquals(webPage1.contains("NotIncluded"),false,"The String 'NotIncluded' is NOT part of the content ArrayList and should return false");
        assertEquals(webPage1.contains("This is"),false,"The String 'This is' is NOT part of the content ArrayList and should return false");
        assertEquals(webPage1.contains("This is"),false,"The Title is NOT part of the content ArrayList and should return false");
    }
    */

    @Test
    @DisplayName("The class Constructor should correctly split the arrayList received as parameter in title and content")
    void testConstructor() {
        var webPage2 = new WebPage(title2, "https:www.example.com");
        assertEquals(webPage2.getTitle(),"Title2","Title should be equal to the first element in the ArrayList provided as parameter");
        assertEquals(webPage2.getURL(),"https:www.example.com","URL should be equal to https:www.example.com");
    }

}
