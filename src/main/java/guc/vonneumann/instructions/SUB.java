package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class SUB implements Instruction{
    final private int r1;
    final private int r2;
    final private int r3;
    private int difference;

    public SUB(int r1, int r2, int r3) {
        super();
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public int getR3() {
        return r3;
    }

    public int getR2() {
        return r2;
    }

    public int getR1() {
        return r1;
    }

    public void execute(){
        setDifference(r2 - r3);
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.getCpu().getRegisterFile()[r1] = difference;
    }
}
