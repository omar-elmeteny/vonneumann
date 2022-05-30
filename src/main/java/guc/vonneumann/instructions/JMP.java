package guc.vonneumann.instructions;

import guc.vonneumann.simulator.Computer;

public class JMP implements Instruction{
    final private int address;

    public JMP(int address) {
        super();
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public void execute(){
        int pc = Computer.getCpu().getPc();
        pc = pc & 0xf0000000;
        Computer.getCpu().setPc(pc | address);
    }

    public void memAccess(){

    }

    public void writeBack(){
        
    }
}
