package pe.util.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import pe.util.Util;
import pe.util.color.Color;

public class Console implements Runnable{

	private boolean running = false;
	private Thread consoleThread;
	private final int UPS = 60;
	private final long NANO = 1000000000;
	
	private int pastConsoleCommandIndex = 0;
	private List<String> pastConsoleCommands = new ArrayList<String>();
	
	private List<ConsoleQueue> queues = new ArrayList<ConsoleQueue>();
	private List<ConsoleQueue> updateQueues = new ArrayList<ConsoleQueue>();
	
	private String logFileName;
	private long lineMax;
	
	private boolean scrollLock;
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
	private JScrollPane consoleInputScrollbar;
	
	private SimpleAttributeSet logText, errText, warnText, sucText, logTextDefault;

	private double ratioWidth, ratioHeight;
	
	private int[] lineIndeces = new int[0];
	
	public static void main(String...args){
		Console console = new Console();
		console.start();
		console.setCanWriteToFile(true);
	}
	
	public Console(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.logFilePath = "./PE/src/pe/output/logs/";
		this.lineMax = 10;
		this.textColor = Color.BLACK;
		this.canWriteToFile = false;
		this.finalLogFileOffset = 0;
		this.showLineNumbers = true;
		this.scrollLock = false;
		this.consoleThread = new Thread(this);
	}
	
