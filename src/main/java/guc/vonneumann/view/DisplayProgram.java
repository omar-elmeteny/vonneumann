package guc.vonneumann.view;

import java.util.ArrayList;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.simulator.OpCodes;

public class DisplayProgram {
    
    static final private DisplayProgram instance = new DisplayProgram();
    final private ArrayList<DisplayCycle> cycles;
    private DisplayCycle currentCycle = null;
    
    private DisplayProgram() {
        super();
        cycles = new ArrayList<>();
    }

    public static void addCycle() {
        instance.currentCycle = new DisplayCycle(instance.cycles.size() + 1);
        instance.cycles.add(instance.currentCycle);
    }

    public static void setPhase(String phaseName) {
        instance.currentCycle.setCurrentPhase(phaseName);
    }

    public static void addInitialValue(String name, int value){
        if (name == "Dest") {
            String html = String.format("<span class='init'>%s: </span><span class='val'>%s</span>",
            name, value == 0 ? "" : value == -1 ? "PC" : "R" + value);
            instance.currentCycle.getCurrentPhase().addNewLine(html);
            return;    
        }
        String html = String.format("<span class='init'>%s: </span><span class='val'>%d</span>",
            name, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addInitialValueOpCode(int value) throws SimulatorRuntimeException{
        String html = String.format("<span class='init'>%s: </span><span class='val'>%d(%s)</span>",
            "OpCode", value, getNameByOpCode(value));
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addInitialValueHex(String name, int value){
        String html = String.format("<span class='init'>%s: </span><span class='val'>0x%08x</span>",
            name, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public ArrayList<DisplayCycle> getCycles() {
        return cycles;
    }

    public static DisplayProgram getInstance() {
        return instance;
    }

    public static void addRead(String name, int value){
        String html = String.format("<span class='read'>read %s: </span><span class='val'>%d</span>",
            name, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addReadHex(String name, int value){
        String html = String.format("<span class='read'>read %s: </span><span class='val'>%08x</span>",
            name, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addWrite(String name, int oldValue, int newValue){
        String html = String.format("<span class='write'>write %s: </span><span class='val'>%d -&gt; %d</span>",
            name, oldValue, newValue);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    private static String getNameByOpCode(int opcode) throws SimulatorRuntimeException {
        switch (opcode) {
            case OpCodes.ADD:
                return "ADD";

            case OpCodes.SUB:
                return "SUB";

            case OpCodes.MUL:
                return "MUL";

            case OpCodes.MOVI:
                return "MOVI";

            case OpCodes.JEQ:
                return "JEQ";

            case OpCodes.AND:
                return "AND";

            case OpCodes.XORI:
                return "XORI";

            case OpCodes.JMP:
                return "JMP";

            case OpCodes.LSL:
                return "LSL";

            case OpCodes.LSR:
                return "LSR";

            case OpCodes.MOVR:
                return "MOVR";

            case OpCodes.MOVM:
                return "MOVM";
        }
        throw new SimulatorRuntimeException("There is no such instruction (" + opcode + ")");

    }

    public static void setPhaseCycles(int cycles){
        instance.currentCycle.getCurrentPhase().setCycles(cycles);
    }
}
