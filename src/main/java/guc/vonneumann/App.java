package guc.vonneumann;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.awt.Desktop;

import guc.vonneumann.exceptions.SimulatorRuntimeException;
import guc.vonneumann.exceptions.SimulatorSyntaxException;
import guc.vonneumann.simulator.*;
import guc.vonneumann.view.DisplayProgram;
import guc.vonneumann.view.DisplayHTML;
 

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
        String displayProgram = DisplayHTML.addDisplayProgram(DisplayProgram.getInstance());
        String memory = DisplayHTML.addMemory(Computer.getRam().getMemory());
        String registers = DisplayHTML.addRegisters(Computer.getCpu().getRegisterFile(), Computer.getCpu().getPc());
        File file = new File("output/printings.html");
            try {
                BufferedWriter br = new BufferedWriter(new FileWriter(file));
                try {
                    br.write("<html><head><title>CPU Simulator</title><link href='printings.css' rel='stylesheet'/></head><body>");
                    br.write(displayProgram);
                    br.write(memory);
                    br.write(registers);
                    br.write("</body></html>");
                } finally {
                    br.close();
                }
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {

            }
    }
}
