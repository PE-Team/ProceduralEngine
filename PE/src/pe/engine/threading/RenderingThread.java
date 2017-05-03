package pe.engine.threading;

import ep.testing.initial_program_setup2.Timer;

public class RenderingThread implements Runnable{
	
	private Timer timer;

	@Override
	public void run() {
		timer = new Timer(1.5f);
		timer.start();
		while(MasterThread.running()){
			if(timer.delayPassed()){
				MasterThread.println("Rendering Thread", "I am Rendering some stuff. Look at me!");
			}
		}
	}
}