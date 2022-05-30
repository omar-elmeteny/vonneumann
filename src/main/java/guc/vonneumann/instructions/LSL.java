package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class LSL implements Instruction{
    final private int r1;
    final private int r2Value;
    final private int shamt;
    private int shiftLeft;

    public LSL(int r1, int r2, int shamt) {
        super();
        this.r1 = r1;
        this.r2Value = Computer.readRegister(r2);
        this.shamt = shamt;
    }

    public int getShiftLeft() {
        return shiftLeft;
    }

    public void setShiftLeft(int shiftLeft) {
        this.shiftLeft = shiftLeft;
    }

    public int getShamt() {
        return shamt;
    }

    public int getR2Value() {
        return r2Value;
    }

    public int getR1() {
        return r1;
    }

    public void execute(){
        setShiftLeft(r2Value << shamt);
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.writeRegister(r1, shiftLeft);
    }
}
