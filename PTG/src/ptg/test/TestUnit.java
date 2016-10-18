package ptg.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;
import ptg.util.maths.Vec3f;

public class TestUnit {

	private Method method;
	private Class<?> testedClass;
	private List<Class<?>> paramClasses;
	private String input, expected;
	
	private List<String> failures = new ArrayList<String>();
	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	
	private int failureReportType = PTG.REPORT_TYPE_ALL;
	private int errorReportType = PTG.REPORT_TYPE_ALL;
	private int warningReportType = PTG.REPORT_TYPE_SUMMARY;
	
	public TestUnit(Class<?> testedClass, String methodName, List<Class<?>> paramClasses) {
		this.testedClass = testedClass;
		this.paramClasses = paramClasses;
		this.method = Test.getMethod(testedClass, methodName, paramClasses);
	}
	
	public void run(){
		runTestCase(new Vec3f(), new Vec3f());
	}
	
	public void run(Object obj){
		// result = method.invoke(obj, new Object(), new Object());
	}
	
	public void runTestCase(Object... parameters){
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
		if(!input.equals(expected)) System.out.println("input: " + input + ", expected: " + expected);
	}
	
	public void setExpectedCalculation(String string){
		
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