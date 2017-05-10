package pe.engine.threading;

import pe.util.Timer;

public class AudioThread implements Runnable{
	
	private Timer timer;

	@Override
	public void run() {
		timer = new Timer(2.1f);
		
		while(MasterThread.isRunning()){
			if(timer.delayPassed()){
				MasterThread.println("Audio Thread", "AudiO... AUDio... AUDIO!");
			}
		}
	}
}
