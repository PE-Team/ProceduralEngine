package ptg.util.console;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import ptg.util.color.Color;
import ptg.util.Util;

public class Console_d {
	

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
	
	public Console_d(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.lineMax = 10;
		this.textColor = Color.BLACK;
		this.writeToFile = false;
		this.finalLogFileOffset = 0;
		this.showLineNumbers = false;
	}
	
	public static void main(String...args){
		Console_d console = new Console_d().start();
		console.setWriteToFile(true);
		console.setLogFilePath();
		
		
		Timer timer = new Timer(200);
		
		console.log("Well hello");
		/*
		timer.start();
		console.warn("I will be leaving soon");
		timer.start();
		console.err("Very, VERY soon");
		timer.start();
		console.log("Bye bye!");
		timer.start();
		console.clearln(3);
		timer.start();
		console.overrideChars(21, 27, "Hello");
		timer.start();
		console.clearln(2);
		timer.start();
		console.overrideln("This should be overridden");
		timer.start();
		console.log("This is another new line");
		timer.start();
		console.clear();
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.log("Moore text");
		console.writeToFile();
		console.showLineNumbers(true);
		timer.start();
		console.showLineNumbers(false);
		timer.start();
		console.showLineNumbers(true);
		timer.start();
		console.showLineNumbers(false);
		*/
	}
	
	private void addLineNumbers(){
		int lineNumber = 1;
		int lineNumberMax = lineIndeces.length + 1;
		insertString(0, Util.addSpaces(lineNumber + "|", (int) Math.log10(lineNumberMax) + 2), logText);
		for(int i = 0; i < lineIndeces.length; i++){
			lineIndeces = Util.getNewLineIndeces(getText());
			lineNumber++;
			int offset = lineIndeces[i]+1;
			insertString(offset, Util.addSpaces(lineNumber + "|", (int) Math.log10(lineNumberMax) + 2), logText);
		}
	}
	
	public Console_d clear(){
		writeToFile();
		finalLogFileOffset += getText().length();
		clearNoWrite();
		return this;
	}
	
	public Console_d clear(int indexStart, int indexEnd){
		removeString(indexStart, indexEnd-indexStart);
		updateNewLineIndeces();
		return this;
	}
	
	public Console_d clearAll(){
		clear();
		clearLogFile();
		return this;
	}
	
	public Console_d clearln(){
		clearln(lineIndeces.length);
		return this;
	}
	
	public Console_d clearln(int lineNumber){
		int min = lineNumber-2 < 0 ? 0 : lineIndeces[lineNumber-2]+1;
		int max = lineNumber > lineIndeces.length ? getText().length() : lineIndeces[lineNumber-1];
		clear(min, max);
		return this;
	}
	
