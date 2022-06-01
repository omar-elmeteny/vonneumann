package guc.vonneumann.simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import guc.vonneumann.exceptions.SimulatorSyntaxException;

/**
 * Compiler
 */
public class CodeCompiler {

    private static ArrayList<String> readCode(String pathname) throws IOException {
        File file = new File(pathname);
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<String> code = new ArrayList<>();
        try {
            String st;
            while ((st = br.readLine()) != null) {
                code.add(st);
            }
            return code;
        } finally {
            br.close();
        }

    }

    public static void compileCode(String pathname) throws IOException, SimulatorSyntaxException {
        ArrayList<String> code = readCode(pathname);
        for (String c : code) {
            c = c.trim();
            if(c.length() == 0){
                continue;
            }
            try {
                compileLine(c);
            } catch (NumberFormatException e) {
                throw new SimulatorSyntaxException("Invalid number: " + e.getMessage());
            }
        }
    }

    private static int getOpCodeByName(String instruction) throws SimulatorSyntaxException {
        switch (instruction) {
            case "ADD":
                return OpCodes.ADD;

            case "SUB":
                return OpCodes.SUB;

            case "MUL":
                return OpCodes.MUL;

            case "MOVI":
                return OpCodes.MOVI;

            case "JEQ":
                return OpCodes.JEQ;

            case "AND":
                return OpCodes.AND;

            case "XORI":
                return OpCodes.XORI;

            case "JMP":
                return OpCodes.JMP;

            case "LSL":
                return OpCodes.LSL;

            case "LSR":
                return OpCodes.LSR;

            case "MOVR":
                return OpCodes.MOVR;

            case "MOVM":
                return OpCodes.MOVM;
        }
        throw new SimulatorSyntaxException("There is no such instruction (" + instruction + ")");

    }

