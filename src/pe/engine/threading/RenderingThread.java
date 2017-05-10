package pe.engine.threading;

import pe.util.Timer;

public class RenderingThread implements Runnable{
	
	private Timer timer;

	@Override
	public void run() {
		timer = new Timer(1.5f);
		timer.start();
		while(MasterThread.isRunning()){
			if(timer.delayPassed()){
				MasterThread.println("Rendering Thread", "I am Rendering some stuff. Look at me!");
			}
		}
	}
}
