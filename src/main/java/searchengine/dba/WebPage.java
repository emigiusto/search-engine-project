package searchengine.dba;

import java.util.List;

public class WebPage {
    //private List<String> content;
    private String url;
    private String title;


    public WebPage(String content, String url){
        this.title = content;
        //this.content = content.subList(1, content.size());
        this.url = url;
    }
    /*
    public boolean contains(String SearchTerm){
        return content.contains(SearchTerm);
    }

    public List<String> getContent(){
        return content;
    }
    */
    public String getURL(){
        return url;
    }
    public String getTitle(){
        return title;
    }
}
