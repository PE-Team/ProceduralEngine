package ptg.util.console;

import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Console {
	
	private String logFileName;
	private int lineMax;
	private List<String> consoleLines = new ArrayList<String>();
	private JFrame consoleFrame;
	
	public int pointer;

	public Console(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.lineMax = 10;
		createWindow();
	}
	
	public static void main(String...args){
		Console console = new Console();
	}
	
	private void createWindow(){
		consoleFrame = new JFrame("Console");
		consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		double aspect = 10/19d;
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = width*aspect;
		
		consoleFrame.setSize(500, 200);
		consoleFrame.pack();
		consoleFrame.setVisible(true);
	}
	
	private void parseInput(){
		
	}
	
	public void setPointer(int lineNumber){
		
	}
	
	public void overrideln(){
		overrideln(pointer);
	}
	
	public void overrideln(int lineNumber){
		
	}
	
	public void overrideChar(int index){
		
	}
	
	public void overrideChars(int start, int end){
		
	}
	
	private void createLogFile(String fileName){
		logFileName = fileName;
	}
	
	public void logToFile(){
		
	}
	
	public void clear(){
		
	}
	
	public void clear(int lineNumber){
		
	}
	
	public void clearLog(){
		
	}
	
	public void clearAll(){
		clear();
		clearLog();
	}
	
	public void log(String msg){
		
	}
	
	public void logln(String msg){
		log(msg);
		setPointer(++pointer);
	}
	
	public void setLineMax(int lineMax){
		this.lineMax = lineMax;
	}
}
