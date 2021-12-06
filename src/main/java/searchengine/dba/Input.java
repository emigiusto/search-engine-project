package searchengine.dba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Input {
    private String originalInput;
    private List<String> originalInputList;
    private List<String> inputList;
    private List<String> stemmedWords;
    private List<String> stopWords;
    private Stemmer stemmer = new Stemmer();

    public Input(String input){
        originalInput = input;
        try {
            stopWords = Files.readAllLines(Paths.get("data/stop-words.txt"));
            split();
            trim();
            lowerCase();
            removeStopWords();
            stemm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void split() {
        inputList = Arrays.asList(originalInput.split("\\s+"));
        originalInputList = Arrays.asList(originalInput.split("\\s+"));
    }

    public void trim() {
        inputList = inputList.stream().map(string -> string.replaceAll("[.,!?´¨^*:;}{&¤}]+", "")).collect(Collectors.toList());
    }

    public void lowerCase() {
        inputList = inputList.stream().map(string -> string.toLowerCase()).collect(Collectors.toList());
    }

    public void removeStopWords() {
        inputList = inputList.stream().filter(string -> stopWords.contains(string)).collect(Collectors.toList());
    }

    public void stemm() {
        stemmedWords =  inputList.stream().map(string -> stemmer.stemWord(string)).collect(Collectors.toList());
    }

    //Getters
    public List<String> getInputList() {
        return inputList;
    }

    public List<String> getStemmedWords() {
        return stemmedWords;
    }

    public List<String> getStopWords() {
        return stopWords;
    }

    public String getOriginalInput() {
        return originalInput;
    }

    public List<String> getOriginalInputList() {
        return originalInputList;
    }
}