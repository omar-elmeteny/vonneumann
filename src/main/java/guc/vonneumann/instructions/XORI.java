package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class XORI implements Instruction{
    final private int r1;
    final private int r2;
    final private int imm;
    private int xor;

    public XORI(int r1, int r2, int imm) {
        super();
        this.r1 = r1;
        this.r2 = r2;
        this.imm = imm;
    }

    public int getXor() {
        return xor;
    }

    public void setXor(int xor) {
        this.xor = xor;
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
        setXor(r2 ^ imm);
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.getCpu().getRegisterFile()[r1] = xor;
    }
}
