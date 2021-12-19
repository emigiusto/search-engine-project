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