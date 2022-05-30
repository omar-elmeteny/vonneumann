package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class LSL implements Instruction{
    final private int r1;
    final private int r2;
    final private int shamt;
    private int shiftLeft;

    public LSL(int r1, int r2, int shamt) {
        super();
        this.r1 = r1;
        this.r2 = r2;
        this.shamt = shamt;
    }

    public int getShiftLeft() {
        return shiftLeft;
    }

    public void setSum(int shiftLeft) {
        this.shiftLeft = shiftLeft;
    }

    public int getShamt() {
        return shamt;
    }

    public int getR2() {
        return r2;
    }

    public int getR1() {
        return r1;
    }

    public void execute(){
        setSum(r2 << shamt);
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.getCpu().getRegisterFile()[r1] = shiftLeft;
    }
}
