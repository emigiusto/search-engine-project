package searchengine.dba;

public class WebPage {
    private String url;
    private String title;

    public WebPage(String title, String url){
        this.title = title;
        this.url = url;
    }

    public String getURL(){
        return url;
    }
    public String getTitle(){
        return title;
    }
}
