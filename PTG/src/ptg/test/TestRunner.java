package ptg.test;

import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;

public class TestRunner {
	
	private static List<Test> tests = new ArrayList<Test>();

	public static void main(String... args){
		initialize();
		runTests();
	}
	
	public static void initialize(){
		
		tests.add(new Vec3fTest());
	}
	
	public static void runTests(){
		for(Test test:tests){
			test.run();
		}
	}
}
