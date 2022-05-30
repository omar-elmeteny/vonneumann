package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class JEQ implements Instruction{
    final private int r1Value;
    final private int r2Value;
    final private int imm;

    public JEQ(int r1, int r2, int imm) {
        super();
        this.r1Value = Computer.readRegister(r1);
        this.r2Value = Computer.readRegister(r2);
        this.imm = imm;
    }

    public int getImm() {
        return imm;
    }

    public int getR2Value() {
        return r2Value;
    }

    public int getR1Value() {
        return r1Value;
    }

    public void execute(){
        if(r1Value == r2Value){
            int pc = Computer.getCpu().getPc();
            Computer.getCpu().setPc(pc + imm - 1);
        }
    }

    public void memAccess(){

    }

    public void writeBack(){
        
    }
}
