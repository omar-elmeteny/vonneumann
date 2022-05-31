package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorRuntimeException;

public class CPU {

    private int[] registerFile;
    private int pc;
    private int clockCycles;
    private IFIDPipelineRegister ifidPipelineRegister;
    private IDEXPipelineRegister idexPipelineRegister;
    private EXMEMPipelineRegister exmemPipelineRegister;
    private MEMWBPipelineRegister memwbPipelineRegister;

    public CPU() {
        super();
        registerFile = new int[32];
        pc = 0;
        clockCycles = 0;
    }

    public void runProgram() throws SimulatorRuntimeException {
        while (!isDone()) {
            clockCycles++;
            runCycle();
        }
    }

    public void runCycle() throws SimulatorRuntimeException {
        writeBack();
        memAccess();
        execute();
        decode();
        fetch();
    }

    public boolean isDone() throws SimulatorRuntimeException {
        return (ifidPipelineRegister == null && idexPipelineRegister == null
                && exmemPipelineRegister == null && memwbPipelineRegister == null
                && Computer.getRam().getInstruction(pc) == 0);
    }

    public void fetch() throws SimulatorRuntimeException {
        if (clockCycles % 2 == 0) {
            return;
        }
        int instruction = Computer.getRam().getInstruction(pc);
        if (instruction == 0) {
            return;
        }
        ifidPipelineRegister = new IFIDPipelineRegister(pc, instruction);
        pc++;
    }

    public void decode() throws SimulatorRuntimeException {
        if (ifidPipelineRegister == null) {
            return;
        }
        Integer instruction = ifidPipelineRegister.getInstruction();
        if (instruction == null) {
            return;
        }
        idexPipelineRegister = decode(ifidPipelineRegister);
    }

