package searchengine;

import java.util.Arrays;
import java.util.List;

public class WebPage {
    private List<String> content;
    private String url;

    public WebPage(List<String> content, String url){
        this.content = content;
        this.url = url;
    }

    public boolean contains(String SearchTerm){
        return content.contains(SearchTerm);
    }

    public List<String> getContent(){
        return content;
    }
    public String getURL(){
        return url;
    }
}