	public Console_d clearLogFile(){
		try {
			RandomAccessFile tempLog = new RandomAccessFile(logFilePath.toFile(), "rws");
			tempLog.setLength(0);
			tempLog.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console_d clearNoWrite(){
		removeString(0, consoleOutput.getLength());
		return this;
	}
	
	public Console_d setLogFilePath(){
		logFilePath = Paths.get("./PTG/src/ptg/output/logs/" + logFileName);
		return this;
	}
	
	private void createWindow(){
		ratioWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3000;
		ratioHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2000;
		
		consoleFrame = new JFrame("Console_d");
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
	
	public Console_d err(String msg){
		errln(msg + "\n");
		return this;
	}
	
	public Console_d errln(String msg){
		insertString(consoleOutput.getLength(), msg, errText);
		updateNewLineIndeces();
		return this;
	}
	
	public String getText(){
		try {
			return consoleOutput.getText(0, consoleOutput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Console_d hide(){
		consoleFrame.setVisible(false);
		return this;
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
	
	private void insertString(int offset, String msg, AttributeSet attrib){
		try {
			consoleOutput.insertString(offset, msg, attrib);
			lineIndeces = Util.getNewLineIndeces(getText());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public Console_d log(String msg){
		logln(msg + "\n");
		return this;
	}
	
	public Console_d logln(String msg){
		insertString(consoleOutput.getLength(), msg, logText);
		updateNewLineIndeces();
		return this;
	}
	
	public Console_d logSuccess(String msg){
		logSuccessln(msg + "\n");
		return this;
	}
	
	public Console_d logSuccessln(String msg){
		insertString(consoleOutput.getLength(), msg, sucText);
		updateNewLineIndeces();
		return this;
	}
	
	private int mulRatio(double numb, double ratio){
		return (int) (numb*ratio);
	}
	
	public Console_d overrideChar(int index, String character){
		clear(index, index);
		insertString(index, character, logText);
		updateNewLineIndeces();
		return this;
	}
	
	public Console_d overrideChars(int start, int end, String chars){
		clear(start, end);
		insertString(start, chars, logText);
		updateNewLineIndeces();
		return this;
	}
	
	public Console_d overrideln(int lineNumber, String msg){
		int min = lineNumber-2 < 0 ? 0 : lineIndeces[lineNumber-2]+1;
		int max = lineNumber > lineIndeces.length ? getText().length() : lineIndeces[lineNumber-1];
		overrideChars(min, max, msg);
		return this;
	}
	public Console_d overrideln(String msg){
		overrideln(lineIndeces.length, msg);
		return this;
	}
	
	private void removeLineNumbers(){
		int testLength;
		int removeLength = 0;
		// Remove the line number on the first line
		try{
			testLength = lineIndeces[0];
		}catch(Exception e){
			testLength = consoleOutput.getLength();
		}
		for(int j = 0; j < testLength; j++){
			char index = getText().charAt(j);
			if(Util.isNumber(index) || index == ' ' || index == '|'){
				removeLength++;
			}else{
				break;
			}
		}
		removeString(0, removeLength);
		
		// Remove the line number for all other lines
		for(int i = 0; i < lineIndeces.length; i++){
			int start = lineIndeces[i];
			try{
				testLength = lineIndeces[i+1] - start;
			}catch(Exception e){
				testLength = consoleOutput.getLength() - start;
			}
			removeLength = 0;
			for(int j = 0; j < testLength; j++){
				char index = getText().charAt(start+j);
				if(Util.isNumber(index) || index == ' ' || index == '|'){
					removeLength++;
				}else if(index != '\n'){
					break;
				}
			}
			removeString(start+1, removeLength);
		}
	}
	
	private void removeString(int indexStart, int length){
		try {
			consoleOutput.remove(indexStart, length);
			lineIndeces = Util.getNewLineIndeces(getText());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/*
	private void parseInput(){
		// TODO
	}
	*/
	public Console_d setLineMax(int lineMax){
		this.lineMax = lineMax;
		return this;
	}
	
	public Console_d setLogFileName(String name){
		this.logFileName = name;
		return this;
	}
	
	public Console_d setTextColor(Color color){
		textColor = color;
		logText.addAttribute(StyleConstants.CharacterConstants.Foreground, textColor.getJColor());
		return this;
	}
	
	private Console_d setWriteToFile(boolean canWrite){
		this.writeToFile = canWrite;
		return this;
	}
	
	public Console_d show(){
		consoleFrame.setVisible(true);
		return this;
	}
	
	public Console_d showLineNumbers(boolean showLineNumbers){
		this.showLineNumbers = showLineNumbers;
		updateNewLineIndeces();
		return this;
	}
	
	public Console_d start(){
		createWindow();
		show();
		return this;
	}
	
	private void updateNewLineIndeces(){
		lineIndeces = Util.getNewLineIndeces(getText());
		removeLineNumbers();
		if(showLineNumbers) addLineNumbers();
	}
	
	public Console_d warn(String msg){
		warnln(msg + "\n");
		return this;
	}
	
	public Console_d warnln(String msg){
		insertString(consoleOutput.getLength(), msg, warnText);
		updateNewLineIndeces();
		return this;
	}
	
	public Console_d writeToFile(){
		writeToFile(finalLogFileOffset);
		return this;
	}
	
	public Console_d writeToFile(long offset){
		try {
			byte[] output = getText().getBytes();
			RandomAccessFile tempFile = new RandomAccessFile(logFilePath.toFile(), "rws");
			tempFile.seek(offset);
			tempFile.write(output, 0, output.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
}
