package pe.engine.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Resources {
	
	private static Map<Object, Set<DisposableResource>> resources = new HashMap<Object, Set<DisposableResource>>();
	private static Object currentKey = 0;
	private static boolean printOnDispose = false;
	
	public static void setCurrentKey(Object key){
		currentKey = key;
	}
	
	public static void setPrintOnDispose(boolean print){
		printOnDispose = print;
	}
	
	public static void add(DisposableResource resource){
		add(currentKey, resource);
	}
	
	public static void add(Object key, DisposableResource resource){
		if(!resources.containsKey(key)){
			Set<DisposableResource> resourceList = new HashSet<DisposableResource>();
			resources.put(key, resourceList);
		}
		resources.get(key).add(resource);
	}
	
	public static void dispose(){
		dispose(currentKey);
	}
	
	public static void dispose(Object key){
		for(DisposableResource dr:resources.get(key)){
			disposeObject(dr);
		}
		resources.remove(key);
	}
	
	public static void disposeAll(){
		for(Set<DisposableResource> drList:resources.values()){
			for(DisposableResource dr:drList){
				disposeObject(dr);
			}
		}
		for(Object key:resources.keySet()){
			resources.remove(key);
		}
	}
	
	private static void disposeObject(DisposableResource dr){
		if(printOnDispose)
			System.out.println(dr);
		dr.dispose();
	}
}
