package guc.vonneumann.instructions;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.simulator.Computer;

public class MOVM implements Instruction{
    final private int r1Value;
    final private int r2Value;
    final private int imm;

    public MOVM(int r1, int r2, int imm) {
        super();
        this.r1Value = Computer.readRegister(r1);
        this.r2Value = Computer.readRegister(r2);
        this.imm = imm;
    }

    public int getImm() {
        return imm;
    }

    public int getR1() {
        return r1Value;
    }

    public int getR2() {
        return r2Value;
    }

    public void execute(){

    }

    public void memAccess() throws SimulatorRuntimeException{
        Computer.getRam().writeToMemory(r1Value, r2Value + imm);
    }

    public void writeBack(){
        
    }
}
