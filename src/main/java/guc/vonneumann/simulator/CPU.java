package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorRuntimeException;

public class CPU {
    
    private int[] registerFile;
    private int pc;
    private int clockCycles;

    public void fetch() throws SimulatorRuntimeException{
        int instruction = Computer.getRam().getInstruction(pc++);
        // decode(instruction);
    }

    public int[] getRegisterFile() {
        return registerFile;
    }

    public void decode(int instruction){
        int opcode = instruction & 0xf0000000;
        opcode = opcode >>> 28;
        switch (opcode) {
            case 0:
                
                break;
        
            default:
                break;
        }
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
}
