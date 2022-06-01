package guc.vonneumann;
import java.io.IOException;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.exceptions.SimulatorSyntaxException;
import guc.vonneumann.simulator.*;
import guc.vonneumann.view.DisplayProgram;
import guc.vonneumann.view.DisplayWindow;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SimulatorSyntaxException, IOException, SimulatorRuntimeException
    {
        CodeCompiler.compileCode(args[0]);
        Computer.getCpu().runProgram();
        DisplayWindow.addDisplayProgram(DisplayProgram.getInstance());
        DisplayWindow.addMemory(Computer.getRam().getMemory());
        DisplayWindow.addRegisters(Computer.getCpu().getRegisterFile(), Computer.getCpu().getPc());
    }
}
