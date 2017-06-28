package pe.engine.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Resources {

	private static Map<Object, Set<DisposableResourceI>> resources = new HashMap<Object, Set<DisposableResourceI>>();
	private static Object currentKey = 0;
	private static boolean printOnDispose = false;

	public static synchronized void setCurrentKey(Object key) {
		currentKey = key;
	}

	public static synchronized void setPrintOnDispose(boolean print) {
		printOnDispose = print;
	}

	public static synchronized void add(DisposableResourceI resource) {
		add(currentKey, resource);
	}

	public static synchronized void add(Object key, DisposableResourceI resource) {
		if (!resources.containsKey(key)) {
			Set<DisposableResourceI> resourceList = new HashSet<DisposableResourceI>();
			resources.put(key, resourceList);
		}
		resources.get(key).add(resource);
	}

	public static synchronized void dispose() {
		dispose(currentKey);
	}

	public static synchronized void dispose(Object key) {
		for (DisposableResourceI dr : resources.get(key)) {
			disposeResource(dr);
		}
		resources.remove(key);
	}

	public static synchronized void disposeAll() {
		Iterator<Object> resourcesIterator = resources.keySet().iterator();
		while(resourcesIterator.hasNext()){
			for (DisposableResourceI dr : resources.get(resourcesIterator.next())) {
				disposeResource(dr);
			}
			resourcesIterator.remove();
		}
	}

	private static synchronized void disposeResource(DisposableResourceI dr) {
		if (printOnDispose)
			System.out.println(dr);
		dr.dispose();
	}
}
