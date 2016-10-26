package ptg.test.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;
import ptg.util.Util;
import ptg.util.converters.StringToOperationConverter;
import ptg.util.maths.Vec3f;

public class TestUnit {

	private Method method;
	private Class<?> testedClass;
	private List<Class<?>> paramClasses;
	private String input, expected, expectCalc;
	
	private List<String> failures = new ArrayList<String>();
	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	
	private int failureReportType = PTG.REPORT_TYPE_ALL;
	private int errorReportType = PTG.REPORT_TYPE_ALL;
	private int warningReportType = PTG.REPORT_TYPE_SUMMARY;
	private int testLength;
	
	public TestUnit(Class<?> testedClass, String methodName, List<Class<?>> paramClasses) {
		this.testedClass = testedClass;
		this.paramClasses = paramClasses;
		this.method = Util.getMethod(testedClass, methodName, paramClasses);
	}
	
	public void run(){
		calculateTestLength();
		// This will eventually be in a loop
		for(int test = 0; test < this.testLength; test++){
			Object[] params = setParameters(test);
			runTestUnitInstance(params);
		}
	}
	
	public void runTestUnitInstance(Object... parameters){
		
		if(this.expectCalc != null) calculateExpected(parameters);
		
		Object result = null;
		try {
			result = method.invoke(null, parameters);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println("COULD NOT INVOKE METHOD");
			e.printStackTrace();
		} catch(Exception e){
			System.err.println("DID NOT WORK");
		}
		input = Test.parse(result);
		if(!input.equals(expected)) System.out.println("FAILED: input: " + input + ", expected: " + expected);
		if(input.equals(expected)) System.out.println("SUCCESS: input: " + input + ", expected: " + expected);
	}
	
	public Object[] setParameters(int test){
		Object[] parameters = new Object[paramClasses.size()];
		for(int i = 0; i < paramClasses.size(); i++){
			// test will need to be changed such that each testcase is iterated through all of its cases as compared to all other test cases
			int index = test;
			parameters[i] = TestCase.getTestCase(paramClasses.get(i)).runMethod(index);
		}
		return parameters;
	}
	
	public void calculateExpected(Object... parameters){
		StringToOperationConverter operation = new StringToOperationConverter();
		operation.setOperationString(this.expectCalc);
		String[] params = new String[parameters.length];
		for(int i = 0; i < parameters.length; i++){
			params[i] = parameters[i].toString();
		}
		operation.setParameters(params);
		this.expected = operation.operateOnString();
	}
	
	public void calculateTestLength(){
		this.testLength = 0;
		for(int i = 0; i < paramClasses.size(); i++){
			this.testLength += TestCase.getTestCase(paramClasses.get(i)).size;
		}
	}
	
	public void setExpectedCalculation(String string){
		this.expectCalc = string;
	}
	
	public void setExpectedString(String string){
		this.expected = string;
	}
	
	public String getExpectedString(String string){
		return this.expected;
	}
	
	public void setFailureReportType(int reportType){
		this.failureReportType = reportType;
	}
	
	public void setErrorReportType(int reportType){
		this.errorReportType = reportType;
	}
	
	public void setWarningReportType(int reportType){
		this.warningReportType = reportType;
	}
}