package ptg.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ptg.engine.main.PTG;

public class Util {
	
	private static final String[] DIRECTORIES_TO_IGNORE = {".git"};
	
	public static int[] add(int[] array, int addend){
		for(int i = 0; i < array.length; i++){
			array[i] += addend;
		}
		return array;
	}

	public static String addSpaces(String msg, int stringLength){
		String result = "";
		for(int i = 0; i < stringLength - msg.length(); i++){
			result += " ";
		}
		return result + msg;
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
	
	public static String[] append(String[] array, String str){
		String[] result = new String[array.length + 1];
		for(int i = 0; i < array.length; i++){
			result[i] = array[i];
		}
		result[result.length-1] = str;
		return result;
	}
	
	public static List arrayToList(Object[] array){
		List result = new ArrayList<>();
		for(int i = 0; i < array.length; i++){
			result.add(array[i]);
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
	
	public static int[] copy(int[] array, int start, int end){
		if(array.length == 0) return array;
		int[] result = new int[end-start+1];
		for(int i = 0; i < result.length; i++){
			result[i] = array[i+start];
		}
		return result;
	}
	
	public static boolean existsChar(String string, char character){
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == character) return true;
		}
		return false;
	}

	public static List<Class<?>> getClassesByName(String className){
		className += ".class";
		
		List<Class<?>> possibleClasses = new ArrayList<Class<?>>();
		
		File baseDir = new File(".\\bin\\");
		File[] baseDirFiles = baseDir.listFiles();
		List<File> dirList = arrayToList(baseDirFiles);
		List<String> possibleClassPaths = new ArrayList<String>();
		
		while(dirList.size() > 0){
			int iterations = dirList.size();
			for(int f = 0; f < iterations; f++){
				File file = dirList.get(0);
				
				if(file.isDirectory()){
					File[] currentDir = file.listFiles();
					dirList.addAll(arrayToList(currentDir));
				}else if(file.isFile() && file.getName().equals(className)){
					possibleClassPaths.add(file.getPath());
				}
				dirList.remove(0);
			}
		}
		
		for(int i = 0; i < possibleClassPaths.size(); i++){
			String srcDir = "bin\\";
			String classExt = ".class";
			String possibleClassPath = possibleClassPaths.get(i);
			
			int indexSrcDir = getFirstIndexOf(srcDir,possibleClassPath);
			if(indexSrcDir < 0){
				possibleClassPaths.remove(i);
				continue;
			}
			
			possibleClassPath = possibleClassPath.substring(indexSrcDir + srcDir.length());
			
			possibleClassPath = possibleClassPath.replace(classExt, "");
			possibleClassPath = possibleClassPath.replace("\\", ".");
			possibleClassPaths.set(i, possibleClassPath);
		}
		
		for(String classPackage:possibleClassPaths){
			try {
				possibleClasses.add(Class.forName(classPackage));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return possibleClasses;
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
	
	public static int getFirstIndexOf(String strPart, String str){
		for(int i = 1; i < str.length() - strPart.length() + 1; i++){
			if(str.charAt(i) == strPart.charAt(0)){
				boolean fullString = true;
				for(int c = 1; c < strPart.length(); c++){
					if(str.charAt(i+c) != strPart.charAt(c)){
						fullString = false;
						break;
					}
				}
				if(fullString) return i;
			}
		}
		return -1;
	}
	
	public static int getFirstIndexAfter(int index, String strPart, String str){
		for(int i = index + 1; i < str.length() - strPart.length() + 1; i++){
			if(str.charAt(i) == strPart.charAt(0)){
				boolean fullString = true;
				for(int c = 1; c < strPart.length(); c++){
					if(str.charAt(i+c) != strPart.charAt(c)){
						fullString = false;
						break;
					}
				}
				if(fullString) return i;
			}
		}
		return -1;
	}
	
	public static int getLastIndexOf(String strPart, String str){
		return getLastIndexBefore(str.length(), strPart, str);
	}
	
	public static int getLastIndexBefore(int index, String strPart, String str){
		for(int i = index - strPart.length(); i >= 0; i--){
			if(str.charAt(i) == strPart.charAt(0)){
				boolean fullString = true;
				for(int c = 1; c < strPart.length(); c++){
					if(str.charAt(i+c) != strPart.charAt(c)){
						fullString = false;
						break;
					}
				}
				if(fullString) return i;
			}
		}
		return -1;
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
	
	public static boolean isInStringArray(String str, String[] array){
		for(int i = 0; i < array.length; i++){
			if(str.equals(array[i])) return true;
		}
		return false;
	}

	public static boolean isNumber(char character){
		for(int i = 0; i < PTG.CHARSET_NUMBERS.length; i++){
			if(PTG.CHARSET_NUMBERS[i] == character){
				return true;
			}
		}
		return false;
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
	
	public static String listToString(List<String> list){
		String result = "";
		for(int i = 0; i < list.size(); i++){
			result+=list.get(i);
		}
		return result;
	}
	
	public static List<Constructor<?>> parseConstructor(String constrStr){
		constrStr = constrStr.replace("new ", "");
		constrStr = constrStr.replace(" ", "");
		
		String constructorStr = "";
		System.out.println(constrStr + " | " + constructorStr);
		
		int paramNumb = 0;
		while(true){
			System.out.println(constrStr);
			int leftEndIndex = constrStr.length();
			int rightStartIndex = constrStr.length();
			
			for(int c = constrStr.length() - 1; c >= 0; c--){
				if(constrStr.charAt(c) == ')') leftEndIndex = c;
				if(constrStr.charAt(c) == '(') rightStartIndex = c;
				if(rightStartIndex < leftEndIndex){
					constrStr = constrStr.substring(0, rightStartIndex + 1) + "@p" + paramNumb + constrStr.substring(leftEndIndex);
					paramNumb++;
					break;
				}
			}
			break;
		}
		
		System.out.println(constrStr);
		
		List<Constructor<?>> possibleConstructors = new ArrayList<Constructor<?>>();
		
		return possibleConstructors;
	}
	
	public static boolean isConstructor(String constrStr){
		constrStr = constrStr.replace("new ", "");
		constrStr = constrStr.replace(" ", "");
		
		int firstBegParenth = getFirstIndexOf("(", constrStr);
		int lastEndParenth = getLastIndexOf(")", constrStr);
		
		int nextLastBegParenth = getFirstIndexAfter(firstBegParenth, "(", constrStr);
		int nextEndParenth = getLastIndexBefore(lastEndParenth, ")", constrStr);
		
		return (firstBegParenth > 0) && (firstBegParenth < lastEndParenth) && (nextLastBegParenth <= nextEndParenth) && isValidByCharset(constrStr.substring(0,firstBegParenth), PTG.CHARSET_NUMBERS_LETTERS);
	}
	
	public static boolean isValidByCharset(String str, char[] charset){
		for(int c = 0; c < str.length(); c++){
			boolean inCharset = false;
			for(int i = 0; i < charset.length; i++){
				if(str.charAt(c) == charset[i]){
					inCharset = true;
					break;
				}
			}
			if(!inCharset) return false;
		}
		return true;
	}
	
	public static String[] splitConstructorArguments(String constrArgStr){
		System.out.println(constrArgStr);
		constrArgStr = constrArgStr.replace("new ", "");
		constrArgStr = constrArgStr.replace(" ", "");
		
		String[] args = constrArgStr.split(",");
		String[] finalArgs = new String[0];
		for(int i = 0; i < args.length; i++){
			for(int l = 1; l <= args.length - i; l++){
				String possibleConstructor = "";
				for(int arg = 0; arg < l; arg++){
					possibleConstructor += "," + args[arg+i];
				}
				if(possibleConstructor.length() > 0) possibleConstructor = possibleConstructor.substring(1, possibleConstructor.length());
				
				if(isConstructor(possibleConstructor) || isNumber(possibleConstructor)){
					finalArgs = append(finalArgs, possibleConstructor);
					args = remove(args, i, l);
					i = 0;
					l = 0;
				}
			}
		}
		return finalArgs;
	}
	
	public static String[] remove(String[] array, int start, int length) {
		String[] temp = new String[array.length - length];
		int index = 0;
		for(int i = 0; i < start; i++){
			temp[index] = array[i];
			index++;
		}
		for(int i = start + length; i < array.length; i++){
			temp[index] = array[i];
			index++;
		}
		return temp;
	}

	public static Method parseMethod(){
		
		return null;
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
	
	public static String arrayToString(Object[] array){
		String result = "";
		for(int i = 0; i < array.length; i++){
			result += "_" + array[i].toString();
		}
		if(result.length() > 0) result = result.substring(1, result.length());
		return "[" + result + "]";
	}
}
