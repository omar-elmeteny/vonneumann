package guc.vonneumann.simulator;

import java.util.LinkedList;
import java.util.Queue;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.instructions.*;

public class CPU {
    
    private int[] registerFile;
    private int pc;
    private int clockCycles;
    private Queue<Integer> decodeQueue;
    private Queue<Instruction> executeQueue;
    private Queue<Instruction> memoryAccessQueue; 
    private Queue<Instruction> writeBackQueue;

    public CPU() {
        super();
        registerFile = new int[32];
        pc = 0;
        clockCycles = 0;
        decodeQueue = new LinkedList<>();
        executeQueue = new LinkedList<>();
        memoryAccessQueue = new LinkedList<>();
        writeBackQueue = new LinkedList<>();
    }

    public void runProgram() throws SimulatorRuntimeException{
        while(!isDone()){
            clockCycles++;
            runCycle();
        }
    }

    public void runCycle() throws SimulatorRuntimeException{
        writeBack();
        memAccess();
        execute();
        decode();
        fetch();
    }

    public boolean isDone() throws SimulatorRuntimeException{
        return (decodeQueue.size() == 0 && executeQueue.size() == 0 
             && memoryAccessQueue.size() == 0 && writeBackQueue.size() == 0 
             && Computer.getRam().getInstruction(pc) == 0);
    }

    public void fetch() throws SimulatorRuntimeException{
        if(clockCycles%2 == 0){
            return;
        }
        int instruction = Computer.getRam().getInstruction(pc);
        if(instruction == 0){
            return;
        }
        pc++;
        decodeQueue.add(null);
        decodeQueue.add(instruction);
    }

    public void decode() throws SimulatorRuntimeException{
        if(decodeQueue.size() == 0){
            return;
        }
        Integer instruction = decodeQueue.remove();
        if(instruction == null){
            return;
        }
        decode(instruction);
    }

    public void execute(){
        if(executeQueue.size() == 0){
            return;
        }
        Instruction instruction = executeQueue.remove();
        if(instruction == null){
            return;
        }
        instruction.execute();
        memoryAccessQueue.add(instruction);
    }

    public void memAccess() throws SimulatorRuntimeException{
        if(clockCycles%2 == 1 || memoryAccessQueue.size() == 0){
            return;
        }
        Instruction instruction = memoryAccessQueue.remove();
        instruction.memAccess();
        writeBackQueue.add(instruction);
    }

    public void writeBack(){
        if(writeBackQueue.size() == 0){
            return;
        }
        Instruction instruction = writeBackQueue.remove();
        instruction.writeBack();
    }

    public int[] getRegisterFile() {
        return registerFile;
    }

    public void decode(int instruction) throws SimulatorRuntimeException{
        Instruction ins;
        int opcode = instruction & 0xf0000000;
        opcode = opcode >>> 28;
        switch (opcode) {
            case 0:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r3 = instruction & 0x0003e000;
                r3 = r3 >>> 13;
                ins = new ADD(r1, r2, r3);
                break;
            }
            case 1:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r3 = instruction & 0x0003e000;
                r3 = r3 >>> 13;
                ins = new SUB(r1, r2, r3);
                break;
            }
            case 2:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r3 = instruction & 0x0003e000;
                r3 = r3 >>> 13;
                ins = new MUL(r1, r2, r3);
                break;
            }
            case 3:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int imm = instruction & 0x0003ffff;
                ins = new MOVI(r1, imm);
                break;
            }
            case 4:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int imm = instruction & 0x0003ffff;
                ins = new JEQ(r1, r2, imm);
                break;
            }
            case 5:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int r3 = instruction & 0x0003e000;
                r3 = r3 >>> 13;
                ins = new AND(r1, r2, r3);
                break;
            }
            case 6:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int imm = instruction & 0x0003ffff;
                ins = new XORI(r1, r2, imm);
                break;
            }
            case 7:
            {
                int address = instruction & 0x0fffffff;
                ins = new JMP(address);
                break;
            }
            case 8:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int shamt = instruction & 0x00001fff;
                ins = new LSL(r1, r2, shamt);
                break;
            }
            case 9:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int shamt = instruction & 0x00001fff;
                ins = new LSR(r1, r2, shamt);
                break;
            }
            case 10:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int imm = instruction & 0x0003ffff;
                ins = new MOVR(r1, r2, imm);
                break;
            }
            case 11:
            {
                int r1 = instruction & 0x0f800000;
                r1 = r1 >>> 23;
                int r2 = instruction & 0x007c0000;
                r2 = r2 >>> 18;
                int imm = instruction & 0x0003ffff;
                ins = new MOVM(r1, r2, imm);
                break;
            }
            default:
                throw new SimulatorRuntimeException("Failed to decode instruction");
        }
        executeQueue.add(null);
        executeQueue.add(ins);
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        executeQueue.clear();
        decodeQueue.clear();
        this.pc = pc;
    }
}
