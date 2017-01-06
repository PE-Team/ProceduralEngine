package ptg.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ptg.engine.main.PTG;

public class Util {
	
	public static int[] add(int[] array, int addend){
		for(int i = 0; i < array.length; i++){
			array[i] += addend;
		}
		return array;
	}
	
	public static int addAll(int[] array){
		int count = 0;
		for(int i = 0; i < array.length; i++){
			count += array[i];
		}
		return count;
	}
	
	public static String addSpaces(String msg, int stringLength){
		String result = "";
		for(int i = 0; i < stringLength - msg.length(); i++){
			result += " ";
		}
		return result + msg;
	}

	public static boolean allCharsMatch(char charRight, char charLeft, String statementStr){
		int rightCharNumb = 0;
		int leftCharNumb = 0;
		for(int c = 0; c < statementStr.length(); c++){
			if(statementStr.charAt(c) == charRight) rightCharNumb++;
			if(statementStr.charAt(c) == charLeft) leftCharNumb++;
			if(leftCharNumb > rightCharNumb) return false;
		}
		if(rightCharNumb != leftCharNumb) return false;
		return true;
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
	
	public static <T> List<T> arrayToList(T[] array){
		List<T> result = new ArrayList<>();
		if(array == null) return result;
		for(int i = 0; i < array.length; i++){
			result.add(array[i]);
		}
		return result;
	}
	
	public static <T> String arrayToString(T[] array){
		String result = "";
		for(int i = 0; i < array.length; i++){
			result += "," + array[i].toString();
		}
		if(result.length() > 0) result = result.substring(1, result.length());
		return "[" + result + "]";
	}
	
	public static <T> List<T> cloneList(List<T> list1, List<T> list2){
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
	
	public static int count(String strPart, String str){
		int result = 0;
		
		for(int c = 0; c < str.length() - strPart.length() + 1; c++){
			if(str.charAt(c) == strPart.charAt(0)){
				boolean fullString = true;
				for(int i = 1; i < strPart.length(); i++){
					if(str.charAt(c+i) != strPart.charAt(i)){
						fullString = false;
						break;
					}
				}
				if(fullString) result++;
			}
		}
		
		return result;
	}
	
	public static int countPrimitiveNumberClassesIn(List<Class<?>> list){
		int count = 0;
		for(int c = 0; c < list.size(); c++){
			if(		list.get(c).equals(Double.class) || list.get(c).equals(double.class) ||
					list.get(c).equals(Long.class) || list.get(c).equals(long.class) ||
					list.get(c).equals(Integer.class) || list.get(c).equals(int.class) ||
					list.get(c).equals(Float.class) || list.get(c).equals(float.class) ||
					list.get(c).equals(Short.class) || list.get(c).equals(short.class) ||
					list.get(c).equals(Byte.class) || list.get(c).equals(byte.class)
				) count++;
		}
		return count;
	}

	public static boolean existsChar(String string, char character){
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == character) return true;
		}
		return false;
	}

	public static Class<?> getClassByName(String className, String[] classFileLocations){
		boolean identifiedSpecificClass = false;
		int index = 0;
		
		int colonIndex = Util.getFirstIndexOf(":", className);
		if(colonIndex != -1){
			index = Integer.parseInt(className.substring(0, colonIndex));
			className = className.substring(colonIndex + 1);
			identifiedSpecificClass = true;
		}
		
		List<Class<?>> listClasses = getClassesByName(className, classFileLocations);
		
		if((listClasses.size() > 1 && !identifiedSpecificClass) || index >= listClasses.size()){
			String errMsg = "There is more than one possible class with the name " + listClasses + "\n";
			for(int pc = 0; pc < listClasses.size(); pc++){
				errMsg += "\t" + pc + ": " + listClasses.get(pc).getName() + "\n";
			}
			errMsg += "\nNext time, call the correct class by using 'number_from_above:Class_Name'\n\tEx: '0:Scanner' will return java.util.Scanner.class";
			throw new IllegalArgumentException(errMsg);
		}
		
		return listClasses.get(index);
	}
	
