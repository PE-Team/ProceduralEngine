package pe.engine.threading;

import pe.util.Timer;

public class AudioThread implements Runnable {

	private Timer timer;

	@Override
	public void run() {
		try {
			timer = new Timer(2.1f);
			timer.start();

			while (MasterThread.isRunning()) {
				if (timer.delayPassed()) {
					MasterThread.println("Audio Thread", "AudiO... AUDio... AUDIO!");
				}
			}
		} catch (Exception e) {
			MasterThread.println("Audio Thread",
					"An Exception has Occured Which Will Cause the Main Thread to Shutdown");
			e.printStackTrace(MasterThread.getConsolePrintStream());
			MasterThread.shutdown();
		}
	}
}
