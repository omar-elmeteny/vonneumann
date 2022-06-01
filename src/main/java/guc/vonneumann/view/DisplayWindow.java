package guc.vonneumann.view;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

// Displays every scheduling event and process output and executing instruction
public class DisplayWindow extends JFrame{

    private static DisplayWindow mainWindow;
    private JTextPane outputTextPane;
    private JScrollPane outputTextScroller;
    private HTMLEditorKit editorKit;
    private HTMLDocument htmlDoc;

    private DisplayWindow() {
        super();
        setTitle("CPU Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500,800);
        // https://stackoverflow.com/questions/5133240/add-html-content-to-document-associated-with-jtextpane
        editorKit = new HTMLEditorKit();
        htmlDoc = new HTMLDocument();
        outputTextPane = new JTextPane();
        outputTextPane.setEditorKit(editorKit);
        outputTextPane.setDocument(htmlDoc);
        outputTextPane.setEditable(false);
        outputTextPane.setBackground(Color.getHSBColor(216,0.28f,0.07f));
        outputTextPane.setContentType("text/html");
        outputTextPane.setCaretColor(outputTextPane.getBackground());
        outputTextPane.getCaret().setBlinkRate(0);
        outputTextScroller = new JScrollPane(outputTextPane);
        outputTextScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputTextScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(outputTextScroller);
        setVisible(true);
    }

    // singleton design pattern because we can only have one window
    public static DisplayWindow getMainWindow(){
        if(mainWindow == null){
            mainWindow = new DisplayWindow();
        }
        return mainWindow;
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
                tableHTML.append("<td>");
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
        tableHTML.append("<h1>PC: " + pc + "</h1>");
        tableHTML.append("<table id='registers'>");
        int cols = 8;
        for(int i = 0;i < registers.length;i+=cols){
            tableHTML.append("<tr>");
            tableHTML.append("<td>");
            tableHTML.append(String.format("%d-%d", i, i + cols - 1));
            tableHTML.append("</td>");
            for(int j = 0;j < cols;j++){
                tableHTML.append("<td>");
                tableHTML.append(registers[i+j]);
                tableHTML.append("</td>");
            }
            tableHTML.append("</tr>");
        }
        tableHTML.append("</table>");
        return tableHTML.toString();
    }

    public void addHTML(String html){
        try {
            editorKit.insertHTML(htmlDoc,htmlDoc.getLength(), html,0, 0,  null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}

