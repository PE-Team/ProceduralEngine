package ptg.util.console;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ptg.util.Timer;
import ptg.util.Util;
import ptg.util.color.Color;

public class Console implements Runnable{

	private boolean running = false;
	private Thread consoleThread;
	private final int UPS = 60;
	private final long NANO = 1000000000;
	
	private List<ConsoleQueue> queues = new ArrayList<ConsoleQueue>();
	
	private String logFileName;
	private int lineMax;
	
	private boolean showLineNumbers;
	private Color textColor;
	private boolean writeToFile;
	
	private Path logFilePath;
	private long finalLogFileOffset;
	private JFrame consoleFrame;
	private JPanel consolePanel;
	private JTextPane consoleOutputPane;
	private StyledDocument consoleOutput;
	
	private JScrollPane consoleOutputScrollbar;
	
	private SimpleAttributeSet logText, errText, warnText, sucText;

	private double ratioWidth, ratioHeight;
	
	private int[] lineIndeces = new int[0];
	
	public static void main(String...args){
		Console console = new Console();
		Timer timer = new Timer(500);
		console.start();
		
		console.log("A Normal Log");
		console.logSuccess("A Success");
		console.warn("A Warning");
		console.err("An error");
		
		timer.start();
		
		console.clearNoWrite();
	}
	
	public Console(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.lineMax = 10;
		this.textColor = Color.BLACK;
		this.writeToFile = false;
		this.finalLogFileOffset = 0;
		this.showLineNumbers = false;
		this.consoleThread = new Thread(this);
	}
	
	public synchronized Console start(){
		if(!running){ 
			running = true;
			createWindow();
			show();
			
			consoleThread.start();
		}
		return this;
	}
	
	public void run() {
		update();
		long delta;
		long lastUpdate = System.nanoTime();
		
		long updates = 0;
		
		while(running){
			delta = System.nanoTime() - lastUpdate;
			if(UPS*delta/NANO >= 1){
				lastUpdate = System.nanoTime();
				
				if(updates%UPS == 0){
					//This is triggered once per second
					
					updates = 0;
				}
				
				update();
				updates++;
			}
		}
	}
	
	private void update(){
		consolidateRemovalsInQueue();
		sortQueue();
		executeQueues();
	}
	
	private void createWindow(){
		ratioWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3000;
		ratioHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2000;
		
		consoleFrame = new JFrame("Console");
		consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int width = mulRatio(1500, ratioWidth);
		int height = mulRatio(1900, ratioHeight);
		
		consoleFrame.setSize(width, height);
		
		consolePanel = new JPanel();
		
		// CONSOLE OUTPUT AREA
		consoleOutputPane = new JTextPane();
		
		consoleOutput = consoleOutputPane.getStyledDocument();
		
		initAttribs();
		consoleOutputPane.setCharacterAttributes(logText, false);
		
		// SCROLLBAR
		consoleOutputScrollbar = new JScrollPane(consoleOutputPane);
		consoleOutputScrollbar.getVerticalScrollBar().setPreferredSize(new Dimension(mulRatio(30,ratioWidth),0));
		
		consoleFrame.add(consolePanel);
		consoleFrame.add(consoleOutputScrollbar);
		
	}
	
	private void initAttribs(){
		Object fontForground = StyleConstants.CharacterConstants.Foreground;
		Object fontSize = StyleConstants.CharacterConstants.FontSize;
		Object fontFamily = StyleConstants.CharacterConstants.FontFamily;
		
		int defaultTextSize = mulRatio(36, ratioHeight);
		String defaultFontFamily = "Lucida Console";
		
		logText = new SimpleAttributeSet();
		logText.addAttribute(fontForground, textColor.getJColor());
		logText.addAttribute(fontSize, defaultTextSize);
		logText.addAttribute(fontFamily, defaultFontFamily);
		
		errText = new SimpleAttributeSet();
		errText.addAttribute(fontForground, Color.RED.getJColor());
		errText.addAttribute(fontSize, defaultTextSize);
		errText.addAttribute(fontFamily, defaultFontFamily);
		
		warnText = new SimpleAttributeSet();
		warnText.addAttribute(fontForground, Color.ORANGE.getJColor());
		warnText.addAttribute(fontSize, defaultTextSize);
		warnText.addAttribute(fontFamily, defaultFontFamily);
		
		sucText = new SimpleAttributeSet();
		sucText.addAttribute(fontForground, Color.GREEN.getJColor());
		sucText.addAttribute(fontSize, defaultTextSize);
		sucText.addAttribute(fontFamily, defaultFontFamily);
	}
	
