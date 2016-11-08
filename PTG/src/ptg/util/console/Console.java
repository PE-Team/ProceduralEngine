package ptg.util.console;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.SimpleAttributeSet;

public class Console implements Runnable{

	private boolean running = false;
	private Thread consoleThread;
	
	private List<ConsoleQueue> queues = new ArrayList<ConsoleQueue>();
	private SimpleAttributeSet logText, errText, warnText, sucText;
	
	public static void main(String...args){
		Console console = new Console();
	}
	
	public Console(){
		consoleThread = new Thread(this);
		this.running = true;
	}
	
	public void run() {
		while(running){
			
		}
	}
	
	public synchronized Console start(){
		consoleThread.start();
		return this;
	}
	
	private void queue(int offset, String msg, SimpleAttributeSet attrib, int length, boolean isRemoval){
		if(isRemoval){
			queues.add(new ConsoleQueue(offset, length));
		}else{
			queues.add(new ConsoleQueue(offset, msg, attrib));
		}
	}

	private void sortQueue(){
		List<ConsoleQueue> newQueue = new ArrayList<ConsoleQueue>();
		int iterations = queues.size();
		int largestOffset = -1;
		int largestOffsetIndex = -1;
		for(int i = 0; i < iterations; i++){
			largestOffset = -1;
			for(int q = 0; q < queues.size(); q++){
				ConsoleQueue queue = queues.get(q);
				if(queue.getOffset() > largestOffset || (queue.isRemoval() && queue.getOffset() >= largestOffset)){ 
					largestOffset = queue.getOffset();
					largestOffsetIndex = q;
				}
			}
			
			newQueue.add(queues.get(largestOffsetIndex));
			queues.remove(largestOffsetIndex);
		}
	}
	
	private void consolidateRemovalsInQueue(){
		for(int q = 0; q + 1 < queues.size(); q++){
			ConsoleQueue currentQueue = queues.get(q);
			ConsoleQueue futureQueue = queues.get(q+1);
			if(currentQueue.isRemoval() && futureQueue.isRemoval() && currentQueue.getOffset() == futureQueue.getOffset()){
				ConsoleQueue longerRemoval = currentQueue.length() >= futureQueue.length() ? currentQueue : futureQueue;
				queues.set(q, longerRemoval);
				queues.remove(q+1);
			}
		}
	}
	
	public synchronized Console log(String msg){
		
		return this;
	}
}
