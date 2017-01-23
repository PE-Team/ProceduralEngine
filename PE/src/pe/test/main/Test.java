package pe.test.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Test {

	// Unused
	private static List<String> testMethodNames = new ArrayList<String>();

	public static int actualTestUnits = 0, actualTestCases = 0;
	
	protected Class<?> testedClass;
	protected List<Class<?>> paramClasses = new ArrayList<Class<?>>();
	protected List<Constructor<?>> paramConstructors = new ArrayList<Constructor<?>>();
	
	public Test(Class<?> testedClass){
		this.testedClass = testedClass;
	}

	public void run() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				method.invoke(this, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			paramClasses.clear();
		}
	}
	
	public int getNumbTestUnits(){
		return this.getClass().getDeclaredMethods().length;
	}

	public static String parse(Object result) {
		// Parse NULL
		if(result == null) return "NULL OBJECT";
		// Parse Vec3f
		try{
			Method toString = result.getClass().getMethod("toString", null);
			return (String) toString.invoke(result, null);
		}catch(Exception e){
			// The parse method for Objects contained in other libraries go here
			
			// If there is not parse method made or the toString() method does not exist, this occurs
			System.err.println("NO SUCH METHOD toString() IN " + result.getClass().getSimpleName() + " WITH PARAM: NULL");
		}
		
		
		return null;
	}
	
	public static void msgTestStart(int tests, int testUnits, int testCases){
		System.out.println("Testing started | Test Classes: " + tests + tab(1) + "Test Units: " + testUnits );//+ tab(1) + "Test Cases: " + testCases);
	}
	
	public static void msgTestEnd(int tests, int predictTestClasses, int predictTestUnits, long time){
		System.out.println("Testing ended | Test Classes: " + tests + "/" + predictTestClasses + tab(1) + 
										   "Test Units: " + actualTestUnits + "/" + predictTestUnits + tab(1) + 
										   "Test Cases: " + actualTestCases + tab(1) + "Time: " + time + "ms");
	}
	
	private static String tab(int numb){
		String result = "";
		for(int i = 0; i < numb; i++){
			result += "    ";
		}
		return result;
	}
	
	// Returns true if the method is found in the Test class
	// Unused
	public static boolean isTestMethod(String string) {
		for (String methodName : testMethodNames) {
			if (string.equals(methodName))
				return true;
		}
		return false;
	}

	// Generates an arrayList including all of the names of the methods declared
	// in the Test class
	// Unused
	public static void generateTestMethodNames() {
		Method[] methods = Test.class.getDeclaredMethods();
		for (Method method : methods) {
			testMethodNames.add(method.getName());
		}
	}
}
