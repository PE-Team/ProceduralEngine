package ptg.util.console;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ptg.util.Timer;
import ptg.util.color.Color;
import ptg.util.Util;

public class Console {
	
	private String logFileName;
	private int lineMax;
	private List<String> consoleLines = new ArrayList<String>();
	private Color textColor;
	
	private JFrame consoleFrame;
	private JPanel consolePanel;
	private JTextPane consoleOutputPane;
	private StyledDocument consoleOutput;
	private Font consoleFont;
	private JScrollPane consoleOutputScrollbar;
	
	private SimpleAttributeSet logText, errText, warnText, sucText;
	
	private double ratioWidth, ratioHeight;
	
	public int linePointer;

	public Console(){
		this.logFileName = "defaultConsoleLogFile.log";
		this.lineMax = 10;
		this.textColor = Color.BLACK;
	}
	
	public static void main(String...args){
		Console console = new Console().start();
		Timer timer = new Timer();
		
		console.logln("Well hello");
		timer.setWaitTime(200).start();
		console.warnln("I will be leaving soon");
		timer.setWaitTime(200).start();
		console.errln("Very, VERY soon");
		timer.setWaitTime(200).start();
		console.logln("Bye bye!");
		timer.setWaitTime(50).start();
		console.clearln(3);
	}
	
	private void initAttribs(){
		Object fontForground = StyleConstants.CharacterConstants.Foreground;
		Object fontSize = StyleConstants.CharacterConstants.FontSize;
		Object fontFamily = StyleConstants.CharacterConstants.FontFamily;
		
		int defaultTextSize = mulRatio(36, ratioHeight);
		String defaultFontFamily = "Verdana";
		
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
		
		// SCROLLBAR
		consoleOutputScrollbar = new JScrollPane(consoleOutputPane);
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
	
	public Console overrideln(String msg){
		overrideln(linePointer, msg);
		return this;
	}
	
	public Console overrideln(int lineNumber, String msg){
		
		return this;
	}
	
	public Console overrideChar(int index){
		
		return this;
	}
	
	public Console overrideChars(int start, int end){
		for(int i = start; i < end; i++){
			overrideChar(i);
		}
		return this;
	}
	
	private void createLogFile(String fileName){
		logFileName = fileName;
	}
	
	public Console logToFile(){
		
		return this;
	}
	
	public Console clear(){
		//TODO logs all of the console to the logFile
		try {
			consoleOutput.remove(0, consoleOutput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console clear(int indexStart, int indexEnd){
		try {
			consoleOutput.remove(indexStart, indexEnd-indexStart);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console clearln(){
		clearln(linePointer);
		return this;
	}
	
	public Console clearln(int lineNumber){
		int[] lineIndeces = Util.getNewLineIndeces(getText());
		clear(lineIndeces[lineNumber-2], lineIndeces[lineNumber-1]);
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
	
	public String getText(){
		try {
			return consoleOutput.getText(0, consoleOutput.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Console log(String msg){
		try {
			consoleOutput.insertString(consoleOutput.getLength(), msg, logText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console logln(String msg){
		log(msg + "\n");
		return this;
	}
	
	public Console err(String msg){
		try {
			consoleOutput.insertString(consoleOutput.getLength(), msg, errText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console errln(String msg){
		err(msg + "\n");
		return this;
	}
	
	public Console warn(String msg){
		try {
			consoleOutput.insertString(consoleOutput.getLength(), msg, warnText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console warnln(String msg){
		warn(msg + "\n");
		return this;
	}
	
	public Console logSuccess(String msg){
		try {
			consoleOutput.insertString(consoleOutput.getLength(), msg, sucText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Console logSuccessln(String msg){
		logSuccess(msg + "\n");
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
		logText.addAttribute(StyleConstants.CharacterConstants.Foreground, textColor.getJColor());
		return this;
	}
}