	public static List<Class<?>> getClassesByName(String className, String[] classFileLocations){
		
		List<Class<?>> possibleClasses = new ArrayList<Class<?>>();
		
		File baseDir;
		File[] baseDirFiles;
		List<File> dirList;
		List<String> possibleClassPaths = new ArrayList<String>();
		
		for(String fileLocation:classFileLocations){
			baseDir = new File(fileLocation);
			baseDirFiles = baseDir.listFiles();
			dirList = arrayToList(baseDirFiles);
			
			while(dirList.size() > 0){
				int iterations = dirList.size();
				for(int f = 0; f < iterations; f++){
					File file = dirList.get(0);
					
					if(file.isDirectory()){
						File[] currentDir = file.listFiles();
						dirList.addAll(arrayToList(currentDir));
					}else if(file.getName().length() > 4 && file.getName().substring(file.getName().length() - 4,file.getName().length()).equals(".zip")){
						
						try {
						    ZipFile zipFile = new ZipFile(file.getAbsolutePath());
						    Enumeration<? extends ZipEntry> entries = zipFile.entries();
						    while (entries.hasMoreElements()) {
						        ZipEntry entry = entries.nextElement();
						        int lastDirIndex = Util.getLastIndexOf("/", entry.getName());
						        String output = lastDirIndex != -1 ? entry.getName().substring(lastDirIndex + 1) : entry.getName();
						        if(output.equals(className + ".class") || output.equals(className + ".java")){
						        	
						        	String[] classExt = new String[] {".class",".java"};
									String possibleClassPath = entry.getName();
									
									possibleClassPath = possibleClassPath.replace(classExt[0], "");
									possibleClassPath = possibleClassPath.replace(classExt[1], "");
									possibleClassPath = possibleClassPath.replace("\\", ".");
									possibleClassPath = possibleClassPath.replace("/", ".");
									
									possibleClassPaths.add(possibleClassPath);
						        }
						    }
						    zipFile.close();
						} catch (IOException e) {
						    System.out.println("Error opening Zip" +e);
						}
						
					}else if(file.isFile() && (file.getName().equals(className + ".class") || file.getName().equals(className + ".java"))){
						
						String srcDir = fileLocation.replace("./", "").replace(".\\", "").replace("\\", "").replace("/", "");
						String[] classExt = new String[] {".class",".java"};
						String possibleClassPath = file.getPath();
						
						int indexSrcDir = getFirstIndexOf(srcDir,possibleClassPath);
						
						if(indexSrcDir >= 0) possibleClassPath = possibleClassPath.substring(indexSrcDir + srcDir.length() + 1);
						
						possibleClassPath = possibleClassPath.replace(classExt[0], "");
						possibleClassPath = possibleClassPath.replace(classExt[1], "");
						possibleClassPath = possibleClassPath.replace("\\", ".");
						possibleClassPath = possibleClassPath.replace("/", ".");
						
						possibleClassPaths.add(possibleClassPath);
					}
					dirList.remove(0);
				}
			}
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
			String constructor = className.getName() + "(";
			for(int c = 0; c < initClasses.size(); c++){
				constructor += initClasses.get(c).getSimpleName() + ", ";
			}
			constructor = Util.removeLast(", ", constructor);
			constructor += ")";
			throw new NullPointerException("No such constructor: " + constructor);
		}
	}
	
