package ptg.util.console;

import javax.swing.text.SimpleAttributeSet;

public class ConsoleQueue {
	
	private String queueMessage;
	private int offset;
	private boolean isRemoval;
	private int length;
	private SimpleAttributeSet attrib;
	
	public ConsoleQueue(int offset, String msg, SimpleAttributeSet attrib){
		this.queueMessage = msg;
		this.offset = offset;
		this.attrib = attrib;
		this.isRemoval = false;
		this.length = msg.length();
	}
	
	public ConsoleQueue(int offset, int length){
		this.queueMessage = null;
		this.offset = offset;
		this.attrib = null;
		this.isRemoval = true;
		this.length = length;
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
}
