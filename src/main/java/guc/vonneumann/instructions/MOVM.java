package guc.vonneumann.instructions;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.simulator.Computer;

public class MOVM implements Instruction{
    final private int r1;
    final private int r2;
    final private int imm;

    public MOVM(int r1, int r2, int imm) {
        super();
        this.r1 = r1;
        this.r2 = r2;
        this.imm = imm;
    }

    public int getImm() {
        return imm;
    }

    public int getR1() {
        return r1;
    }

    public int getR2() {
        return r1;
    }

    public void execute(){

    }

    public void memAccess() throws SimulatorRuntimeException{
        Computer.getRam().writeToMemory(r1, r2 + imm);
    }

    public void writeBack(){
        
    }
}