    private static void compileLine(String line) throws SimulatorSyntaxException {
        line = line.trim();
        String[] args = line.split("\\s+");
        if (args.length == 0) {
            return;
        }
        int opcode = getOpCodeByName(args[0]);
        switch (opcode) {
            case OpCodes.ADD:
            case OpCodes.SUB:
            case OpCodes.MUL:
            case OpCodes.AND:
                if (args.length != 4) {
                    throw new SimulatorSyntaxException("Incorrect number of arguments");
                }
                if (!checkRegisterSyntax(args[1]) || !checkRegisterSyntax(args[2]) || !checkRegisterSyntax(args[3])) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else if (!checkDestinationRegisterNumber(args[1].substring(1))
                        || !checkSourceRegisterNumber(args[2].substring(1))
                        || !checkSourceRegisterNumber(args[3].substring(1))) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else {
                    int r1 = Integer.parseInt(args[1].substring(1));
                    int r2 = Integer.parseInt(args[2].substring(1));
                    int r3 = Integer.parseInt(args[3].substring(1));
                    int shamt = 0;
                    int instruction = (opcode << 28) | (r1 << 23) | (r2 << 18) | (r3 << 13) | shamt;
                    Computer.getRam().addInstruction(instruction);
                }
                break;
            case OpCodes.MOVI:
                if (args.length != 3) {
                    throw new SimulatorSyntaxException("Incorrect number of arguments");
                } else if (!checkRegisterSyntax(args[1])) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else if (!checkDestinationRegisterNumber(args[1].substring(1))) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else {
                    try {
                        int immediate = Integer.parseInt(args[2]) & 0x0003ffff;
                        int r1 = Integer.parseInt(args[1].substring(1));
                        int r2 = 0;
                        int instruction = (opcode << 28) | (r1 << 23) | (r2 << 18) | immediate;
                        Computer.getRam().addInstruction(instruction);
                    } catch (NumberFormatException e) {
                        throw new SimulatorSyntaxException(e.getMessage());
                    }
                }
                break;
            case OpCodes.JMP:
                if (args.length != 2) {
                    throw new SimulatorSyntaxException("Incorrect number of arguments");
                } else {
                    try {
                        int address = Integer.parseInt(args[1]) & 0x0fffffff;
                        int instruction = (opcode << 28) | address;
                        Computer.getRam().addInstruction(instruction);
                    } catch (NumberFormatException e) {
                        throw new SimulatorSyntaxException(e.getMessage());
                    }
                }
                break;
            case OpCodes.LSL:
            case OpCodes.LSR:
                if (args.length != 4) {
                    throw new SimulatorSyntaxException("Incorrect number of arguments");
                } else if (!checkRegisterSyntax(args[1]) || !checkRegisterSyntax(args[2])) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else if (!checkDestinationRegisterNumber(args[1].substring(1))
                        || !checkSourceRegisterNumber(args[2].substring(1))) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else {
                    try {
                        int shiftAmount = Integer.parseInt(args[3]) & 0x00001fff;
                        int r1 = Integer.parseInt(args[1].substring(1));
                        int r2 = Integer.parseInt(args[2].substring(1));
                        int r3 = 0;
                        int instruction = (opcode << 28) | (r1 << 23) | (r2 << 18) | (r3 << 13) | shiftAmount;
                        Computer.getRam().addInstruction(instruction);
                    } catch (NumberFormatException e) {
                        throw new SimulatorSyntaxException(e.getMessage());
                    }
                }
                break;
            case OpCodes.MOVM:
            case OpCodes.JEQ:
                if (args.length != 4) {
                    throw new SimulatorSyntaxException("Incorrect number of arguments");
                } else if (!checkRegisterSyntax(args[1]) || !checkRegisterSyntax(args[2])) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else if (!checkSourceRegisterNumber(args[1].substring(1))
                        || !checkSourceRegisterNumber(args[2].substring(1))) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else {
                    try {
                        int immediate = Integer.parseInt(args[3]) & 0x0003ffff;
                        int r1 = Integer.parseInt(args[1].substring(1));
                        int r2 = Integer.parseInt(args[2].substring(1));
                        int instruction = (opcode << 28) | (r1 << 23) | (r2 << 18) | immediate;
                        Computer.getRam().addInstruction(instruction);
                    } catch (NumberFormatException e) {
                        throw new SimulatorSyntaxException(e.getMessage());
                    }
                }
                break;
            case OpCodes.MOVR:
            case OpCodes.XORI:
                if (args.length != 4) {
                    throw new SimulatorSyntaxException("Incorrect number of arguments");
                } else if (!checkRegisterSyntax(args[1]) || !checkRegisterSyntax(args[2])) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else if (!checkDestinationRegisterNumber(args[1].substring(1))
                        || !checkSourceRegisterNumber(args[2].substring(1))) {
                    throw new SimulatorSyntaxException("Incorrect register used");
                } else {
                    try {
                        int immediate = Integer.parseInt(args[3]) & 0x0003ffff;
                        int r1 = Integer.parseInt(args[1].substring(1));
                        int r2 = Integer.parseInt(args[2].substring(1));
                        int instruction = (opcode << 28) | (r1 << 23) | (r2 << 18) | immediate;
                        Computer.getRam().addInstruction(instruction);
                    } catch (NumberFormatException e) {
                        throw new SimulatorSyntaxException(e.getMessage());
                    }
                }
                break;
            default:
                throw new SimulatorSyntaxException("There is no such instruction (" + args[0] + ")");
        }
    }

    public static boolean checkRegisterSyntax(String r) {
        if (r.charAt(0) == 'R') {
            return true;
        }
        return false;
    }

    public static boolean checkSourceRegisterNumber(String number) throws SimulatorSyntaxException {
        try {
            int n = Integer.parseInt(number);
            if (n >= 0 && n <= 31) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            throw new SimulatorSyntaxException(e.getMessage());
        }
    }

    public static boolean checkDestinationRegisterNumber(String number) throws SimulatorSyntaxException {
        try {
            int n = Integer.parseInt(number);
            if (n >= 1 && n <= 31) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            throw new SimulatorSyntaxException("Incorrect register used");
        }
    }
}