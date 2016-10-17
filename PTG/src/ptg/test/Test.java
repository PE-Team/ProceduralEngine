package ptg.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Test {
	
	private static List<String> testMethodNames = new ArrayList<String>();

	public void run(){
		Method[] methods = this.getClass().getDeclaredMethods();
		for(Method method:methods){
			if(!isTestMethod(method.getName())){
				try{
					method.invoke(this, null);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean isTestMethod(String string){
		for(String methodName:testMethodNames){
			if(string.equals(methodName)) return true;
		}
		return false;
	}
	
	public static void generateTestMethodNames(){
		Method[] methods = Test.class.getDeclaredMethods();
		for(Method method:methods){
			testMethodNames.add(method.getName());
		}
	}
}
