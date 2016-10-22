package ptg.util;

import java.util.List;

import ptg.engine.main.PTG;

public class Util {

	public static boolean isNumber(String string){
		boolean decimal = false;
		for(int i = 0; i < string.length(); i++){
			if(!Util.isNumber(string.charAt(i)) && (string.charAt(i) != '.' || decimal)){
				return false;
			}else if(string.charAt(i) == '.'){
				decimal = true;
			}
		}
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

}
