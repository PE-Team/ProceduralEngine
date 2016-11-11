package ptg.util.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
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
	private List<ConsoleQueue> updateQueues = new ArrayList<ConsoleQueue>();
	
	private String logFileName;
	private long lineMax;
	
	private boolean showLineNumbers, lineNumbersOn;
	private int lineNumberCount;
	private Color textColor;
	private boolean canWriteToFile;
	
	private KeyListener inputAction;
	
	private String logFilePath;
	private long finalLogFileOffset;
	private JFrame consoleFrame;
	private JPanel consolePanel;
	
	private JTextPane consoleOutputPane;
	private JTextPane consoleInputPane;
	private JTextPane consoleHeaderPane;
	private JTextPane consoleLineNumberPane;
	
	private StyledDocument consoleOutput;
	private StyledDocument consoleInput;
	private StyledDocument consoleHeader;
	private StyledDocument consoleLineNumber;
	
	private JScrollPane consoleOutputScrollbar;
	
	private SimpleAttributeSet logText, errText, warnText, sucText, logTextDefault;

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
		console.setCanWriteToFile(true);
		console.clear();
	}
	
	public Console(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.logFilePath = "./PTG/src/ptg/output/logs/";
		this.lineMax = 10;
		this.textColor = Color.BLACK;
		this.canWriteToFile = false;
		this.finalLogFileOffset = 0;
		this.showLineNumbers = true;
		this.consoleThread = new Thread(this);
	}
	
	public synchronized Console start(){
		if(!running){ 
			running = true;
			createWindow();
			clearLogFile();
			show();
			
			consoleThread.start();
		}
		return this;
	}
	
	public void run() {
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
		initializeUpdateQueues();
		consolidateRemovalsInQueues();
		sortQueues();
		executeQueues();
		updateNewLineIndeces();
		updateLineNumbers();
	}
	
	private void initializeUpdateQueues(){
		updateQueues = Util.cloneList(updateQueues, queues);
		for(int i = 0; i < updateQueues.size(); i++){
			queues.remove(0);
		}
	}
	
	private void updateNewLineIndeces(){
		lineIndeces = Util.getNewLineIndeces(getText());
	}
	
	// START OF THE QUEUE METHODS
	
	private synchronized void queue(int offset, String msg, SimpleAttributeSet attrib, int length, boolean isRemoval){
		if(isRemoval){
			queues.add(new ConsoleQueue(offset, length));
		}else{
			queues.add(new ConsoleQueue(offset, msg, attrib));
		}
	}

	private void sortQueues(){
		if(updateQueues.size() < 2) return;
		
		List<ConsoleQueue> newQueue = new ArrayList<ConsoleQueue>();
		int iterations = updateQueues.size();
		int largestOffset;
		int largestOffsetIndex;
		for(int i = 0; i < iterations; i++){
			largestOffsetIndex = 0;
			largestOffset = updateQueues.get(largestOffsetIndex).getOffset();
			for(int q = 1; q < updateQueues.size(); q++){
				ConsoleQueue queue = updateQueues.get(q);
				if(queue.getOffset() > largestOffset || (queue.isRemoval() && queue.getOffset() >= largestOffset)){ 
					largestOffset = queue.getOffset();
					largestOffsetIndex = q;
				}
			}
			
			newQueue.add(updateQueues.get(largestOffsetIndex));
			updateQueues.remove(largestOffsetIndex);
		}
		updateQueues = newQueue;
	}
	
	private void consolidateRemovalsInQueues(){
		for(int q = 0; q + 1 < updateQueues.size(); q++){
			ConsoleQueue currentQueue = updateQueues.get(q);
			ConsoleQueue futureQueue = updateQueues.get(q+1);
			if(currentQueue.isRemoval() && futureQueue.isRemoval() && currentQueue.getOffset() == futureQueue.getOffset()){
				ConsoleQueue longerRemoval = (currentQueue.length() >= futureQueue.length() || currentQueue.getOffset() == -1) ? currentQueue : futureQueue;
				updateQueues.set(q, longerRemoval);
				updateQueues.remove(q+1);
				q--;
			}
		}
	}
	
	private void executeQueues(){
		for(ConsoleQueue queue:updateQueues){
			if(queue.isRemoval()){
				removeString(queue.getOffset(), queue.length());
			}else{
				insertString(queue.getOffset(), queue.getMessage(), queue.getAttrib());
			}
		}
		queues.clear();
	}
	
	// END OF THE QUEUE METHODS
	
	private void updateLineNumbers(){
		if(!showLineNumbers && showLineNumbers == lineNumbersOn) return;
		if(showLineNumbers && lineNumberCount == lineIndeces.length + 1) return;
		
		if(showLineNumbers){
			// Show the line numbers and expand the LineNumberPane if needed
			addLineNumbers();
			lineNumberCount = lineIndeces.length + 1;
		}else{
			// Delete/Hide any line numbers and shrink the lineNumber Pane
			removeLineNumbers();
			lineNumberCount = 0;
		}
		
		lineNumbersOn = showLineNumbers;
	}
	
	private void setLineNumberBoxWidth(int widthIndex){
		int textWidth = mulRatio(widthIndex*23, ratioHeight) + mulRatio(7, ratioHeight);
		consoleLineNumberPane.setPreferredSize(new Dimension(textWidth, 0));
	}
	
	private void addLineNumbers(){
		try {
			int boxWidth = (int) Math.log10(lineIndeces.length+1) + 1;
			if(lineIndeces.length + 1 > lineNumberCount){
				// Resize the lineNumberPane width
				setLineNumberBoxWidth(boxWidth);
				
				for(int i = lineNumberCount; i < lineIndeces.length + 1; i++){
					consoleLineNumber.insertString(consoleLineNumber.getLength(),(i+1) + "\n", logTextDefault);
				}
			}else{
				// Remove some of the line numbers
				int start = getLineNumberCharCount(lineIndeces.length+1);
				consoleLineNumber.remove(start-1, consoleLineNumber.getLength() - start);
				// Resize the lineNumberPane width
				setLineNumberBoxWidth(boxWidth);
			}
		} catch (BadLocationException e){
			e.printStackTrace();
		}
	}
	
	private void removeLineNumbers(){
		try {
			setLineNumberBoxWidth(0);
			consoleLineNumber.remove(0, consoleLineNumber.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private int getLineNumberCharCount(int lineNumber){
		int charCount = 0;
		for(int i = 1; i <= lineNumber; i++){
			charCount += (int) Math.log10(i) + 2;
		}
		return charCount;
	}
	
	// START OF THE WINDOW METHODS
	
	private void createWindow(){
		ratioWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3000;
		ratioHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2000;
		
		consoleFrame = new JFrame("Console");
		consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int width = mulRatio(1500, ratioWidth);
		int height = mulRatio(1500, ratioHeight);
		int textHeight = mulRatio(42, ratioHeight);
		int textWidth = mulRatio(32, ratioHeight);
		
		consoleFrame.setSize(width, height);
		
		consolePanel = new JPanel();
		consolePanel .setLayout(new BorderLayout(5,5));
		Border panelBorder = BorderFactory.createLineBorder(Color.LIGHT_GREY.getJColor(), 5);
		consolePanel.setBorder(panelBorder);
		
		initActionHandlers();
		
		// CONSOLE OUTPUT AREA
		consoleOutputPane = new JTextPane();
		consoleOutputPane.setEditable(false);
		JPanel notTextWrapPanel = new JPanel( new BorderLayout() );
		notTextWrapPanel.add(consoleOutputPane);
		
		// CONSOLE LINE NUMBER PANE
		consoleLineNumberPane = new JTextPane();
		consoleLineNumberPane.setPreferredSize(new Dimension(textWidth, 0));
		consoleLineNumberPane.setEditable(false);
		
		// CONSOLE HEADER PANE
		consoleHeaderPane = new JTextPane();
		consoleHeaderPane.setPreferredSize(new Dimension(consoleHeaderPane.getPreferredSize().width, textHeight));
		consoleHeaderPane.setEditable(false);
		
		// CONSOLE INPUT PANE
		consoleInputPane = new JTextPane();
		consoleInputPane.setPreferredSize(new Dimension(consoleHeaderPane.getPreferredSize().width, textHeight));
		consoleInputPane.addKeyListener(inputAction);
		
		consoleOutput = consoleOutputPane.getStyledDocument();
		
		consoleLineNumber = consoleLineNumberPane.getStyledDocument();
		consoleHeader = consoleHeaderPane.getStyledDocument();
		consoleInput = consoleInputPane.getStyledDocument();
		
		initAttribs();
		consoleOutputPane.setCharacterAttributes(logText, false);
		consoleLineNumberPane.setCharacterAttributes(logText, false);
		consoleHeaderPane.setCharacterAttributes(logText, false);
		consoleInputPane.setCharacterAttributes(logText, false);
		
		// SCROLLBAR
		consoleOutputScrollbar = new JScrollPane(notTextWrapPanel);
		consoleOutputScrollbar.getVerticalScrollBar().setPreferredSize(new Dimension(mulRatio(30,ratioWidth),0));
		consoleOutputScrollbar.getHorizontalScrollBar().setPreferredSize(new Dimension(0,mulRatio(30,ratioWidth)));
		
		consolePanel.add(consoleOutputScrollbar, BorderLayout.CENTER);
		consolePanel.add(consoleLineNumberPane, BorderLayout.WEST);
		consolePanel.add(consoleHeaderPane, BorderLayout.NORTH);
		consolePanel.add(consoleInputPane, BorderLayout.SOUTH);
		consoleFrame.setContentPane(consolePanel);
		consoleFrame.setLocationByPlatform(true);
	}
	
	private void initActionHandlers(){
		inputAction = new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}
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
		
		logTextDefault = logText;
		
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
	
	private int mulRatio(double numb, double ratio){
		return (int) (numb*ratio);
	}
	
	// END OF THE WINDOW METHODS
	
	// START OF THE METHODS WHICH SET VARIABLES
	
	public synchronized Console show(){
		consoleFrame.setVisible(true);
		return this;
	}
	
	public synchronized Console hide(){
		consoleFrame.setVisible(false);
		return this;
	}
	
	public synchronized Console setLogFileName(String name){
		this.logFileName = name;
		return this;
	}
	
	public synchronized Console setLogFilePath(String name){
		this.logFilePath = name;
		return this;
	}
	
	public synchronized Console setLineMax(long lineMax){
		this.lineMax = lineMax;
		return this;
	}
	
	public synchronized Console setTextColor(Color color){
		textColor = color;
		logText.addAttribute(StyleConstants.CharacterConstants.Foreground, textColor.getJColor());
		return this;
	}
	
	public synchronized Console setCanWriteToFile(boolean canWrite){
		this.canWriteToFile = canWrite;
		return this;
	}
	
	public synchronized Console setShowLineNumbers(boolean showLineNumbers){
		this.showLineNumbers = showLineNumbers;
		return this;
	}
	
	// END OF THE METHODS WHICH SET VARIABLES
	
	// START OF THE OUTPUT METHODS
	
	public Console writeToFile(){
		writeToFile(finalLogFileOffset);
		return this;
	}
	
	public synchronized Console writeToFile(long offset){
		if(!canWriteToFile){
			try {
				throw new Exception("DOES NOT HAVE PERMISSION TO WRITE");
			} catch (Exception e) {e.printStackTrace();}
		}
		
		try {
			byte[] output = getText().getBytes();
			Path filePath = Paths.get(logFilePath + logFileName);
			RandomAccessFile tempFile = new RandomAccessFile(filePath.toFile(), "rws");
			tempFile.seek(offset);
			tempFile.write(output, 0, output.length);
			tempFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
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
	
	public synchronized Console clear(){
		writeToFile();
		finalLogFileOffset += getText().length();
		clearNoWrite();
		return this;
	}
	
	public Console clear(int indexStart, int indexEnd){
		queue(indexStart, null, null, indexEnd - indexStart, true);
		return this;
	}
	
	public Console clearAll(){
		clear();
		clearLogFile();
		return this;
	}
	
	public Console clearln(){
		clearln(lineIndeces.length);
		return this;
	}
	
	public Console clearln(int lineNumber){
		int min = lineNumber-2 < 0 ? 0 : lineIndeces[lineNumber-2]+1;
		int max = lineNumber > lineIndeces.length ? getText().length() : lineIndeces[lineNumber-1];
		clear(min, max);
		return this;
	}
	
	public synchronized Console clearLogFile(){
		try {
			Path filePath = Paths.get(logFilePath + logFileName);
			RandomAccessFile tempLog = new RandomAccessFile(filePath.toFile(), "rws");
			tempLog.setLength(0);
			tempLog.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console clearNoWrite(){
		queue(0, null, null, -1, true);
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
	
	public Console overrideChar(int index, Object msg){
		clear(index, index);
		queue(index, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console overrideChars(int start, int end, Object msg){
		clear(start, end);
		queue(start, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console overrideln(int lineNumber, Object msg){
		int min = lineNumber-2 < 0 ? 0 : lineIndeces[lineNumber-2]+1;
		int max = lineNumber > lineIndeces.length ? getText().length() : lineIndeces[lineNumber-1];
		overrideChars(min, max, msg);
		return this;
	}
	
	public Console overrideln(Object msg){
		overrideln(lineIndeces.length, msg);
		return this;
	}
}
