package pe.test.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pe.test.testCases.BooleanTestCase;
import pe.test.testCases.ByteTestCase;
import pe.test.testCases.CharacterTestCase;
import pe.test.testCases.DoubleTestCase;
import pe.test.testCases.FloatTestCase;
import pe.test.testCases.IntTestCase;
import pe.test.testCases.LongTestCase;
import pe.test.testCases.ShortTestCase;

public class TestCase {
	
	private static TestCase byteTest = new ByteTestCase();
	private static TestCase shortTest = new ShortTestCase();
	private static TestCase intTest = new IntTestCase();
	private static TestCase longTest = new LongTestCase();
	private static TestCase floatTest = new FloatTestCase();
	private static TestCase doubleTest = new DoubleTestCase();
	private static TestCase booleanTest = new BooleanTestCase();
	private static TestCase charTest = new CharacterTestCase();
	
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
		if(paramClass == byte.class) return byteTest;
		if(paramClass == short.class) return shortTest;
		if(paramClass == int.class) return intTest;
		if(paramClass == long.class) return longTest;
		if(paramClass == float.class) return floatTest;
		if(paramClass == double.class) return doubleTest;
		if(paramClass == boolean.class) return booleanTest;
		if(paramClass == char.class) return charTest;
		return null;
	}
}
