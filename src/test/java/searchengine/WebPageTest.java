package searchengine;

import searchengine.dba.WebPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class WebPageTest {

    @Test
    @DisplayName("The class Constructor should asign the first String parameter to the title field and the second to url field")
    void testConstructor() {
        var webPage2 = new WebPage("Example1", "https:www.example.com");
        assertEquals(webPage2.getTitle(),"Example1","Title should be equal to Example1");
        assertEquals(webPage2.getURL(),"https:www.example.com","URL should be equal to https:www.example.com");
    }

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