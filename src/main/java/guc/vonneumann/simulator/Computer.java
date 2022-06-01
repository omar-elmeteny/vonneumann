package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.view.DisplayProgram;

public class Computer {
    
    final static private CPU cpu = new CPU();
    final static private RAM ram = new RAM();

    public static RAM getRam() {
        return ram;
    }

    public static CPU getCpu() {
        return cpu;
    }

    public static int readMemory(int address) throws SimulatorRuntimeException{
        DisplayProgram.addRead("MEM[" + address + "]", ram.readFromMemory(address));
        return ram.readFromMemory(address);
    }

    public static void writeMemory(int address, int value) throws SimulatorRuntimeException{
        DisplayProgram.addWrite("MEM[" + address + "]", ram.readFromMemory(address), value);
        ram.writeToMemory(value, address);
    }
}
