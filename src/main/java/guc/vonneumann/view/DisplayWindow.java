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

    public static void addDisplayProgram(DisplayProgram program){
        StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<style>");
        tableHTML.append("table{width:100%;border-spacing:0px;margin:20px;border-collapse:collapse;border:1px solid white;font-family:monospace;font-size:16px;}");
        tableHTML.append("th{border:1px solid white;color:white;text-align:center;font-weight:bold;}");
        tableHTML.append("td{border:1px solid white;color:white;padding:2px;}");
        tableHTML.append("</style>");
        tableHTML.append("<table cellpadding='2'>");
        tableHTML.append("<tr><th>Cycle</th><th>IF</th><th>ID</th><th>EX</th><th>MEM</th><th>WB</th></tr>");
        int counter = 1;
        for(DisplayCycle cycle : program.getCycles()){
            if(counter%2 == 1){
                tableHTML.append("<tr style='background-color:#222222;'>");
            }
            else{
                tableHTML.append("<tr>");
            }
            counter++;
            tableHTML.append("<td style='text-align:center;'>");
            tableHTML.append(cycle.getCycleNumber());
            tableHTML.append("</td>");
            for(DisplayPhase phase : cycle.getPhases()){
                tableHTML.append("<td>");
                for(String message : phase.getMessages()){
                    tableHTML.append(message);
                    tableHTML.append("<br/>");
                }
                tableHTML.append("</td>");
            }
            tableHTML.append("</tr>");
        }
        tableHTML.append("</table>");
        getMainWindow().addHTML(tableHTML.toString());
    }

    public static void addMemory(int[] memory){
        StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<h1 style='color:white;font-size:20px;font-family:serif;margin:20px;'>RAM</h1>");
        tableHTML.append("<table cellpadding='2'>");
        int counter = 1;
        int cols = 8;
        for(int i = 0;i < memory.length;i+=cols){
            if(counter%2 == 1){
                tableHTML.append("<tr style='background-color:#222222;'>");
            }
            else{
                tableHTML.append("<tr>");
            }
            counter++;
            tableHTML.append("<td style='font-weight:bold;'>");
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
        getMainWindow().addHTML(tableHTML.toString());
    }

    public static void addRegisters(int[] registers, int pc){
        StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<h1 style='color:white;font-size:20px;font-family:serif;margin:20px;'>Registers</h1>");
        tableHTML.append("<h1 style='color:white;font-size:16px;font-family:serif;margin-left:20px;'>PC: " + pc + "</h1>");
        tableHTML.append("<table cellpadding='2'>");
        int counter = 1;
        int cols = 8;
        for(int i = 0;i < registers.length;i+=cols){
            if(counter%2 == 1){
                tableHTML.append("<tr style='background-color:#222222;'>");
            }
            else{
                tableHTML.append("<tr>");
            }
            counter++;
            tableHTML.append("<td style='font-weight:bold;'>");
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
        getMainWindow().addHTML(tableHTML.toString());
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

