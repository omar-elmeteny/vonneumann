package guc.vonneumann.view;

import java.util.ArrayList;

public class DisplayCycle {
    
    private int cycleNumber;
    final private ArrayList<DisplayPhase> phases;
    private DisplayPhase currentPhase;

    public DisplayCycle(int cycleNumber) {
        super();
        this.setCycleNumber(cycleNumber);
        phases = new ArrayList<>();
        phases.add(new DisplayPhase("IF"));
        phases.add(new DisplayPhase("ID"));
        phases.add(new DisplayPhase("EX"));
        phases.add(new DisplayPhase("MEM"));
        phases.add(new DisplayPhase("WB"));
        currentPhase = phases.get(0);
    }

    public DisplayPhase getCurrentPhase() {
        return currentPhase;
    }

    public ArrayList<DisplayPhase> getPhases() {
        return phases;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public void setCurrentPhase(String name) {
        for(DisplayPhase phase : phases){
            if(phase.getPhaseName().equals(name)){
                this.currentPhase = phase;
                return;
            }
        }
    }
}
