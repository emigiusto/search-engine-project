package searchengine.dba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Input {
    private String originalInput;
    private List<String> originalInputList;
    private List<String> inputList;
    private String finalInput;

    public Input(String input){
        originalInput = input;
        split();
        trimInput();
        lowerCase();
    }

    public void split() {
        inputList = Arrays.asList(originalInput.split("\\s+"));
        originalInputList = Arrays.asList(originalInput.split("\\s+"));
    }

    public void trimInput() {
        inputList = inputList.stream().map(string -> string.replaceAll("[.,!?´¨^*:;}{&¤}]+", "")).collect(Collectors.toList());
    }

    public void lowerCase() {
        inputList=inputList.stream().map(string -> string.toLowerCase()).collect(Collectors.toList());
    }

}
