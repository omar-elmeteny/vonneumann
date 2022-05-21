package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorSyntaxException;

public class RAM {

    private static int[] memory;
    private static int instructionsSize;
    private static int lastInstruction;

    public RAM() {
        super();
        memory = new int[2048];
        instructionsSize = 1024;
        lastInstruction = -1;
    }

    public static void addInstruction(int instruction) throws SimulatorSyntaxException{
        if(instructionsSize - 1 == lastInstruction){
            throw new SimulatorSyntaxException("RAM overflow");
        }    
        memory[++lastInstruction] = instruction;   
    }

}
