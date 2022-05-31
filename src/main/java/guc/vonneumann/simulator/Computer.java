package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorRuntimeException;

public class Computer {
    
    final static private CPU cpu = new CPU();
    final static private RAM ram = new RAM();

    public static RAM getRam() {
        return ram;
    }

    public static CPU getCpu() {
        return cpu;
    }

    public static int readRegister(int index) {
        int value = cpu.getRegisterFile()[index];
        return value;
    }

    public static void writeRegister(int index, int value) {
        cpu.getRegisterFile()[index] = value;
    }
    
    public static int readMemory(int address) throws SimulatorRuntimeException{
        return ram.readFromMemory(address);
    }

    public static void writeMemory(int address, int value) throws SimulatorRuntimeException{
        ram.writeToMemory(value, address);
    }
}
