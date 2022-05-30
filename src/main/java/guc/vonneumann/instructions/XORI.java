package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class XORI implements Instruction{
    final private int r1;
    final private int r2Value;
    final private int imm;
    private int xor;

    public XORI(int r1, int r2, int imm) {
        super();
        this.r1 = r1;
        this.r2Value = Computer.readRegister(r2);
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

    public int getR2Value() {
        return r2Value;
    }

    public void execute(){
        setXor(r2Value ^ imm);
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.writeRegister(r1, xor);
    }
}
