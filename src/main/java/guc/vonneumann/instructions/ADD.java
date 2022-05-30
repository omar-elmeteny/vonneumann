package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class ADD implements Instruction{
    
    final private int r1;
    final private int r2Value;
    final private int r3Value;
    private int sum;

    public ADD(int r1, int r2, int r3) {
        super();
        this.r1 = r1;
        this.r2Value = Computer.readRegister(r2);
        this.r3Value = Computer.readRegister(r3);
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getR3Value() {
        return r3Value;
    }

    public int getR2Value() {
        return r2Value;
    }

    public int getR1() {
        return r1;
    }

    public void execute(){
        setSum(r2Value + r3Value);
    }

    public void memAccess(){

    }

    public void writeBack(){
        Computer.writeRegister(r1, sum);
    }
}
