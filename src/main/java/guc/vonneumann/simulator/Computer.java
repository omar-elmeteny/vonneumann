package guc.vonneumann.simulator;

public class Computer {
    
    static private CPU cpu;
    static private RAM ram;

    public Computer() {
        super();
        setCpu(new CPU());
        setRam(new RAM());
    }

    public static RAM getRam() {
        return ram;
    }

    public static void setRam(RAM ram) {
        Computer.ram = ram;
    }

    public static CPU getCpu() {
        return cpu;
    }

    public static void setCpu(CPU cpu) {
        Computer.cpu = cpu;
    }
    
}
