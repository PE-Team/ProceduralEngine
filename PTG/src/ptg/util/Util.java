package ptg.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;

public class Util {
	
	public static String addSpaces(String msg, int stringLength){
		String result = "";
		for(int i = 0; i < stringLength - msg.length(); i++){
			result += " ";
		}
		return result + msg;
	}

	public static boolean isNumber(String string){
		boolean decimal = false;
		for(int i = 1; i < string.length(); i++){
			char numb = string.charAt(i);
			if((!Util.isNumber(numb)) && (numb != '.' || decimal)){
				return false;
			}else if(string.charAt(i) == '.'){
				decimal = true;
			}
		}
		char posneg = string.charAt(0);
		if(!isNumber(posneg) && ((posneg != '+' && posneg != '-') || string.length() <= 1)) return false;
		return true;
	}

	public static boolean isNumber(char character){
		for(int i = 0; i < PTG.CHARSET_NUMBERS.length; i++){
			if(PTG.CHARSET_NUMBERS[i] == character){
				return true;
			}
		}
		return false;
	}
	
	public static int[] getNewLineIndeces(String string){
		int[] result = new int[0];
		int[] old = result;
		for(int i = 0 ; i < string.length(); i++){
			if(string.charAt(i) == '\n'){
				old = result;
				result = new int[old.length+1];
				for(int j = 0; j < old.length; j++){
					result[j] = old[j];
				}
				result[old.length] = i;
			}
		}
		return result;
	}
	
	public static int[] add(int[] array, int addend){
		for(int i = 0; i < array.length; i++){
			array[i] += addend;
		}
		return array;
	}
	
	public static int[] copy(int[] array, int start, int end){
		if(array.length == 0) return array;
		int[] result = new int[end-start+1];
		for(int i = 0; i < result.length; i++){
			result[i] = array[i+start];
		}
		return result;
	}
	
	public static int[] append(int[] array, int numb){
		int[] result = new int[array.length + 1];
		for(int i = 0; i < array.length; i++){
			result[i] = array[i];
		}
		result[result.length-1] = numb;
		return result;
	}
	
	public static int[] append(int[] array1, int[] array2){
		int[] result = new int[array1.length+array2.length];
		for(int i = 0; i < array1.length; i++){
			result[i] = array1[i];
		}
		for(int i = 0; i < array2.length; i++){
			result[i+array1.length] = array2[i];
		}
		return result;
	}

	public static List cloneList(List list1, List list2){
		list1.clear();
		for(int i = 0; i < list2.size(); i++){
			list1.add(list2.get(i));
		}
		return list1;
	}

	public static List shrinkList(List list, int min, int max){
		int size = list.size();
		for(int i = 0; i < size-max-1; i++){
			list.remove(max+1);
		}
		for(int i = 0; i < min; i++){
			list.remove(0);
		}
		return list;
	}
	
	public static List arrayToList(Object[] array){
		List result = new ArrayList<>();
		for(int i = 0; i < array.length; i++){
			result.add(array[i]);
		}
		return result;
	}
	
	public static List insertList(List listInsert, List base, int index){
		List result = new ArrayList<>();
		for(int i = 0; i < base.size(); i++){
			if(i != index){
				result.add(base.get(i));
			}else{
				for(int j = 0; j < listInsert.size(); j++){
					result.add(listInsert.get(j));
				}
			}
		}
		return result;
	}

	public static String listToString(List<String> list){
		String result = "";
		for(int i = 0; i < list.size(); i++){
			result+=list.get(i);
		}
		return result;
	}

	public static boolean existsChar(String string, char character){
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == character) return true;
		}
		return false;
	}
	
	public static Constructor<?> getConstructor(Class<?> className, List<Class<?>> initClasses){
		try {
			return className.getConstructor(initClasses.toArray(new Class<?>[0]));
		} catch (Exception e) {
			System.err.println("NO SUCH CONSTRUCTOR FOUND");
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Class<?> className, String name, List<Class<?>> paramClasses){
		try {
			return className.getDeclaredMethod(name, paramClasses.toArray(new Class<?>[0]));
		} catch (Exception e) {
			System.err.println("NO SUCH METHOD FOUND");
			e.printStackTrace();
		}
		return null;
	}

}
