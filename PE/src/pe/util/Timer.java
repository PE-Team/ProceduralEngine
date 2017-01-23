package pe.util;

public class Timer{
	
	private long timeToWait;
	
	public Timer(){
		
	}
	
	public Timer(long timeToWait){
		this.timeToWait = timeToWait;
	}
	
	public Timer setWaitTime(long timeToWait){
		this.timeToWait = timeToWait;
		return this;
	}
	
	public Timer start(){
		try {
			Thread.sleep(timeToWait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
}