	public static List<Constructor<?>> getConstructors(Class<?> className, List<Class<?>> initClasses){
		List<Constructor<?>> possibleConstructors = new ArrayList<Constructor<?>>();
		List<Integer> possibleConstructorProximity = new ArrayList<Integer>();
		List<Class<?>> initClassesCopy = new ArrayList<Class<?>>();
		initClassesCopy.addAll(initClasses);
		//6
		
		int[] primitiveTryIndex = new int[countPrimitiveNumberClassesIn(initClasses)];
		int[] primitiveClassIndeces = getPrimitiveNumberClassIndeces(initClasses);
		int[] proximities = new int[primitiveTryIndex.length];
		
		for(int i = 0; i < primitiveClassIndeces.length; i++){
			Class<?> primClass = initClasses.get(i);
			
			if(primClass.equals(Double.class) || primClass.equals(double.class)) proximities[i] = 0;
			if(primClass.equals(Long.class) || primClass.equals(long.class)) proximities[i] = 1;
			if(primClass.equals(Integer.class) || primClass.equals(int.class)) proximities[i] = 2;
			if(primClass.equals(Float.class) || primClass.equals(float.class)) proximities[i] = 3;
			if(primClass.equals(Short.class) || primClass.equals(short.class)) proximities[i] = 4;
			if(primClass.equals(Byte.class) || primClass.equals(byte.class)) proximities[i] = 5;
		
		}
		
		while(Util.addAll(primitiveTryIndex) < primitiveTryIndex.length * 5){
			for(int i = 0; i < primitiveTryIndex.length; i++){
				Class<?> primitiveClass = null;
				switch(primitiveTryIndex[i]){
				case 0:
					primitiveClass = double.class;
					break;
				case 1:
					primitiveClass = float.class;
					break;
				case 2:
					primitiveClass = long.class;
					break;
				case 3:
					primitiveClass = int.class;
					break;
				case 4:
					primitiveClass = short.class;
					break;
				case 5:
					primitiveClass = byte.class;
					break;
				}
				
				initClassesCopy.set(primitiveClassIndeces[i], primitiveClass);
			}
			
			try{
				possibleConstructors.add(getConstructor(className, initClassesCopy));
				possibleConstructorProximity.add(Math.abs(Util.addAll(Util.subtract(proximities, primitiveTryIndex))));
			}catch(Exception e){}
			
			for(int i = 0; i < primitiveTryIndex.length; i++){
				primitiveTryIndex[i]++;
				if(primitiveTryIndex[i] < 6) break;
				primitiveTryIndex[i] = 0;
			}
		}
		
		if(possibleConstructors.isEmpty()){
			String constructor = className.getName() + "(";
			for(int c = 0; c < initClasses.size(); c++){
				constructor += initClasses.get(c).getSimpleName() + ",";
			}
			constructor = Util.removeLast(",", constructor);
			constructor += ")";
			throw new NullPointerException("No constructor like: " + constructor + " exists");
		}
		
		List<Constructor<?>> finalConstructors = new ArrayList<Constructor<?>>();
		
		while(!possibleConstructors.isEmpty()){
			int smallestNumb = 6;
			int smallestNumbIndex = -1;
			for(int i = 0; i < possibleConstructors.size(); i++){
				if(possibleConstructorProximity.get(i) < smallestNumb){
					smallestNumb = possibleConstructorProximity.get(i);
					smallestNumbIndex = i;
				}
			}
			
			finalConstructors.add(possibleConstructors.get(smallestNumbIndex));
			possibleConstructors.remove(smallestNumbIndex);
			possibleConstructorProximity.remove(smallestNumbIndex);
		}
		
		return finalConstructors;
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

	public static int getLastIndexOf(String strPart, String str){
		return getLastIndexBefore(str.length(), strPart, str);
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
	
	public static Class<?> getPrimitiveClassOf(Object obj){
		Class<?> objClass = obj.getClass();
		if(objClass.equals(Double.class)) return double.class;
		if(objClass.equals(Long.class)) return long.class;
		if(objClass.equals(Integer.class)) return int.class;
		if(objClass.equals(Float.class)) return float.class;
		if(objClass.equals(Character.class)) return char.class;
		if(objClass.equals(Short.class)) return short.class;
		if(objClass.equals(Byte.class)) return byte.class;
		
		return objClass;
	}

	public static int[] getPrimitiveNumberClassIndeces(List<Class<?>> list){
		int[] indeces = new int[0];
		for(int c = 0; c < list.size(); c++){
			Class<?> primClass = list.get(c);
			if(		primClass.equals(Double.class) || primClass.equals(double.class) ||
					primClass.equals(Long.class) || primClass.equals(long.class) ||
					primClass.equals(Integer.class) || primClass.equals(int.class) ||
					primClass.equals(Float.class) || primClass.equals(float.class) ||
					primClass.equals(Short.class) || primClass.equals(short.class) ||
					primClass.equals(Byte.class) || primClass.equals(byte.class)
				) indeces = append(indeces, c);
		}
		return indeces;
	}
	
	public static int getStringParameterIndex(String paramStr){
		if(!isStringParameter(paramStr)) throw new IllegalArgumentException("'" + paramStr + "' does not follow the format: @p[number].");
		return Integer.parseInt(paramStr.substring(2));
	}
	
	public static <T> List<T> insertList(List<T> listInsert, List<T> base, int index){
		List<T> result = new ArrayList<>();
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
	
	public static boolean isBoolean(String bool){
		return bool.equals("true") || bool.equals("false");
	}
	
	public static boolean isConstructor(String constrStr){
		constrStr = constrStr.replace("new ", "");
		constrStr = constrStr.replace(" ", "");
		
		int begParenthIndex = getFirstIndexOf("(", constrStr);
		
		return (begParenthIndex > 0) && allCharsMatch('(', ')', constrStr) && isValidByCharset(constrStr.substring(0,begParenthIndex), PTG.CHARSET_NUMBERS_LETTERS);
	}
	
	public static boolean isDecimal(String number){
		boolean decimal = false;
		for(int c = 0; c < number.length(); c++){
			if(number.charAt(c) == '.'){
				if(decimal) return false;
				decimal = true;
			}
		}
		return isNumber(number);
	}
	
	public static boolean isField(String field){
		String[] splitField = field.split("\\.");
		return splitField.length > 1 && splitField[splitField.length - 1].length() > 0;
	}
	
	public static boolean isInStringArray(String str, String[] array){
		for(int i = 0; i < array.length; i++){
			if(str.equals(array[i])) return true;
		}
		return false;
	}
	
	public static boolean isMethod(String methodStr) {
		
		int periodIndex = getFirstIndexOf(".", methodStr);
		int begParenthIndex = getFirstIndexOf("(", methodStr);
		
		return (periodIndex > 0) && (begParenthIndex > 0) && allCharsMatch('(', ')', methodStr) && isValidByCharset(methodStr.substring(0,periodIndex), PTG.CHARSET_NUMBERS_LETTERS) && isValidByCharset(methodStr.substring(periodIndex + 1, begParenthIndex), PTG.CHARSET_NUMBERS_LETTERS);
	}
	
	public static boolean isNumber(char number){
		for(int i = 0; i < PTG.CHARSET_NUMBERS.length; i++){
			if(PTG.CHARSET_NUMBERS[i] == number){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNumber(String number){
		boolean decimal = false;
		for(int i = 1; i < number.length() - 1; i++){
			char numb = number.charAt(i);
			if((!Util.isNumber(numb)) && (numb != '.' || decimal)){
				return false;
			}else if(number.charAt(i) == '.'){
				decimal = true;
			}
		}
		char posneg = number.charAt(0);
		if(!isNumber(posneg) && ((posneg != '+' && posneg != '-') || number.length() <= 1)) return false;
		
		char numbCast = number.charAt(number.length() - 1);
		if(!isNumber(numbCast) && !Util.isValidByCharset(numbCast, PTG.CHARSET_NUMBER_CASTING_CHARACTERS)) return false;
		return true;
	}
	
	public static boolean isParsableConstructor(String constrStr){
		int colonIndex = getFirstIndexOf(":", constrStr);
		if(colonIndex == -1){
			return isConstructor(constrStr);
		}else if(isNumber(constrStr.substring(0, colonIndex))){
			return isConstructor(constrStr.substring(colonIndex + 1));
		}else{
			return false;
		}
	}
	
	public static boolean isParsableMethod(String methodStr){
		int colonIndex = getFirstIndexOf(":", methodStr);
		int periodIndex = getFirstIndexOf(".",methodStr);
		if(periodIndex == -1) return false;
		if(colonIndex == -1){
			if(isStringParameter(methodStr.substring(0,periodIndex))){
				return isConstructor(methodStr.substring(periodIndex + 1));
			}else{
				return isMethod(methodStr);
			}
		}else if(isNumber(methodStr.substring(0, colonIndex))){
			return isMethod(methodStr.substring(colonIndex + 1));
		}else{
			return false;
		}
	}
	
	public static boolean isString(String str){
		return  (count("\"", str) == 2) && (str.charAt(0) == '\"') && (str.charAt(str.length() - 1) == '\"');
	}
	
	public static boolean isStringParameter(String paramStr){
		return paramStr.length() > 2 && paramStr.substring(0,2).equals("@p") && isNumber(paramStr.substring(2));
	}
	
	public static boolean isStringParameterList(String paramListStr){
		String[] paramList = paramListStr.split(",");
		for(int i = 0; i < paramList.length; i++){
			if(!isStringParameter(paramList[i])) return false;
		}
		return true;
	}
	
	public static boolean isValidByCharset(char character, char[] charset){
		for(int i = 0; i < charset.length; i++){
			if(character == charset[i]) return true;
		}
		return false;
	}
	
	public static boolean isValidByCharset(String str, char[] charset){
		for(int c = 0; c < str.length(); c++){
			if(!isValidByCharset(str.charAt(c), charset)) return false;
		}
		return true;
	}

	public static Object[] listToArray(List<Object> list){
		Object[] objArray = new Object[list.size()];
		for(int i = 0; i < list.size(); i++){
			objArray[i] = list.get(i);
		}
		return objArray;
	}
	
	public static String listToString(List<String> list){
		String result = "";
		for(int i = 0; i < list.size(); i++){
			result+=list.get(i);
		}
		return result;
	}
	
	public static Object parseBoolean(String bool, List<Object> paramValues){
		if(!isBoolean(bool)) throw new IllegalArgumentException(bool + "is not a valid boolean");
		
		if(bool.equals("true")){
			paramValues.add(true);
			return true;
		}else{
			paramValues.add(false);
			return false;
		}
	}
	
	public static Object parseConstructor(String constrStr, List<Object> paramValues, String[] classFileLocations){
		if(!isParsableConstructor(constrStr)) throw new IllegalArgumentException(constrStr + "is not a valid Constructor");

		constrStr = constrStr.replace("new", "").replace(" ", "");
		
		int rightStartParenth = Util.getFirstIndexOf("(", constrStr);
		int leftEndParenth = Util.getLastIndexOf(")", constrStr);
		
		String constructorName = constrStr.substring(0, rightStartParenth);
		
		Class<?> constrClass = getClassByName(constructorName, classFileLocations);
		
		List<Class<?>> paramClasses = new ArrayList<Class<?>>();
		List<Object> paramObjects = new ArrayList<Object>();
		String[] innerArgs = constrStr.substring(rightStartParenth + 1, leftEndParenth).split(",");
		for(int i = 0; i < innerArgs.length; i++){
			int argIndex = getStringParameterIndex(innerArgs[i]);
			paramClasses.add(getPrimitiveClassOf(paramValues.get(argIndex)));
			paramObjects.add(paramValues.get(argIndex));
		}
		
		Constructor<?> constr = Util.getConstructor(constrClass, paramClasses);
		
		Object instancedObject = null;
		try {
			instancedObject = constr.newInstance(listToArray(paramObjects));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
		paramValues.add(instancedObject);
		
		return paramValues.get(paramValues.size() - 1);
	}
	
	public static Object parseField(String fieldStr, List<Object> paramValues, String[] classFileLocations){
		if(!isField(fieldStr)) throw new IllegalArgumentException(fieldStr + "is not a valid field");

		int paramNumb = paramValues.size();
		String[] fields = fieldStr.split("\\.");
		String className = fields[0];
		Class<?> varClass = Util.getClassByName(className, classFileLocations);
		
		for(int f = 1; f < fields.length; f++){
			
			className = fields[f - 1];
			String varName = fields[f];
			
			if(isStringParameter(className)){
				int paramIndex = getStringParameterIndex(className);
				Object paramObj = paramValues.get(paramIndex);
				try {
					paramValues.add(paramObj.getClass().getField(varName).get(paramObj));
					fields[f] = "@p" + paramNumb;
					paramNumb++;
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					boolean enumFound = false;
					Object[] enums = ((Class<?>) paramObj).getEnumConstants();
					for(Object enm:enums){
						if(enm.toString().equals(fields[f])){
							paramValues.add(enm);
							fields[f] = "@p" + paramNumb;
							paramNumb++;
							enumFound = true;
							break;
						}
					}
					
					if(!enumFound){
						e.printStackTrace();
						System.exit(2);
					}
				}
			}else{
				try {
					paramValues.add(varClass.getField(varName).get(varClass));
					fields[f] = "@p" + paramNumb;
					paramNumb++;
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					boolean enumClassFound = false;
					Class<?>[] declClasses = varClass.getDeclaredClasses();
					for(Class<?> declClass:declClasses){
						if(declClass.getSimpleName().equals(fields[f])){
							paramValues.add(declClass);
							fields[f] = "@p" + paramNumb;
							paramNumb++;
							enumClassFound = true;
							break;
						}
					}
					
					if(!enumClassFound){
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
		}
		
		return paramValues.get(paramValues.size() - 1);
	}
	
	public static Object parseIntoObject(String objStr, List<Object> paramValues, String[] classFileLocations){
		
		objStr = objStr.replace("new ", "").replace(" ","");
		
		int paramNumb = 0;
		
		while(true){
			int rightParenthIndex = -1;
			int leftParenthIndex = -1;
			for(int c = 0; c < objStr.length(); c++){
				if(objStr.charAt(c) == '(') rightParenthIndex = c;
				if(objStr.charAt(c) == ')') leftParenthIndex = c;
				
				if(leftParenthIndex > rightParenthIndex && rightParenthIndex > -1){
					String inParenthStr = objStr.substring(rightParenthIndex + 1, leftParenthIndex);
					if(!isStringParameterList(inParenthStr)){
						String[] inParenthArgs = inParenthStr.split(",");
						String finalInsert = "";
						for(int arg = 0; arg < inParenthArgs.length; arg++){
							String currentArg = inParenthArgs[arg];
							
							if(isStringParameter(currentArg)){
								finalInsert += "," + currentArg;
								continue;
							}else if(isBoolean(currentArg)){
								parseBoolean(currentArg, paramValues);
								finalInsert += ",@p" + paramNumb;
								paramNumb++;
								continue;
							}else if(isNumber(currentArg)){
								parseNumber(currentArg, paramValues);
								finalInsert += ",@p" + paramNumb;
								paramNumb++;
								continue;
							}else if(isString(currentArg)){
								parseString(currentArg, paramValues);
								finalInsert += ",@p" + paramNumb;
								paramNumb++;
								continue;
							}else if(isField(currentArg)){
								parseField(currentArg, paramValues, classFileLocations);
								finalInsert += ",@p" + paramNumb;
								paramNumb++;
								continue;
							}
							
							throw new IllegalArgumentException("'" + currentArg + "' is not an accepted argument.");
						}
						
						objStr = objStr.substring(0, rightParenthIndex + 1) + finalInsert.substring(1) + objStr.substring(leftParenthIndex);
						break;
					}else{
						int rightOuterParenthIndex = getLastIndexBefore(rightParenthIndex, "(", objStr);
						int rightOuterCommaIndex = getLastIndexBefore(rightParenthIndex, ",", objStr);
						int rightOuterIndex = rightOuterParenthIndex > rightOuterCommaIndex ? rightOuterParenthIndex : rightOuterCommaIndex;
						String functionStr = objStr.substring(rightOuterIndex + 1, leftParenthIndex + 1);
						
						if(isParsableConstructor(functionStr)){
							parseConstructor(functionStr, paramValues, classFileLocations);
							objStr = objStr.substring(0, rightOuterIndex + 1) + "@p" + paramNumb + objStr.substring(leftParenthIndex + 1);
							paramNumb++;
							break;
						}else if(isParsableMethod(functionStr)){
							parseMethod(functionStr, paramValues, classFileLocations);
							objStr = objStr.substring(0, rightOuterIndex + 1) + "@p" + paramNumb + objStr.substring(leftParenthIndex + 1);
							paramNumb++;
							break;
						}
						
						throw new IllegalArgumentException("'" + functionStr + "' is not an accepted constructor or method.");
					}
				}
			}
			
			if(isStringParameter(objStr)) break;
		}
		
		return paramValues.get(paramValues.size() - 1);
	}
	
	public static Object parseMethod(String methodStr, List<Object> paramValues, String[] classFileLocations){
		if(!isParsableMethod(methodStr)) throw new IllegalArgumentException(methodStr + "is not a valid Method");

		methodStr = methodStr.replace(" ", "");
		
		int rightStartParenth = Util.getFirstIndexOf("(", methodStr);
		int leftEndParenth = Util.getLastIndexOf(")", methodStr);
		int rightStartPeriod = Util.getFirstIndexOf(".", methodStr);
		
		String methodObject = methodStr.substring(0, rightStartPeriod);
		boolean invokeOnObject = isStringParameter(methodObject);
		String methodName = methodStr.substring(rightStartPeriod + 1, rightStartParenth);
		
		Class<?> methodClass = invokeOnObject ? paramValues.get(getStringParameterIndex(methodObject)).getClass() : getClassByName(methodObject, classFileLocations);
		
		List<Class<?>> paramClasses = new ArrayList<Class<?>>();
		List<Object> paramObjects = new ArrayList<Object>();
		String[] innerArgs = methodStr.substring(rightStartParenth + 1, leftEndParenth).split(",");
		for(int i = 0; i < innerArgs.length; i++){
			int argIndex = getStringParameterIndex(innerArgs[i]);
			paramClasses.add(getPrimitiveClassOf(paramValues.get(argIndex)));
			paramObjects.add(paramValues.get(argIndex));
		}
		
		Method method = Util.getMethod(methodClass, methodName, paramClasses);
		
		Object methodResult = null;
		try {
			methodResult = invokeOnObject ? method.invoke(paramValues.get(getStringParameterIndex(methodObject)), listToArray(paramObjects)) : method.invoke(methodClass, listToArray(paramObjects));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		paramValues.add(methodResult);
		
		return paramValues.get(paramValues.size() - 1);
	}
	
	public static Object parseNumber(String numb, List<Object> paramValues){
		if(!isNumber(numb)) throw new IllegalArgumentException(numb + "is not a valid number");
		
		switch(numb.charAt(numb.length() - 1)){
		case 'f':case 'F':
			paramValues.add(Float.parseFloat(numb.substring(0, numb.length() - 1)));
			break;
		case 'l':case 'L':
			paramValues.add(Long.parseLong(numb.substring(0, numb.length() - 1).split("\\.")[0]));
			break;
		case 'i':case 'I':
			paramValues.add(Integer.parseInt(numb.substring(0, numb.length() - 1).split("\\.")[0]));
			break;
		case 'd':case 'D':
			paramValues.add(Double.parseDouble(numb.substring(0, numb.length() - 1)));
			break;
		case 's':case 'S':
			paramValues.add(Short.parseShort(numb.substring(0, numb.length() - 1).split("\\.")[0]));
			break;
		case 'b':case 'B':
			paramValues.add(Byte.parseByte(numb.substring(0, numb.length() - 1).split("\\.")[0]));
			break;
		default:
			if(isDecimal(numb)){
				paramValues.add(Double.parseDouble(numb));
				break;
			}else{
				paramValues.add(Long.parseLong(numb));
				break;
			}
		}
		
		return paramValues.get(paramValues.size() - 1);
	}
	
	public static Object parseString(String str, List<Object> paramValues){
		if(!isString(str)) throw new IllegalArgumentException("'" + str + "' is not a valid String");
		String result = new String(str.substring(1, str.length() - 1));
		paramValues.add(result);
		return result;
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
	
	public static String removeFirst(String strPart, String str){
		int index = getFirstIndexOf(strPart, str);
		return index == -1 ? str : str.substring(0,index) + str.substring(index + strPart.length());
	}
	
	public static String removeLast(String strPart, String str){
		int index = getLastIndexOf(strPart, str);
		return index == -1 ? str : str.substring(0,index) + str.substring(index + strPart.length());
	}

	public static List<?> shrinkList(List<?> list, int min, int max){
		int size = list.size();
		for(int i = 0; i < size-max-1; i++){
			list.remove(max+1);
		}
		for(int i = 0; i < min; i++){
			list.remove(0);
		}
		return list;
	}
	
	public static String[] splitBy(String str, String... parts){
		String[] result = new String[0];
		int startIndex = 0;
		
		for(int c = 0; c < str.length(); c++){
			for(int i = 0; i < parts.length; i++){
				if(c + parts[i].length() >= str.length()) break;
					
				if(str.charAt(c) == parts[i].charAt(0)){
					boolean fullString = true;
					for(int j = 1; j < parts[i].length(); j++){
						if(str.charAt(c + j) != parts[i].charAt(j)) fullString = false;
					}
					if(fullString){
						String appendPart = str.substring(startIndex, c);
						
						result = Util.append(result, appendPart);
						c += parts[i].length();
						startIndex = c;
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	public static int[] subtract(int[] array1, int[] array2){
		int greaterLength = array1.length >= array2.length ? array1.length : array2.length;
		int lesserLength = array1.length <= array2.length ? array1.length : array2.length;
		int[] result = new int[greaterLength];
		
		for(int i = 0; i < lesserLength; i++){
			result[i] = array2[i] - array1[i];
		}
		
		for(int i = lesserLength; i < array1.length; i++){
			result[i] = array1[i];
		}
		for(int i = lesserLength; i < array2.length; i++){
			result[i] = array2[i];
		}
		
		return result;
	}
}