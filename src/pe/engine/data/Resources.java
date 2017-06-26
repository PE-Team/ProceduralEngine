package pe.engine.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Resources {
	
	private static Map<Object, Set<DisposableResourceI>> resources = new HashMap<Object, Set<DisposableResourceI>>();
	private static Object currentKey = 0;
	private static boolean printOnDispose = false;
	
	public static void setCurrentKey(Object key){
		currentKey = key;
	}
	
	public static void setPrintOnDispose(boolean print){
		printOnDispose = print;
	}
	
	public static void add(DisposableResourceI resource){
		add(currentKey, resource);
	}
	
	public static void add(Object key, DisposableResourceI resource){
		if(!resources.containsKey(key)){
			Set<DisposableResourceI> resourceList = new HashSet<DisposableResourceI>();
			resources.put(key, resourceList);
		}
		resources.get(key).add(resource);
	}
	
	public static void dispose(){
		dispose(currentKey);
	}
	
	public static void dispose(Object key){
		for(DisposableResourceI dr:resources.get(key)){
			disposeObject(dr);
		}
		resources.remove(key);
	}
	
	public static void disposeAll(){
		for(Set<DisposableResourceI> drList:resources.values()){
			for(DisposableResourceI dr:drList){
				disposeObject(dr);
			}
		}
		for(Object key:resources.keySet()){
			resources.remove(key);
		}
	}
	
	private static void disposeObject(DisposableResourceI dr){
		if(printOnDispose)
			System.out.println(dr);
		dr.dispose();
	}
}
