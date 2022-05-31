package guc.vonneumann.simulator;

public class IFIDPipelineRegister {
    
    private int pc;
    private Integer instruction;

    public IFIDPipelineRegister(int pc, int instruction) {
        super();
        this.setPc(pc);
        this.setInstruction(instruction);
    }

    public Integer getInstruction() {
        return instruction;
    }

    public void setInstruction(Integer instruction) {
        this.instruction = instruction;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
}
