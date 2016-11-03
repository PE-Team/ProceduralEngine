package ptg.util.console;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ptg.util.color.Color;

public class Console {
	
	private String logFileName;
	private int lineMax;
	private List<String> consoleLines = new ArrayList<String>();
	private Color textColor;
	
	private JFrame consoleFrame;
	private JPanel consolePanel;
	private JTextArea consoleOutput;
	private Font consoleFont;
	private JScrollPane consoleOutputScrollbar;
	
	private double ratioWidth, ratioHeight;
	
	public int pointer;

	public Console(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.lineMax = 10;
		this.textColor = Color.BLACK;
	}
	
	public static void main(String...args){
		Console console = new Console().start();
		console.log("This is Black");
		console.setTextColor(Color.RED).log("This is a failure");
		console.setTextColor(Color.ORANGE).log("This is a warning");
		console.setTextColor(Color.GREEN).log("This is a success");
	}
	
	private void createWindow(){
		ratioWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3000;
		ratioHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2000;
		
		consoleFont = new Font("Verdana", Font.PLAIN, mulRatio(36, ratioHeight));
		
		consoleFrame = new JFrame("Console");
		consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int width = mulRatio(1500, ratioWidth);
		int height = mulRatio(1900, ratioHeight);
		
		consoleFrame.setSize(width, height);
		
		consolePanel = new JPanel();
		
		// CONSOLE OUTPUT AREA
		consoleOutput = new JTextArea();
		consoleOutput.setFont(consoleFont);
		consoleOutput.setForeground(textColor.getJColor());
		
		// SCROLLBAR
		consoleOutputScrollbar = new JScrollPane(consoleOutput);
		consoleOutputScrollbar.getVerticalScrollBar().setPreferredSize(new Dimension(mulRatio(30,ratioWidth),0));
		
		consoleFrame.add(consolePanel);
		consoleFrame.add(consoleOutputScrollbar);
		
	}
	
	public Console start(){
		createWindow();
		show();
		return this;
	}
	
	public Console hide(){
		consoleFrame.setVisible(false);
		return this;
	}
	
	public Console show(){
		consoleFrame.setVisible(true);
		return this;
	}
	
	private int mulRatio(double numb, double ratio){
		return (int) (numb*ratio);
	}
	
	private void parseInput(){
		
	}
	
	public Console overrideln(){
		overrideln(pointer);
		return this;
	}
	
	public Console overrideln(int lineNumber){
		
		return this;
	}
	
	public Console overrideChar(int index){
		
		return this;
	}
	
	public Console overrideChars(int start, int end){
		
		return this;
	}
	
	private void createLogFile(String fileName){
		logFileName = fileName;
	}
	
	public Console logToFile(){
		
		return this;
	}
	
	public Console clear(){
		
		return this;
	}
	
	public Console clear(int lineNumber){
		
		return this;
	}
	
	public Console clearLog(){
		
		return this;
	}
	
	public Console clearAll(){
		clear();
		clearLog();
		return this;
	}
	
	public Console log(String msg){
		consoleOutput.append(msg);
		return this;
	}
	
	public Console logln(String msg){
		log(msg + "\n");
		return this;
	}
	
	public Console setLineMax(int lineMax){
		this.lineMax = lineMax;
		return this;
	}
	
	public Console setPointer(int lineNumber){
		
		return this;
	}
	
	public Console setTextColor(Color color){
		textColor = color;
		consoleOutput.setForeground(textColor.getJColor());
		return this;
	}
}
