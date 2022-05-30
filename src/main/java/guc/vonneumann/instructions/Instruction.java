package guc.vonneumann.instructions;

import guc.vonneumann.exceptions.SimulatorRuntimeException;

public interface Instruction {
    
    void execute();
    void memAccess() throws SimulatorRuntimeException;
    void writeBack();
}
