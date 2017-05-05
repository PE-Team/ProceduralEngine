package pe.engine.threading;

import pe.util.Timer;

public class NetworkingThread implements Runnable{

	private Timer timer;

	@Override
	public void run() {
		timer = new Timer(2.9f);
		
		while(MasterThread.running()){
			if(timer.delayPassed()){
				MasterThread.println("Networking Thread", "In the Database, Database. Just livin in the Database. Wha Oh!");
			}
		}
	}
}
