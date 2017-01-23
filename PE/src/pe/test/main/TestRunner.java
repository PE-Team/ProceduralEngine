package pe.test.main;

import java.util.ArrayList;
import java.util.List;

import pe.test.testUnits.DemoTest;

public class TestRunner {
	
	protected List<Test> tests = new ArrayList<Test>();
	
	protected List<String> failures = new ArrayList<String>();
	protected List<String> errors = new ArrayList<String>();
	protected List<String> warnings = new ArrayList<String>();
	
	protected int predictedTestedClasses = 0, predictedTestUnits = 0, predictedTestCases = 0;
	protected int actualTestedClasses = 0;
	protected long startTime, time;
	
	public TestRunner(){}
	
	protected void addTests(){
		tests.add(new DemoTest());
	}

	public static void main(String... args){
		TestRunner testRunner = new TestRunner();
		testRunner.initialize();
		testRunner.runTests();
	}
	
	private void initialize(){
		addTests();
		
		calculatePredictedTestedClasses();
		calculatePredictedTestUnits();
		calculatePredictedTestCases();
	}

	private void runTests(){
		Test.msgTestStart(predictedTestedClasses, predictedTestUnits, predictedTestCases);
		startTime = System.currentTimeMillis();
		for(Test test:tests){
			test.run();
			actualTestedClasses++;
		}
		time = System.currentTimeMillis() - startTime;
		Test.msgTestEnd(actualTestedClasses, predictedTestedClasses, predictedTestUnits, time);
	}
	
	private void calculatePredictedTestedClasses() {
		predictedTestedClasses = tests.size();
	}
	
	private void calculatePredictedTestUnits(){
		for(Test test:tests){
			predictedTestUnits += test.getNumbTestUnits();
		}
	}
	
	private void calculatePredictedTestCases(){
		
	}
}
