package ptg.test.main;

import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;
import ptg.test.testUnits.DemoTest;
import ptg.test.testUnits.Vec3fTest;

public class TestRunner {
	
	private static List<Test> tests = new ArrayList<Test>();

	public static void main(String... args){
		initialize();
		runTests();
	}
	
	public static void initialize(){
		
		tests.add(new DemoTest());
	}
	
	public static void runTests(){
		for(Test test:tests){
			test.run();
		}
	}
}
