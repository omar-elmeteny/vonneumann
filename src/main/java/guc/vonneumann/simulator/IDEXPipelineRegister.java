package guc.vonneumann.simulator;

public class IDEXPipelineRegister {
    
    private final int pc;
    private int r2Value;
    private int r3Value;
    private int destinationRegister;
    private final int opCode;
    private int immediate;
    private EXMEMPipelineRegister next;
    
    public IDEXPipelineRegister(int pc, int opCode) {
        super();
        this.opCode = opCode;
        this.pc = pc;
    }

    public EXMEMPipelineRegister getNext() {
        return next;
    }

    public void setNext(EXMEMPipelineRegister next) {
        this.next = next;
    }

    public int getOpCode() {
        return opCode;
    }

    public int getImmediate() {
        return immediate;
    }

    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    public int getDestinationRegister() {
        return destinationRegister;
    }

    public void setDestinationRegister(int destinationRegister) {
        this.destinationRegister = destinationRegister;
    }

    public int getR3Value() {
        return r3Value;
    }

    public void setR3Value(int r3Value) {
        this.r3Value = r3Value;
    }

    public int getR2Value() {
        return r2Value;
    }

    public void setR2Value(int r2Value) {
        this.r2Value = r2Value;
    }

    public int getPc() {
        return pc;
    }


}
