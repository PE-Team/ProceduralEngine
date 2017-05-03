package pe.engine.threading;

import java.io.PrintStream;

import pe.util.Timer;
import pe.util.Util;

public class MasterThread{
	
	private static PrintStream console = System.out;
	private static Timer timer;
	
	private static ThreadManager threadManager;
	private static RenderingThread renderer;
	private static NetworkingThread networking;
	private static AudioThread audio;
	
	private static boolean running = false;
	
	public static void main(String[] args){
		timer = new Timer(1);
		timer.start();
		println("Main Thread", "Procedural Engine Main Thread Started");
		running = true;
		
		threadManager = new ThreadManager();
		renderer = new RenderingThread();
		networking = new NetworkingThread();
		audio = new AudioThread();
		
		threadManager.addTask(renderer);
		threadManager.addTask(networking);
		threadManager.addTask(audio);
		
		TestShutdownTask shutdownTask = new TestShutdownTask(15);
		
		threadManager.addTask(shutdownTask);
		
		for(int i = 0; i < 10; i++){
			addUpdate(new TestTask());
		}
		
		while(!threadManager.threadsTerminated()){
			if(timer.delayPassed()){
				println("Thread Manager", threadManager.getCurrentStatus());
			}
		}
		
		println("Main Thread", "Procedural Engine Main Thread Terminated");
	}
	
	public static boolean running(){
		return running;
	}
	
	public synchronized static void shutdown(){
		running = false;
		println("Master Thread", "Shutting down all processes");
	}
	
	public static void addUpdate(Runnable update){
		threadManager.addTask(update);
	}
	
	public static void println(String source, String msg){
		String time = String.format("[%f", timer.getTime());
		String src = String.format("%s]:", source);
		console.println(String.format("%s\t%s", Util.alignStrings(time, "", src, 35), msg));
	}
}
