package guc.vonneumann.view;

import java.util.ArrayList;

public class DisplayPhase {
    
    final private ArrayList<String> messages;
    final private String phaseName;

    public DisplayPhase(String phaseName) {
        super();
        this.phaseName = phaseName;
        messages = new ArrayList<>();
    }

    public String getPhaseName() {
        return phaseName;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void addNewLine(String message){
        messages.add(message);
    }

}
