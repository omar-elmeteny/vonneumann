package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class JEQ implements Instruction{
    final private int r1;
    final private int r2;
    final private int imm;

    public JEQ(int r1, int r2, int imm) {
        super();
        this.r1 = r1;
        this.r2 = r2;
        this.imm = imm;
    }

    public int getImm() {
        return imm;
    }

    public int getR2() {
        return r2;
    }

    public int getR1() {
        return r1;
    }

    public void execute(){
        if(r1 == r2){
            int pc = Computer.getCpu().getPc();
           Computer.getCpu().setPc(pc + imm - 1);
        }
    }

    public void memAccess(){

    }

    public void writeBack(){
        
    }
}
