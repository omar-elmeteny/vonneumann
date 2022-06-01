package guc.vonneumann.view;

import java.util.ArrayList;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.simulator.OpCodes;

public class DisplayProgram {
    
    static final private String initialColor = "#1591e1";
    static final private String errorColor = "#ff0000";
    static final private String valueColor = "#ffffff";
    static final private String readColor = "#00ff00";
    static final private String writeColor = "#ff00ff";
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
        String html = String.format("<span style='font-weight: bold; color: %s'>%s:</span><span style='color: %s'>%d</span>",
            initialColor, name, valueColor, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addInitialValueOpCode(int value) throws SimulatorRuntimeException{
        String html = String.format("<span style='font-weight: bold; color: %s'>%s:</span><span style='color: %s'>%d(%s)</span>",
            initialColor, "OpCode", valueColor, value, getNameByOpCode(value));
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addInitialValueHex(String name, int value){
        String html = String.format("<span style='font-weight: bold; color: %s'>%s:</span><span style='color: %s'>0x%08x</span>",
            initialColor, name, valueColor, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public ArrayList<DisplayCycle> getCycles() {
        return cycles;
    }

    public static DisplayProgram getInstance() {
        return instance;
    }

    public static void addRead(String name, int value){
        String html = String.format("<span style='font-weight: bold; color: %s'>read %s:</span><span style='color: %s'>%d</span>",
            readColor, name, valueColor, value);
        instance.currentCycle.getCurrentPhase().addNewLine(html);
    }

    public static void addWrite(String name, int oldValue, int newValue){
        String html = String.format("<span style='font-weight: bold; color: %s'>write %s:</span><span style='color: %s'>%d -&gt; %d</span>",
            writeColor, name, valueColor, oldValue, newValue);
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
