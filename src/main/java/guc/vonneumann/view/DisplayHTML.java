package guc.vonneumann.view;

public class DisplayHTML {

    
    private DisplayHTML() {
    }

    public static String addDisplayProgram(DisplayProgram program){
        StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<table id='program'>");
        tableHTML.append("<tr><th>Cycle</th><th>IF</th><th>ID</th><th>EX</th><th>MEM</th><th>WB</th></tr>");
        for(DisplayCycle cycle : program.getCycles()){
            tableHTML.append("<tr>");
            tableHTML.append("<td>");
            tableHTML.append(cycle.getCycleNumber());
            tableHTML.append("</td>");
            for(DisplayPhase phase : cycle.getPhases()){
                if(phase.getCycles() == 0){
                    continue;
                }
                else if(phase.getCycles() > 1){
                    tableHTML.append("<td rowspan='" + phase.getCycles() + "'>");
                }
                else{
                    tableHTML.append("<td>");
                }
                for(String message : phase.getMessages()){
                    tableHTML.append(message);
                    tableHTML.append("<br/>");
                }
                tableHTML.append("</td>");
            }
            tableHTML.append("</tr>");
        }
        tableHTML.append("</table>");
        return tableHTML.toString();
    }

    public static String addMemory(int[] memory){
        StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<h1>RAM</h1>");
        tableHTML.append("<table id='memory'>");
        int cols = 8;
        for(int i = 0;i < memory.length;i+=cols){
            tableHTML.append("<tr>");
            tableHTML.append("<td>");
            tableHTML.append(String.format("%d-%d", i, i + cols - 1));
            tableHTML.append("</td>");
            for(int j = 0;j < cols;j++){
                if (memory[i + j] != 0) {
                    tableHTML.append("<td class='changed'>");    
                } else {
                    tableHTML.append("<td>");    
                }
                tableHTML.append(memory[i+j]);
                tableHTML.append("</td>");
            }
            tableHTML.append("</tr>");
        }
        tableHTML.append("</table>");
        return tableHTML.toString();
    }

    public static String addRegisters(int[] registers, int pc){
        StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<h1>Registers</h1>");
        tableHTML.append("<h4>PC=" + pc + "</h4>");
        tableHTML.append("<table id='registers'>");
        int cols = 8;
        for(int i = 0;i < registers.length;i+=cols){
            tableHTML.append("<tr>");
            for(int j = 0;j < cols;j++){
                if (registers[i + j] != 0) {
                    tableHTML.append("<td class='changed'>R");    
                } else {
                    tableHTML.append("<td>R");    
                }
                tableHTML.append(i + j);
                tableHTML.append("=");
                tableHTML.append(registers[i+j]);
                tableHTML.append("</td>");
            }
            tableHTML.append("</tr>");
        }
        tableHTML.append("</table>");
        return tableHTML.toString();
    }
}

