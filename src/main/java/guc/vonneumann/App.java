package guc.vonneumann;
import java.io.IOException;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.exceptions.SimulatorSyntaxException;
import guc.vonneumann.simulator.*;

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
    }
}
