package searchengine.dba;

import java.util.HashMap;
import java.util.Set;

public class Word {
    private HashMap<WebPage,Integer> webPagesFrequency = new HashMap<WebPage,Integer>(); 
    private String term;

    public Word(String term, WebPage webPage){
        this.term = term;
        addOcurrence(webPage);
    }

    public void addOcurrence(WebPage webPage){
        if (webPagesFrequency.containsKey(webPage)) {
            var currentFrequency = webPagesFrequency.get(webPage);
            webPagesFrequency.put(webPage, currentFrequency+1);
        } else {
            webPagesFrequency.put(webPage, 1);
        }
    }

    public int getWebPageFrequency(WebPage webPage){
        if (webPagesFrequency.containsKey(webPage)) {
            return webPagesFrequency.get(webPage);
        } else {
            return 0;
        }
    }

    public HashMap<WebPage,Integer> getAllFrequencies(){
        return webPagesFrequency;
    }

    public Set<WebPage> getAllWebPages(){
        return webPagesFrequency.keySet();
    }

    public String getTerm(){
        return term;
    }

    public int getTotalFrequency(){
        return webPagesFrequency.values().stream().reduce(0, Integer::sum);
    }
}
