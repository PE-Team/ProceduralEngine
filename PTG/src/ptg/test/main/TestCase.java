package ptg.test.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ptg.test.testCases.FloatTestCase;

public class TestCase {
	
	private static TestCase floatTest = new FloatTestCase();
	
	private List<Method> testMethods = new ArrayList<Method>();
	public int size;
	
	public TestCase(){
		addMethods();
		this.size = testMethods.size();
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
	
	protected void addMethods(){
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			testMethods.add(method);
		}
	}
	
	public Object runMethod(int index){
		Method method = testMethods.get(index);
		try{
			return method.invoke(this, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static TestCase getTestCase(Class<?> paramClass){
		if(paramClass == float.class) return floatTest;
		return null;
	}
}