    public void execute() {
        if (idexPipelineRegister == null) {
            return;
        }
        int opCode = idexPipelineRegister.getOpCode();
        int pc = idexPipelineRegister.getPc();
        exmemPipelineRegister = new EXMEMPipelineRegister(pc);
        switch (opCode) {
            case OpCodes.ADD:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() + idexPipelineRegister.getR3Value());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.SUB:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() - idexPipelineRegister.getR3Value());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.MUL:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() * idexPipelineRegister.getR3Value());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;  
            case OpCodes.MOVI:
                exmemPipelineRegister.setResult(idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break; 
            case OpCodes.JEQ: 
                if(idexPipelineRegister.getR2Value() == idexPipelineRegister.getR3Value()){
                    exmemPipelineRegister.setResult(idexPipelineRegister.getPc() + idexPipelineRegister.getImmediate() + 1);
                    exmemPipelineRegister.setWriteBack(true);
                    exmemPipelineRegister.setDestinationRegister(-1);
                } 
                break;
            case OpCodes.AND:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() & idexPipelineRegister.getR3Value());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;  
            case OpCodes.XORI:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() ^ idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.JMP: 
                exmemPipelineRegister.setResult((idexPipelineRegister.getPc() & 0xf0000000) | idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(-1);
                break;
            case OpCodes.LSL:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() << idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.LSR:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR2Value() >>> idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break; 
            case OpCodes.MOVR:
                exmemPipelineRegister.setAddress(idexPipelineRegister.getR2Value() + idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteBack(true);
                exmemPipelineRegister.setReadMem(true);
                exmemPipelineRegister.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break; 
            case OpCodes.MOVM:
                exmemPipelineRegister.setResult(idexPipelineRegister.getR3Value());
                exmemPipelineRegister.setAddress(idexPipelineRegister.getR2Value() + idexPipelineRegister.getImmediate());
                exmemPipelineRegister.setWriteMem(true);
                break;                   
            default:
                break;
        }

    }

    public void memAccess() throws SimulatorRuntimeException {
        if (clockCycles % 2 == 1 || exmemPipelineRegister == null) {
            return;
        }
        if (exmemPipelineRegister.isReadMem()) {
            int value = Computer.readMemory(exmemPipelineRegister.getAddress());
            memwbPipelineRegister = new MEMWBPipelineRegister(exmemPipelineRegister.getPc());
            memwbPipelineRegister.setResult(value);
            memwbPipelineRegister.setDestinationRegister(exmemPipelineRegister.getDestinationRegister());
        } else if (exmemPipelineRegister.isWriteMem()) {
            Computer.writeMemory(exmemPipelineRegister.getAddress(), exmemPipelineRegister.getResult());
            memwbPipelineRegister = null;
        } else if (exmemPipelineRegister.isWriteBack()) {
            memwbPipelineRegister = new MEMWBPipelineRegister(exmemPipelineRegister.getPc());
            memwbPipelineRegister.setResult(exmemPipelineRegister.getResult());
            memwbPipelineRegister.setDestinationRegister(exmemPipelineRegister.getDestinationRegister());
        }
    }

    public void writeBack() {
        if (memwbPipelineRegister == null) {
            return;
        }
        Computer.writeRegister(memwbPipelineRegister.getDestinationRegister(), memwbPipelineRegister.getResult());
    }

    public int[] getRegisterFile() {
        return registerFile;
    }

    public IDEXPipelineRegister decode(IFIDPipelineRegister ifidPipelineRegister) throws SimulatorRuntimeException {
        int instruction = ifidPipelineRegister.getInstruction();
        int opCode = ifidPipelineRegister.getInstruction() & 0xf0000000;
        opCode = opCode >>> 28;
        int pc = ifidPipelineRegister.getPc();
        IDEXPipelineRegister idexPipelineRegister = new IDEXPipelineRegister(pc, opCode);
        switch (opCode) {
            // R-Type
            case OpCodes.ADD:
            case OpCodes.SUB:
            case OpCodes.MUL:
            case OpCodes.AND:
            case OpCodes.LSL:
            case OpCodes.LSR: {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r2Value = readRegister(r2);
                int r3 = instruction & 0x0003e000;
                r3 = r3 >>> 13;
                int r3Value = readRegister(r2);

                idexPipelineRegister.setDestinationRegister(r1);
                idexPipelineRegister.setR2Value(r2Value);
                idexPipelineRegister.setR3Value(r3Value);

                int shamt = instruction & 0x00001fff;
                int msb = shamt >>> 12;
                if (msb == 1) {
                    shamt = shamt | 0xfffff000;
                }
                idexPipelineRegister.setImmediate(shamt);
                break;
            }
            case OpCodes.MOVI:
            case OpCodes.XORI:
            case OpCodes.MOVR: {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r2Value = readRegister(r2);

                idexPipelineRegister.setDestinationRegister(r1);
                idexPipelineRegister.setR2Value(r2Value);

                int immediate = instruction & 0x0003ffff;
                int msb = immediate >>> 17;
                if (msb == 1) {
                    immediate = immediate | 0xfffe0000;
                }
                idexPipelineRegister.setImmediate(immediate);
                break;
            }
            case OpCodes.JEQ:
            case OpCodes.MOVM: {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r1Value = readRegister(r1);
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r2Value = readRegister(r2);

                idexPipelineRegister.setR3Value(r1Value);
                idexPipelineRegister.setR2Value(r2Value);

                int immediate = instruction & 0x0003ffff;
                int msb = immediate >>> 17;
                if (msb == 1) {
                    immediate = immediate | 0xfffe0000;
                }
                idexPipelineRegister.setImmediate(immediate);
                break;
            }
            case OpCodes.JMP: {
                int immediate = instruction & 0x0fffffff;
                int msb = immediate >>> 27;
                if (msb == 1) {
                    immediate = immediate | 0xf8000000;
                }
                idexPipelineRegister.setImmediate(immediate);
                break;
            }
            default:
                throw new SimulatorRuntimeException("Failed to decode instruction");
        }
        return idexPipelineRegister;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int readRegister(int index) {
        int value = registerFile[index];
        return value;
    }

    public void writeRegister(int index, int value) {
        registerFile[index] = value;
    }
}
