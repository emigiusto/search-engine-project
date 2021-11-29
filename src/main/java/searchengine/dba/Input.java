package searchengine.dba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Input {
    private String originalInput;
    private List<String> inputList;
    private String finalInput;

    public Input(String input){
        originalInput = input;
        split();
        trimInput();
    }

    public void split() {
        inputList = Arrays.asList(originalInput.split(" "));
    }

    public void trimInput() {
        for (String string : inputList) {
            string.trim();
        }
    }

}
