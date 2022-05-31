package guc.vonneumann.simulator;


public class EXMEMPipelineRegister {

    final private int pc;
    private int result;
    private int address;
    private int destinationRegister;
    private boolean writeMem;
    private boolean readMem;
    private boolean writeBack;

    public EXMEMPipelineRegister(int pc) {
        this.pc = pc;
    }

    public boolean isWriteBack() {
        return writeBack;
    }

    public void setWriteBack(boolean writeBack) {
        this.writeBack = writeBack;
    }

    public boolean isReadMem() {
        return readMem;
    }

    public void setReadMem(boolean readMem) {
        this.readMem = readMem;
    }

    public boolean isWriteMem() {
        return writeMem;
    }

    public void setWriteMem(boolean writeMem) {
        this.writeMem = writeMem;
    }

    public int getDestinationRegister() {
        return destinationRegister;
    }

    public void setDestinationRegister(int destinationRegister) {
        this.destinationRegister = destinationRegister;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
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

}
