package guc.vonneumann.simulator;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.view.DisplayProgram;

public class CPU {

    private int[] registerFile;
    private int pc;
    private int clockCycles;
    private IFIDPipelineRegister ifidPipelineRegister;
    private IDEXPipelineRegister idexPipelineRegister;
    private EXMEMPipelineRegister exmemPipelineRegister;
    private MEMWBPipelineRegister memwbPipelineRegister;

    private IFIDPipelineRegister nextIFIDPipelineRegister;
    private IDEXPipelineRegister nextIDEXPipelineRegister;
    private EXMEMPipelineRegister nextEXMEMPipelineRegister;
    private MEMWBPipelineRegister nextMEMWBPipelineRegister;

    private boolean nextIFIDPipelineRegisterSet;
    private boolean nextIDEXPipelineRegisterSet;
    private boolean nextEXMEMPipelineRegisterSet;
    private boolean nextMEMWBPipelineRegisterSet;

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
        DisplayProgram.addCycle();
        fetch();
        execute();
        decode();
        memAccess();
        writeBack();

        writePipelineRegisters();
    }

    private void writePipelineRegisters() {
        if (nextIFIDPipelineRegisterSet) {
            ifidPipelineRegister = nextIFIDPipelineRegister;
        }
        if (nextIDEXPipelineRegisterSet) {
            idexPipelineRegister = nextIDEXPipelineRegister;
        }
        if (nextEXMEMPipelineRegisterSet) {
            exmemPipelineRegister = nextEXMEMPipelineRegister;
        }
        if (nextMEMWBPipelineRegisterSet) {
            memwbPipelineRegister = nextMEMWBPipelineRegister;
        }

        nextIFIDPipelineRegister = null;
        nextIDEXPipelineRegister = null;
        nextEXMEMPipelineRegister = null;
        nextMEMWBPipelineRegister = null;
        nextIFIDPipelineRegisterSet = false;
        nextIDEXPipelineRegisterSet = false;
        nextEXMEMPipelineRegisterSet = false;
        nextMEMWBPipelineRegisterSet = false;
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
        DisplayProgram.setPhase("IF");
        int pc = getPc();
        int instruction = Computer.getRam().getInstruction(pc);
        if (instruction == 0) {
            return;
        }
        nextIFIDPipelineRegister = new IFIDPipelineRegister(pc, instruction);
        nextIFIDPipelineRegisterSet = true;
        pc++;
        this.pc = pc;
        DisplayProgram.addWrite("PC", pc - 1, pc);
    }

    public void decode() throws SimulatorRuntimeException {
        if (ifidPipelineRegister == null) {
            return;
        }
        Integer instruction = ifidPipelineRegister.getInstruction();
        if (instruction == null) {
            return;
        }
        DisplayProgram.setPhase("ID");
        if (ifidPipelineRegister.getNext() == null) {
            DisplayProgram.addInitialValue("PC", ifidPipelineRegister.getPc());
            DisplayProgram.addInitialValueHex("Ins", ifidPipelineRegister.getInstruction());
            ifidPipelineRegister.setNext(decode(ifidPipelineRegister));
            DisplayProgram.setPhaseCycles(2);
        } else {
            nextIDEXPipelineRegister = ifidPipelineRegister.getNext();
            nextIDEXPipelineRegisterSet = true;
            if (!nextIFIDPipelineRegisterSet) {
                nextIFIDPipelineRegister = null;
                nextIFIDPipelineRegisterSet = true;
            }

            DisplayProgram.setPhaseCycles(0);
        }

    }

    public void execute() throws SimulatorRuntimeException {
        if (idexPipelineRegister == null) {
            return;
        }
        DisplayProgram.setPhase("EX");
        if (idexPipelineRegister.getNext() != null) {
            nextEXMEMPipelineRegister = idexPipelineRegister.getNext();
            nextEXMEMPipelineRegisterSet = true;
            if (!nextIDEXPipelineRegisterSet) {
                nextIDEXPipelineRegister = null;
                nextIDEXPipelineRegisterSet = true;
            }
            DisplayProgram.setPhaseCycles(0);
            return;
        }
        DisplayProgram.addInitialValue("PC", idexPipelineRegister.getPc());
        DisplayProgram.addInitialValueOpCode(idexPipelineRegister.getOpCode());
        DisplayProgram.addInitialValue("R2", idexPipelineRegister.getR2Value());
        DisplayProgram.addInitialValue("R3", idexPipelineRegister.getR3Value());
        DisplayProgram.addInitialValue("IMM", idexPipelineRegister.getImmediate());
        DisplayProgram.addInitialValue("Dest", idexPipelineRegister.getDestinationRegister());
        int opCode = idexPipelineRegister.getOpCode();
        int pc = idexPipelineRegister.getPc();
        EXMEMPipelineRegister next = new EXMEMPipelineRegister(pc);
        switch (opCode) {
            case OpCodes.ADD:
                next.setResult(idexPipelineRegister.getR2Value() + idexPipelineRegister.getR3Value());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.SUB:
                next.setResult(idexPipelineRegister.getR2Value() - idexPipelineRegister.getR3Value());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.MUL:
                next.setResult(idexPipelineRegister.getR2Value() * idexPipelineRegister.getR3Value());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.MOVI:
                next.setResult(idexPipelineRegister.getImmediate());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.JEQ:
                if (idexPipelineRegister.getR2Value() == idexPipelineRegister.getR3Value()) {
                    next.setResult(idexPipelineRegister.getPc() + idexPipelineRegister.getImmediate() + 1);
                    next.setWriteBack(true);
                    next.setDestinationRegister(-1);
                }
                break;
            case OpCodes.AND:
                next.setResult(idexPipelineRegister.getR2Value() & idexPipelineRegister.getR3Value());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.XORI:
                next.setResult(idexPipelineRegister.getR2Value() ^ idexPipelineRegister.getImmediate());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.JMP:
                next.setResult((idexPipelineRegister.getPc() & 0xf0000000) | idexPipelineRegister.getImmediate());
                next.setWriteBack(true);
                next.setDestinationRegister(-1);
                break;
            case OpCodes.LSL:
                next.setResult(idexPipelineRegister.getR2Value() << idexPipelineRegister.getImmediate());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.LSR:
                next.setResult(idexPipelineRegister.getR2Value() >>> idexPipelineRegister.getImmediate());
                next.setWriteBack(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.MOVR:
                next.setAddress(idexPipelineRegister.getR2Value() + idexPipelineRegister.getImmediate());
                next.setWriteBack(true);
                next.setReadMem(true);
                next.setDestinationRegister(idexPipelineRegister.getDestinationRegister());
                break;
            case OpCodes.MOVM:
                next.setResult(idexPipelineRegister.getR3Value());
                next.setAddress(idexPipelineRegister.getR2Value() + idexPipelineRegister.getImmediate());
                next.setWriteMem(true);
                break;
            default:
                break;
        }
        idexPipelineRegister.setNext(next);
        DisplayProgram.setPhaseCycles(2);
        nextIDEXPipelineRegister = idexPipelineRegister;
        nextIDEXPipelineRegisterSet = true;
    }

    public void memAccess() throws SimulatorRuntimeException {
        if (clockCycles % 2 == 1 || exmemPipelineRegister == null) {
            return;
        }
        DisplayProgram.setPhase("MEM");
        DisplayProgram.addInitialValue("PC", exmemPipelineRegister.getPc());
        DisplayProgram.addInitialValueHex("ADDR", exmemPipelineRegister.getAddress());
        DisplayProgram.addInitialValue("Res", exmemPipelineRegister.getResult());
        DisplayProgram.addInitialValue("Dest", exmemPipelineRegister.getDestinationRegister());
        MEMWBPipelineRegister next = null;
        if (exmemPipelineRegister.isReadMem()) {
            int value = Computer.readMemory(exmemPipelineRegister.getAddress());
            next = new MEMWBPipelineRegister(exmemPipelineRegister.getPc());
            next.setResult(value);
            next.setDestinationRegister(exmemPipelineRegister.getDestinationRegister());
        } else if (exmemPipelineRegister.isWriteMem()) {
            Computer.writeMemory(exmemPipelineRegister.getAddress(), exmemPipelineRegister.getResult());
        } else if (exmemPipelineRegister.isWriteBack()) {
            next = new MEMWBPipelineRegister(exmemPipelineRegister.getPc());
            next.setResult(exmemPipelineRegister.getResult());
            next.setDestinationRegister(exmemPipelineRegister.getDestinationRegister());
        }
        nextMEMWBPipelineRegister = next;
        nextMEMWBPipelineRegisterSet = true;
        if (!nextEXMEMPipelineRegisterSet) {
            nextEXMEMPipelineRegister = null;
            nextEXMEMPipelineRegisterSet = true;
        }
    }

    public void writeBack() {
        if (memwbPipelineRegister == null) {
            return;
        }
        DisplayProgram.setPhase("WB");
        DisplayProgram.addInitialValue("PC", memwbPipelineRegister.getPc());
        DisplayProgram.addInitialValue("Res", memwbPipelineRegister.getResult());
        DisplayProgram.addInitialValue("Dest", memwbPipelineRegister.getDestinationRegister());
        writeRegister(memwbPipelineRegister.getDestinationRegister(), memwbPipelineRegister.getResult());
        if (!nextMEMWBPipelineRegisterSet) {
            nextMEMWBPipelineRegister = null;
            nextMEMWBPipelineRegisterSet = true;
        }
    }

    public int[] getRegisterFile() {
        return registerFile;
    }

    public IDEXPipelineRegister decode(IFIDPipelineRegister ifidPipelineRegister) throws SimulatorRuntimeException {
        int instruction = ifidPipelineRegister.getInstruction();
        int opCode = ifidPipelineRegister.getInstruction() & 0xf0000000;
        opCode = opCode >>> 28;
        int pc = ifidPipelineRegister.getPc();
        IDEXPipelineRegister next = new IDEXPipelineRegister(pc, opCode);
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
                int r3Value = readRegister(r3);

                next.setDestinationRegister(r1);
                next.setR2Value(r2Value);
                next.setR3Value(r3Value);

                int shamt = instruction & 0x00001fff;
                int msb = shamt >>> 12;
                if (msb == 1) {
                    shamt = shamt | 0xfffff000;
                }
                next.setImmediate(shamt);
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

                next.setDestinationRegister(r1);
                next.setR2Value(r2Value);

                int immediate = instruction & 0x0003ffff;
                int msb = immediate >>> 17;
                if (msb == 1) {
                    immediate = immediate | 0xfffe0000;
                }
                next.setImmediate(immediate);
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

                next.setR3Value(r1Value);
                next.setR2Value(r2Value);

                int immediate = instruction & 0x0003ffff;
                int msb = immediate >>> 17;
                if (msb == 1) {
                    immediate = immediate | 0xfffe0000;
                }
                next.setImmediate(immediate);
                break;
            }
            case OpCodes.JMP: {
                int immediate = instruction & 0x0fffffff;
                int msb = immediate >>> 27;
                if (msb == 1) {
                    immediate = immediate | 0xf8000000;
                }
                next.setImmediate(immediate);
                break;
            }
            default:
                throw new SimulatorRuntimeException("Failed to decode instruction");
        }
        return next;
    }

    public int getPc() {
        int result = pc;
        String name = "PC";
        if(exmemPipelineRegister != null && exmemPipelineRegister.getDestinationRegister() == -1){
            result = exmemPipelineRegister.getResult();
            name = "PC(FWD EX/MEM)";
        }
        else if(memwbPipelineRegister != null && memwbPipelineRegister.getDestinationRegister() == -1){
            result = memwbPipelineRegister.getResult();
            name = "PC(FWD MEM/WB)";
        }
        DisplayProgram.addRead(name, result);
        return result;
    }

    public void setPc(int pc) {
        nextIDEXPipelineRegister = null;
        nextIDEXPipelineRegisterSet = true;
        nextEXMEMPipelineRegister = null;
        nextEXMEMPipelineRegisterSet = true;
    }

    public int readRegister(int index) {
        int value = registerFile[index];
        String name = "R" + index;
        if(idexPipelineRegister != null && idexPipelineRegister.getNext() != null && idexPipelineRegister.getNext().getDestinationRegister() == index){
            name = name + "(FWD ID/EX)";
            value = idexPipelineRegister.getNext().getResult();
        }
        else if(exmemPipelineRegister != null && exmemPipelineRegister.getDestinationRegister() == index){
            name = name + "(FWD EX/MEM)";
            value = exmemPipelineRegister.getResult();
        }
        else if(memwbPipelineRegister != null && memwbPipelineRegister.getDestinationRegister() == index){
            name = name + "(FWD MEM/WB)";
            value = memwbPipelineRegister.getResult();
        }
        DisplayProgram.addRead(name , value);
        return value;
    }

    public void writeRegister(int index, int value) {
        if (index == -1) {
            setPc(value);
            return;
        }
        DisplayProgram.addWrite("R" + index, registerFile[index], value);
        registerFile[index] = value;
    }
}
