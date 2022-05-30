package guc.vonneumann.instructions;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.simulator.Computer;

public class MOVR implements Instruction{
    final private int r1;
    final private int r2Value;
    final private int imm;
    private int load;

    public MOVR(int r1, int r2, int imm) {
        super();
        this.r1 = r1;
        this.r2Value = Computer.readRegister(r2);
        this.imm = imm;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public int getImm() {
        return imm;
    }

    public int getR1() {
        return r1;
    }

    public int getR2Value() {
        return r2Value;
    }

    public void execute(){

    }

    public void memAccess() throws SimulatorRuntimeException{
        load = Computer.getRam().readFromMemory(r2Value + imm);
    }

    public void writeBack(){
        Computer.writeRegister(r1, load);
    }
}
