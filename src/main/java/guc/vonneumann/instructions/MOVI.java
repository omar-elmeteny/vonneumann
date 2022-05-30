package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class MOVI implements Instruction{
    final private int r1;
    final private int imm;

    public MOVI(int r1, int imm) {
        super();
        this.r1 = r1;
        this.imm = imm;
    }

    public int getImm() {
        return imm;
    }

    public int getR1() {
        return r1;
    }

    public void execute(){
        
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.writeRegister(r1, imm);
    }
}
