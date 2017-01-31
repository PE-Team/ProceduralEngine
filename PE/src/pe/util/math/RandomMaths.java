package pe.util.math;

import java.util.Random;

public class RandomMaths {

	private static Random random = new Random();
	
	public static int randInt(int min, int max){
		return random.nextInt()*(max-min) + min;
	}
	
	public static float randFloat(float min, float max){
		return random.nextFloat()*(max-min) + min;
	}
	
	public static long randLong(long min, long max){
		return random.nextLong()*(max-min) + min;
	}
	
	public static double randDouble(double min, double max){
		return random.nextDouble()*(max-min) + min;
	}
	
	public static short randShort(short min, short max){
		return (short) randInt(min, max);
	}
	
	public static boolean randBoolean(){
		return random.nextBoolean();
	}
	
	public static char randChar(char[] charset){
		return charset[randInt(0,charset.length-1)];
	}
	
	public static char randChar(int min, int max, char[] charset){
		return charset[randInt(min, max)];
	}
}
