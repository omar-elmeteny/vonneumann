package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.exceptions.SimulatorSyntaxException;

public class RAM {

    private int[] memory;
    private int instructionsSize;
    private int lastInstruction;

    public RAM() {
        super();
        memory = new int[2048];
        instructionsSize = 1024;
        lastInstruction = -1;
    }

    public void addInstruction(int instruction) throws SimulatorSyntaxException{
        if(lastInstruction >= instructionsSize - 1){
            throw new SimulatorSyntaxException("RAM overflow");
        }    
        memory[++lastInstruction] = instruction;   
    }

    public int getInstruction(int address) throws SimulatorRuntimeException{
        if(address < 0 || address > 1023){
            throw new SimulatorRuntimeException("Wrong address, address must be between 0 and 1023");
        }
        return memory[address];
    }

    public void writeToMemory(int data, int address) throws SimulatorRuntimeException{
        if(address < 1024 || address > 2047){
            throw new SimulatorRuntimeException("Wrong address, address must be between 1024 and 2047");
        }
        memory[address] = data;
    }

    public int readFromMemory(int address) throws SimulatorRuntimeException{
        if(address < 1024 || address > 2047){
            throw new SimulatorRuntimeException("Wrong address, address must be between 1024 and 2047");
        }
        return memory[address];
    }


}
