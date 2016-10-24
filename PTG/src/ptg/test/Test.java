package ptg.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;
import ptg.util.maths.Vec3f;

public abstract class Test {

	// Unused
	private static List<String> testMethodNames = new ArrayList<String>();
	
	protected Class<?> testedClass;
	protected List<Class<?>> paramClasses = new ArrayList<Class<?>>();
	
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
		}
	}
	
	/*
	 * In the future this needs to invoke a .toString() method
	 * for the class. If it doesn't exist and it is part of this
	 * project, it must be created. If it is part of a library, 
	 * then the method for parsing in will reside in this class
	 * under the name of parse(Object name)
	 */
	public static String parse(Object result) {
		// Parse NULL
		if(result.equals(null)) return "NULL OBJECT";
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
	
	public static Method getMethod(Class<?> testedClass, String name, List<Class<?>> paramClasses){
		try {
			return testedClass.getDeclaredMethod(name, paramClasses.toArray(new Class<?>[0]));
		} catch (Exception e) {
			System.err.println("NO SUCH METHOD FOUND");
			e.printStackTrace();
		}
		return null;
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
