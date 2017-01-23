package pe.test.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pe.engine.main.PE;
import pe.util.Util;
import pe.util.converters.StringToOperationConverter;

public class TestUnit {

	private Method method;
	private Class<?> testedClass;
	private List<Class<?>> paramClasses;
	private List<Constructor<?>> constructors;
	private List<List<Class<?>>> constructorBreakDown;
	private String input, expected, expectCalc;
	
	private List<String> failures = new ArrayList<String>();
	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	
	private int failureReportType = PE.REPORT_TYPE_ALL;
	private int errorReportType = PE.REPORT_TYPE_ALL;
	private int warningReportType = PE.REPORT_TYPE_SUMMARY;
	private int testLength;
	private int[] testMultipliers;
	
	public TestUnit(Class<?> testedClass, String methodName, List<Class<?>> paramClasses) {
		this.testedClass = testedClass;
		this.paramClasses = paramClasses;
		this.method = Util.getMethod(testedClass, methodName, paramClasses);
	}
	
	public TestUnit(Class<?> testedClass, String methodName, List<Class<?>> paramClasses, List<Constructor<?>> constructors) {
		this.testedClass = testedClass;
		this.paramClasses = paramClasses;
		this.method = Util.getMethod(testedClass, methodName, paramClasses);
		this.constructors = constructors;
	}
	
	public void run(){
		Test.actualTestUnits++;
		breakDownConstructors();
		calculateTestLength();
		for(int test = 0; test < this.testLength; test++){
			Object[] params = setNumericalParameters(test);
			runTestUnitInstance(params);
			Test.actualTestCases++;
		}
	}
	
	public void runTestUnitInstance(Object... parameters){
		
		if(this.expectCalc != null) calculateExpected(parameters);
		
		parameters = rebuildConstructors(parameters);
		
		Object result = null;
		try {
			result = method.invoke(null, parameters);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println("COULD NOT INVOKE METHOD");
			System.out.println(Util.arrayToList(parameters).toString());
			e.printStackTrace();
		} catch(Exception e){
			System.err.println("DID NOT WORK");
		}
		input = Test.parse(result);
		
		if(!input.equals(expected)) System.out.println("FAILED: input: " + input + ", expected: " + expected);
		if(input.equals(expected)) System.out.println("SUCCESS: input: " + input + ", expected: " + expected);
	}
	
	private void breakDownConstructors(){
		boolean constructorExists = false;
		constructorBreakDown = new ArrayList<List<Class<?>>>();
		for(int i = 0; i < paramClasses.size(); i++){
			Class<?> param = paramClasses.get(i);
			if(param.isPrimitive()) continue;
			
			for(Constructor constructor:constructors){
				
				if(constructor.getName().equals(param.getName())){
					constructorExists = true;
					constructorBreakDown.add(paramClasses);
					List<Class<?>> newParams = Util.arrayToList(constructor.getParameterTypes());
					paramClasses = Util.insertList(newParams, paramClasses, i);
					break;
				}
			}
			
			if(!constructorExists){ 
				System.out.println("CONSTRUCTOR DOES NOT EXIST: " + param.getName());
				System.exit(3);
			}
		}
	}
	
	private Object[] rebuildConstructors(Object... parameters){
		if(constructorBreakDown == null) return parameters;
		
		for(int i = constructorBreakDown.size()-2; i >= 0; i--){
			for(int j = 0; j < constructorBreakDown.get(i).size(); j++){
				if(!constructorBreakDown.get(i).get(j).isPrimitive()){
					for(int k = 0; k < constructors.size(); k++){
						if(constructorBreakDown.get(i).get(j).getName().equals(constructors.get(k).getName())){
							int constParams = constructors.get(k).getParameterCount();
							Object[] params = new Object[constParams];
							for(int p = 0; p < constParams; p++){
								params[p] = parameters[j+p];
							}
							
							Object constr = null;
							
							try {
								constr = constructors.get(k).newInstance(params);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							List<Object> newParams = Util.arrayToList(parameters);
							newParams.set(j, constr);
							for(int l = 0; l < constParams-1; l++){
								newParams.remove(j+1);
							}
							parameters = newParams.toArray();
						}
					}
				}
			}
		}
		
		return parameters;
	}
	
	public Object[] setNumericalParameters(int test){
		Object[] parameters = new Object[paramClasses.size()];
		for(int i = 0; i < paramClasses.size(); i++){
			TestCase testCase = TestCase.getTestCase(paramClasses.get(i));
			int index = (int) (Math.floor(test/this.testMultipliers[i]))%testCase.size;
			parameters[i] = testCase.runMethod(index);
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
		this.testLength = 1;
		this.testMultipliers = new int[paramClasses.size()];
		for(int i = 0; i < paramClasses.size(); i++){
			this.testMultipliers[i] = this.testLength;
			this.testLength *= TestCase.getTestCase(paramClasses.get(i)).size;
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