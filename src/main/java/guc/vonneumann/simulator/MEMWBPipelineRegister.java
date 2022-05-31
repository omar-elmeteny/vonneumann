package guc.vonneumann.simulator;

public class MEMWBPipelineRegister {
    
    private int pc;
    private int result;
    private int destinationRegister;

    public MEMWBPipelineRegister(int pc) {
        this.pc = pc;
    }

    public int getDestinationRegister() {
        return destinationRegister;
    }

    public void setDestinationRegister(int destinationRegister) {
        this.destinationRegister = destinationRegister;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
}
