package searchengine.dba;

import java.util.HashMap;
import java.util.Map;

// A class to store a Trie node
public class Trie
{
    private boolean isLeaf;
    private Map<Character, Trie> children;
    Trie leaf;
    private Word wordObject;

    // Constructor
    public Trie()
    {
        isLeaf = false;
        children = new HashMap<>();
        leaf = null;
    }
    
    /** Inserts a key into the trie and creates a webPage for that Node
     * @param key
     * @param webPage
     */
    public void insert(String key, WebPage webPage)
    {        
        // start from the root node
        Trie curr = this;
        // do for each character of the key
        for (char c: key.toCharArray())
        {
            // create a new node if the path doesn't exist
            curr.children.putIfAbsent(c, new Trie());
            // go to the next node
            curr = curr.children.get(c);
        }
        if(curr.isLeaf){
            curr.wordObject.addOcurrence(webPage);
        }else{
            curr.wordObject = new Word(key, webPage);
            // mark the current node as a leaf
            curr.isLeaf = true;
        }
    }

    /** If the key is found on the last node, returns True
     * @param key
     * @return boolean
     */
    // Search a key in a Trie. It returns true if found in the Trie; otherwise, false
    public boolean search(String key)
    { 
        Trie curr = this;
        // do for each character of the key
        for (char c: key.toCharArray())
        {
            // go to the next node
            curr = curr.children.get(c);
            // if the string is invalid (reached end of a path in the Trie)
            if (curr == null) {
                return false;
            }
        }
        // return true if the current node is a leaf node and the end of the string is reached
        leaf = curr;
        return curr.isLeaf;
    }

    /** 
     * @param word
     * @return Word
     */
    public Word getWord(String word){
        Trie curr = this;
        System.out.println("From Trie getting " + word);
        if(curr.search(word)) return leaf.wordObject;
        else return null;
    }
}