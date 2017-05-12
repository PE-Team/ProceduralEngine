package pe.engine.threading;

import pe.util.Timer;

public class NetworkingThread implements Runnable {

	private Timer timer;

	@Override
	public void run() {
		try {
			timer = new Timer(2.9f);
			timer.start();

			while (MasterThread.isRunning()) {
				if (timer.delayPassed()) {
					MasterThread.println("Networking Thread",
							"In the Database, Database. Just livin in the Database. Wha Oh!");
				}
			}
		} catch (Exception e) {
			MasterThread.println("Networking Thread",
					"An Exception has Occured Which Will Cause the Main Thread to Shutdown");
			e.printStackTrace(MasterThread.getConsoleStream());
			MasterThread.shutdown();
		}
	}
}
