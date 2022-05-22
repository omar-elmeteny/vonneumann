package guc.vonneumann.simulator;

public class Computer {
    
    private final ALU alu;
    private final RAM ram;
    private int[] registers;
    private int clockCycles;
    private int pc;

    public Computer() {
        super();
        alu = new ALU();
        ram = new RAM();
        registers = new int[32];
        clockCycles = 1;
        pc = 0;
    }
}
