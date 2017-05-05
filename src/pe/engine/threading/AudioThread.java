package pe.engine.threading;

import ep.testing.initial_program_setup2.Timer;

public class AudioThread implements Runnable{
	
	private Timer timer;

	@Override
	public void run() {
		timer = new Timer(2.1f);
		
		while(MasterThread.running()){
			if(timer.delayPassed()){
				MasterThread.println("Audio Thread", "AudiO... AUDio... AUDIO!");
			}
		}
	}
}