	public synchronized Console show(){
		consoleFrame.setVisible(true);
		return this;
	}
	
	public synchronized Console hide(){
		consoleFrame.setVisible(false);
		return this;
	}
	
	private int mulRatio(double numb, double ratio){
		return (int) (numb*ratio);
	}
	
	private synchronized void queue(int offset, String msg, SimpleAttributeSet attrib, int length, boolean isRemoval){
		if(isRemoval){
			queues.add(new ConsoleQueue(offset, length));
		}else{
			queues.add(new ConsoleQueue(offset, msg, attrib));
		}
	}

	private void sortQueue(){
		if(queues.size() < 2) return;
		
		List<ConsoleQueue> newQueue = new ArrayList<ConsoleQueue>();
		int iterations = queues.size();
		int largestOffset;
		int largestOffsetIndex;
		for(int i = 0; i < iterations; i++){
			largestOffsetIndex = 0;
			largestOffset = queues.get(largestOffsetIndex).getOffset();
			for(int q = 1; q < queues.size(); q++){
				ConsoleQueue queue = queues.get(q);
				if(queue.getOffset() > largestOffset || (queue.isRemoval() && queue.getOffset() >= largestOffset)){ 
					largestOffset = queue.getOffset();
					largestOffsetIndex = q;
				}
			}
			
			newQueue.add(queues.get(largestOffsetIndex));
			queues.remove(largestOffsetIndex);
		}
		queues = newQueue;
	}
	
	private void consolidateRemovalsInQueue(){
		for(int q = 0; q + 1 < queues.size(); q++){
			ConsoleQueue currentQueue = queues.get(q);
			ConsoleQueue futureQueue = queues.get(q+1);
			if(currentQueue.isRemoval() && futureQueue.isRemoval() && currentQueue.getOffset() == futureQueue.getOffset()){
				ConsoleQueue longerRemoval = (currentQueue.length() >= futureQueue.length() || currentQueue.getOffset() == -1) ? currentQueue : futureQueue;
				queues.set(q, longerRemoval);
				queues.remove(q+1);
				q--;
			}
		}
	}
	
	private void executeQueues(){
		for(ConsoleQueue queue:queues){
			if(queue.isRemoval()){
				removeString(queue.getOffset(), queue.length());
			}else{
				insertString(queue.getOffset(), queue.getMessage(), queue.getAttrib());
			}
		}
		queues.clear();
	}
	
	private void removeString(int indexStart, int length){
		try {
			if(length == -1) length = consoleOutput.getLength();
			consoleOutput.remove(indexStart, length);
			lineIndeces = Util.getNewLineIndeces(getText());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void insertString(int offset, String msg, AttributeSet attrib){
		try {
			if(offset == -1) offset = consoleOutput.getLength();
			consoleOutput.insertString(offset, msg, attrib);
			lineIndeces = Util.getNewLineIndeces(getText());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public String getText(){
		try {
			return consoleOutput.getText(0, consoleOutput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Console clearNoWrite(){
		queue(0, null, null, -1, true);
		return this;
	}
	
	public Console clear(){
		//TODO write to file
		finalLogFileOffset += getText().length();
		clearNoWrite();
		return this;
	}
	
	public Console clear(int indexStart, int indexEnd){
		queue(indexStart, null, null, indexEnd - indexStart, true);
		return this;
	}
	
	public Console log(Object msg){
		queue(-1, msg.toString() + "\n", logText, -1, false);
		return this;
	}
	
	public Console logln(Object msg){
		queue(-1, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console logSuccess(Object msg){
		queue(-1, msg.toString() + "\n", sucText, -1, false);
		return this;
	}
	
	public Console logSuccessln(Object msg){
		queue(-1, msg.toString(), sucText, -1, false);
		return this;
	}
	
	public Console warn(Object msg){
		queue(-1, msg.toString() + "\n", warnText, -1, false);
		return this;
	}
	
	public Console warnln(Object msg){
		queue(-1, msg.toString(), warnText, -1, false);
		return this;
	}
	
	public Console err(Object msg){
		queue(-1, msg.toString() + "\n", errText, -1, false);
		return this;
	}
	
	public Console errln(Object msg){
		queue(-1, msg.toString(), errText, -1, false);
		return this;
	}
}
