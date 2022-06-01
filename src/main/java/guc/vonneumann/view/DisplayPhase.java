package guc.vonneumann.view;

import java.util.ArrayList;

public class DisplayPhase {
    
    final private ArrayList<String> messages;
    final private String phaseName;
    private int cycles = 1;

    public DisplayPhase(String phaseName) {
        super();
        this.phaseName = phaseName;
        messages = new ArrayList<>();
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
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
