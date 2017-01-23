package pe.util.console;

import javax.swing.text.SimpleAttributeSet;

public class ConsoleQueue {
	
	private String queueMessage;
	private int offset;
	private boolean isRemoval;
	private int length;
	private SimpleAttributeSet attrib;
	private boolean offsetAsLineNumber;
	
	public ConsoleQueue(int offset, boolean isLineNumber, String msg, SimpleAttributeSet attrib){
		this.queueMessage = msg;
		this.offset = offset;
		this.attrib = attrib;
		this.isRemoval = false;
		this.length = msg.length();
		this.offsetAsLineNumber = isLineNumber;
	}
	
	public ConsoleQueue(int offset, boolean isLineNumber, int length){
		this.queueMessage = null;
		this.offset = offset;
		this.attrib = null;
		this.isRemoval = true;
		this.length = length;
		this.offsetAsLineNumber = isLineNumber;
	}
	
	public String getMessage(){
		return this.queueMessage;
	}
	
	public int getOffset(){
		return this.offset;
	}
	
	public boolean isRemoval(){
		return this.isRemoval;
	}
	
	public int length(){
		return this.length;
	}
	
	public SimpleAttributeSet getAttrib(){
		return this.attrib;
	}
	
	public boolean isOffsetAsLineNumber(){
		return this.offsetAsLineNumber;
	}
}