	public synchronized Console start(){
		if(!running){ 
			running = true;
			createWindow();
			clearLogFile();
			initConsoleCommands();
			show();
			
			consoleThread.start();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	private void initConsoleCommands(){
		ConsoleInputInterpreter.initConsoleInputInterpreter(this);
	}
	
	public void run() {
		long delta;
		long lastUpdate = System.nanoTime();
		
		long updates = 0;

		consoleInputPane.requestFocus();
		
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
		updateScroll();
	}
	
	private void updateScroll(){
		JScrollBar verticalOutputBar = consoleOutputScrollbar.getVerticalScrollBar();
		
		if(!scrollLock){
			verticalOutputBar.setValue(verticalOutputBar.getMaximum());
		}
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
	
	private synchronized void queue(int offset, boolean isLineNumber, String msg, SimpleAttributeSet attrib, int length, boolean isRemoval){
		if(isRemoval){
			queues.add(new ConsoleQueue(offset, isLineNumber, length));
		}else{
			queues.add(new ConsoleQueue(offset, isLineNumber, msg, attrib));
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
				removeString(queue.getOffset(), queue.isOffsetAsLineNumber(), queue.length());
			}else{
				insertString(queue.getOffset(), queue.isOffsetAsLineNumber(), queue.getMessage(), queue.getAttrib());
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
		
		int boxWidth = (int) Math.log10(lineIndeces.length+1) + 1;
		setLineNumberBoxWidth(boxWidth);
		lineNumbersOn = showLineNumbers;
	}
	
	private void setLineNumberBoxWidth(int widthIndex){
		int textWidth = mulRatio(widthIndex*23, ratioHeight) + mulRatio(7, ratioHeight);
		consoleLineNumberPane.setPreferredSize(new Dimension(textWidth, 0));
	}
	
	private void addLineNumbers(){
		try {
			if(lineIndeces.length + 1 > lineNumberCount){
				for(int i = lineNumberCount; i < lineIndeces.length + 1; i++){
					consoleLineNumber.insertString(consoleLineNumber.getLength(),(i+1) + "\n", logTextDefault);
				}
			}else{
				// Remove some of the line numbers
				int start = getLineNumberCharCount(lineIndeces.length+1);
				consoleLineNumber.remove(start-1, consoleLineNumber.getLength() - start);
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
		JPanel noTextWrapPanel = new JPanel(new BorderLayout());
		
		noTextWrapPanel.add(consoleLineNumberPane, BorderLayout.WEST);
		noTextWrapPanel.add(consoleOutputPane, BorderLayout.CENTER);
		Border lineNumberBorder = BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(200,200,200).getJColor());
		Border outputBorder = BorderFactory.createMatteBorder(0, 5, textHeight/2, 0, Color.WHITE.getJColor());
		consoleOutputPane.setBorder(outputBorder);
		consoleLineNumberPane.setBorder(lineNumberBorder);
		
		
		consoleOutputScrollbar = new JScrollPane(noTextWrapPanel);
		consoleOutputScrollbar.getVerticalScrollBar().setPreferredSize(new Dimension(mulRatio(30,ratioWidth),0));
		consoleOutputScrollbar.getVerticalScrollBar().setUnitIncrement(textHeight/2);
		consoleOutputScrollbar.getHorizontalScrollBar().setPreferredSize(new Dimension(0,mulRatio(30,ratioWidth)));
		consoleOutputScrollbar.getHorizontalScrollBar().setUnitIncrement(textWidth/2);
		
		consoleInputScrollbar = new JScrollPane(consoleInputPane);
		consoleInputScrollbar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		consolePanel.add(consoleOutputScrollbar, BorderLayout.CENTER);
		//consolePanel.add(consoleLineNumberScrollbar, BorderLayout.WEST);
		consolePanel.add(consoleHeaderPane, BorderLayout.NORTH);
		consolePanel.add(consoleInputScrollbar, BorderLayout.SOUTH);
		consoleFrame.setContentPane(consolePanel);
		consoleFrame.setLocationByPlatform(true);
	}
	
	private void initActionHandlers(){
		Console thisConsole = this;
		
		inputAction = new KeyListener(){
			public void keyPressed(KeyEvent e) {}

			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					thisConsole.pastConsoleCommandIndex = 0;
					new ConsoleInputInterpreter(thisConsole);
				}
				
				if(e.getKeyCode() == KeyEvent.VK_UP){
					thisConsole.pastConsoleCommandIndex = thisConsole.pastConsoleCommandIndex >= thisConsole.pastConsoleCommands.size() ? thisConsole.pastConsoleCommands.size() : thisConsole.pastConsoleCommandIndex + 1;
					thisConsole.setInput(thisConsole.pastConsoleCommands.get(thisConsole.pastConsoleCommands.size() - thisConsole.pastConsoleCommandIndex));
				}
				
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					thisConsole.pastConsoleCommandIndex = thisConsole.pastConsoleCommandIndex <= 0 ? 0 : thisConsole.pastConsoleCommandIndex - 1;
					if(thisConsole.pastConsoleCommandIndex > 0){
						thisConsole.setInput(thisConsole.pastConsoleCommands.get(thisConsole.pastConsoleCommands.size() - thisConsole.pastConsoleCommandIndex));
					}
				}
			}

			public void keyTyped(KeyEvent e) {}
		};
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
	
	public synchronized Console addPastCommand(String cmd){
		this.pastConsoleCommands.add(cmd);
		return this;
	}
	
	public synchronized Console setPastConsoleCommandIndex(int index){
		this.pastConsoleCommandIndex = index;
		return this;
	}
	
	public synchronized Console setInput(String cmd){
		clearInput();
		try {
			this.consoleInput.insertString(0, cmd, logText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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
	
	public synchronized Console setScrollLock(boolean scrollLock){
		this.scrollLock = scrollLock;
		return this;
	}
	
	// END OF THE METHODS WHICH SET VARIABLES
	
	// START OF THE METHODS WHICH GET VARIABLES
	
	public String getText(){
		try {
			return consoleOutput.getText(0, consoleOutput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getInputText(){
		try {
			return consoleInput.getText(0, consoleInput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// END OF THE METHODS WHICH GET VARIABLES
	
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
	
	private void removeString(int indexStart, boolean isLineNumber, int length){
		try {
			if(length == -1) length = consoleOutput.getLength();
			
			if(isLineNumber){
				int min = indexStart-2 < 0 ? 0 : lineIndeces[indexStart-2]+1;
				int max = indexStart > lineIndeces.length ? getText().length() : lineIndeces[indexStart-1];
				indexStart = min;
				length = max - min;
			}
			
			consoleOutput.remove(indexStart, length);
			lineIndeces = Util.getNewLineIndeces(getText());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void insertString(int offset, boolean isLineNumber, String msg, AttributeSet attrib){
		try {
			if(offset == -1) offset = consoleOutput.getLength();
			
			if(isLineNumber) offset = offset-2 < 0 ? 0 : lineIndeces[offset-2]+1;
			
			consoleOutput.insertString(offset, msg, attrib);
			lineIndeces = Util.getNewLineIndeces(getText());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Console clear(){
		writeToFile();
		finalLogFileOffset += getText().length();
		clearNoWrite();
		return this;
	}
	
	public Console clear(int indexStart, int indexEnd){
		queue(indexStart, false, null, null, indexEnd - indexStart, true);
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
		queue(lineNumber, true, null, null, -1, true);
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
		queue(0, false, null, null, -1, true);
		return this;
	}
	
	public synchronized Console clearInput(){
		try {
			consoleInput.remove(0, consoleInput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console log(Object msg){
		queue(-1, false, msg.toString() + "\n", logText, -1, false);
		return this;
	}
	
	public Console log(){
		queue(-1, false, "\n", logText, -1, false);
		return this;
	}
	
	public Console logln(Object msg){
		queue(-1, false, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console logln(){
		queue(-1, false, "", logText, -1, false);
		return this;
	}
	
	public Console logSuccess(Object msg){
		queue(-1, false, msg.toString() + "\n", sucText, -1, false);
		return this;
	}
	
	public Console logSuccess(){
		queue(-1, false, "\n", sucText, -1, false);
		return this;
	}
	
	public Console logSuccessln(Object msg){
		queue(-1, false, msg.toString(), sucText, -1, false);
		return this;
	}
	
	public Console logSuccessln(){
		queue(-1, false, "", sucText, -1, false);
		return this;
	}
	
	public Console warn(Object msg){
		queue(-1, false, msg.toString() + "\n", warnText, -1, false);
		return this;
	}
	
	public Console warn(){
		queue(-1, false, "\n", warnText, -1, false);
		return this;
	}
	
	public Console warnln(Object msg){
		queue(-1, false, msg.toString(), warnText, -1, false);
		return this;
	}
	
	public Console warnln(){
		queue(-1, false, "", warnText, -1, false);
		return this;
	}
	
	public Console err(Object msg){
		queue(-1, false, msg.toString() + "\n", errText, -1, false);
		return this;
	}
	
	public Console err(){
		queue(-1, false, "\n", errText, -1, false);
		return this;
	}
	
	public Console errln(Object msg){
		queue(-1, false, msg.toString(), errText, -1, false);
		return this;
	}
	
	public Console errln(){
		queue(-1, false, "", errText, -1, false);
		return this;
	}
	
	public Console overrideChar(int index, Object msg){
		clear(index, index);
		queue(index, false, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console overrideChars(int start, int end, Object msg){
		clear(start, end);
		queue(start, false, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console overrideln(int lineNumber, Object msg){
		clearln(lineNumber);
		queue(lineNumber, true, msg.toString(), logText, -1, false);
		return this;
	}
	
	public Console overrideln(Object msg){
		overrideln(lineIndeces.length, msg);
		return this;
	}
}
