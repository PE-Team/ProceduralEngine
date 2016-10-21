package ptg.util.converters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ptg.engine.main.PTG;

public class StringToOperationConverter {

	/**
	 * By default, all characters are interpreted as literals except in the below cases
	 *    			@p[number] = refers to the parameter given at the index position of number
	 *            
	 *             <[letters]> = refers to an escape sequence or number
	 * 		 			  <pi> = the number for pi (Math.PI)
	 *
	 * <[letters]>(parameters) = refers to an escape sequence, number, or custom operation (not +,-,*,/)
	 * 			<cos>(degrees) = the cosine of the parameter in degrees
	 *  	  
	 */
	
	private static List<Object> parameters = new ArrayList<Object>();
	private static String operationString;
	private static String outputString;
	private static boolean continuePrompting = true;
	private static Scanner scanner;
	
	public static void main(String... args){
		scanner = new Scanner(System.in);
		promptOperationString();
		promptParameters();
		scanner.close();
		outputString = operateOnString();
		System.out.println(outputString + " -Final output");
	}
	
	//{<pi>,1+@p0,{@p1*@p2,49/50}}
	//param0 = 4, param1 = 7, param2 = 31
	//{3.141592653589793,1+4,{7*31,49/50}}
	//{3.14..., 5.0,{217.0,0.98}}
	
	//(1+2)+3
	//6
	public static String operateOnString(){
		List<String> split = new ArrayList<String>();
		split.add("");
		int pointer = 0;
		
		// Split the operationString by special characters to make the next step easier
		for(int i = 0; i < operationString.length(); i++){
			switch(operationString.charAt(i)){
				case'<':case'@':
					if(split.get(pointer) != ""){
						pointer++;
						split.add("");
					}
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					break;
				case'>':
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					if(split.get(pointer) != ""){
						pointer++;
						split.add("");
					};
					break;
				case'+':case'-':case'*':case'/':case'^':case'%':case'(':case')':
					if(split.get(pointer) != ""){
						pointer++;
						split.add("");
					}
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					pointer++;
					split.add("");
					break;
				case'0':case'1':case'2':case'3':case'4':case'5':case '6':case'7':case'8':case'9':case'.':
					if((i>0 && (isNumber(operationString.charAt(i-1)) || operationString.charAt(i-1) == '.')) || 
					   (i>1 && operationString.charAt(i-1) == 'p' && operationString.charAt(i-2) == '@')){
						split.set(pointer, split.get(pointer) + operationString.charAt(i));
					}else{
						if(split.get(pointer) != ""){
							pointer++;
							split.add("");
						}
						split.set(pointer, split.get(pointer) + operationString.charAt(i));
					}
					break;
				default:
					if(i>0 && isNumber(operationString.charAt(i-1))){
						pointer++;
						split.add("");
					}
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					break;
			}
		}
		
		//System.out.println(listToString(split) + " -After inserting all the parameters");
		
		//Remove the last part of the list if it doesn't include anything
		if(split.get(split.size()-1) == ""){
			split.remove(split.size()-1);
		}
		
		// Replace all variables with their numerical values
		for(int i = 0; i < split.size(); i++){
			
			// Replace all @p[number] with its respective parameter
			if(split.get(i).length() > 2 && split.get(i).charAt(0) == '@' && split.get(i).charAt(1) == 'p' && isNumber(split.get(i).charAt(2))){
				String integer = "";
				int paramIndex = 0;
				for(int j = 2; j < split.get(i).length(); j++){
					integer+=split.get(i).charAt(j);
				}
				if(integer != ""){
					paramIndex = Integer.parseInt(integer);
				}else{
					System.err.println("PARAMETER INDEX INCOMPLETE");
					System.exit(2);
				}
				
				split.set(i, (String) parameters.get(paramIndex));
			}
			
			// Replace all <[letters]> with its respective value
			if(split.get(i).charAt(0) == '<' && split.get(i).charAt(split.get(i).length() - 1) == '>'){
				if(split.get(i).equals("<pi>")) split.set(i, Double.toString(Math.PI));
			}
		}
		
		//System.out.println(listToString(split) + " -After inserting all variables");
		
		// Continue to do operations until they are all finished
		boolean operationFound = true;
		List<String> currentOperation = new ArrayList<String>();
		int operationOffset = 0;
		int leftOffset = -1;
		int rightOffset = -1;
		int chars = 0;
		do{
			operationOffset = 0;
			//Remove unwanted characters
			leftOffset = -1;
			rightOffset = -1;
			for(int i = 0; i < split.size(); i++){
				// Parentheses
				if(existsChar(split.get(i),'(')){
					leftOffset = i;
				}
				if(existsChar(split.get(i),')') && leftOffset >=0){
					rightOffset = i;
				}
				if(rightOffset - leftOffset <= 2 && leftOffset >= 0 && rightOffset >= leftOffset){
					split.remove(leftOffset);
					split.remove(rightOffset-1);
					//System.out.println(listToString(split) + " -After cutting down parentheses.");
					break;
				}
			}
			
			cloneList(currentOperation, split);
			
			// Get a list which has exactly one operation in it but also is of the hightest priority
		
			//PEMDAS
			//Parentheses, Exponents, Multiplication, Division, Addition, Subtraction
			//0			 , 1		, 2				, 3		  , 4		, 5
			
			// Parentheses
			leftOffset = -1;
			rightOffset = -1;
			for(int j = 0; j < currentOperation.size(); j++){
				if(existsChar(currentOperation.get(j),'(')){
					leftOffset = j;
				}
				if(existsChar(currentOperation.get(j),')') && leftOffset >=0){
					rightOffset = j;
				}
				if(rightOffset - leftOffset >= 3 && leftOffset >= 0 && rightOffset >= leftOffset){
					pointer = 0;
					int length = currentOperation.size();
					operationOffset += leftOffset+1;
					shrinkList(currentOperation, operationOffset, rightOffset-1);
					//System.out.println(listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Exponentials
			leftOffset = -1;
			rightOffset = -1;
			chars = currentOperation.size();
			for(int j = 0; j < chars; j++){
				if(existsChar(currentOperation.get(j),'^') && j>0){
					leftOffset = j-1;
					rightOffset = j+1;
					operationOffset += leftOffset;
					shrinkList(currentOperation, leftOffset, rightOffset);
					//System.out.println(listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Multiplication
			leftOffset = -1;
			rightOffset = -1;
			chars = currentOperation.size();
			for(int j = 0; j < chars; j++){
				if(existsChar(currentOperation.get(j),'*') && j>0){
					leftOffset = j-1;
					rightOffset = j+1;
					operationOffset += leftOffset;
					shrinkList(currentOperation, leftOffset, rightOffset);
					//System.out.println(listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Division
			leftOffset = -1;
			rightOffset = -1;
			chars = currentOperation.size();
			for(int j = 0; j < chars; j++){
				if(existsChar(currentOperation.get(j),'/') && j>0){
					leftOffset = j-1;
					rightOffset = j+1;
					operationOffset += leftOffset;
					shrinkList(currentOperation, leftOffset, rightOffset);
					//System.out.println(listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Addition
			leftOffset = -1;
			rightOffset = -1;
			chars = currentOperation.size();
			for(int j = 0; j < chars; j++){
				if(existsChar(currentOperation.get(j),'+') && j>0){
					leftOffset = j-1;
					rightOffset = j+1;
					operationOffset += leftOffset;
					shrinkList(currentOperation, leftOffset, rightOffset);
					//System.out.println(listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Subtraction
			leftOffset = -1;
			rightOffset = -1;
			chars = currentOperation.size();
			for(int j = 0; j < chars; j++){
				if(existsChar(currentOperation.get(j),'-') && j>0){
					leftOffset = j-1;
					rightOffset = j+1;
					operationOffset += leftOffset;
					shrinkList(currentOperation, leftOffset, rightOffset);
					//System.out.println(listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			//System.out.println(listToString(currentOperation) + " -After cutting down to one operation.");
			
			// Do the operation needed
			Class<?> operationClass = null;
			String classMethod = null;
			try{
				//Exponential
				if(doOperation(currentOperation, "^", operationOffset, split, operationClass, classMethod)) continue;
				
				//Multiplication
				if(doOperation(currentOperation, "*", operationOffset, split, operationClass, classMethod)) continue;
				
				//Division
				if(doOperation(currentOperation, "/", operationOffset, split, operationClass, classMethod)) continue;
				
				//Addition
				if(doOperation(currentOperation, "+", operationOffset, split, operationClass, classMethod)) continue;
				
				//Subtraction
				if(doOperation(currentOperation, "-", operationOffset, split, operationClass, classMethod)) continue;
				
				operationFound = false;
				//System.out.println("No operations done");
			}catch(Exception e){operationFound = false;}
			
		}while(operationFound);
		
		return listToString(split);
	}
	
	private static boolean doOperation(List<String> operationString, String operation, int listOffset, List<String> operationList, Class<?> operationClass, String classMethod){
		if((!operationString.get(1).equals(operation) && operationString.size() == 3) || 
		   (!operationString.get(0).equals(operation) && operationString.size() == 4) ||
		   (operationString.size() != 4 && operationString.size() != 3)) return false;
		
		double total = 0;
		double var1 = 0;
		double var2 = 0;
		
		try{
			var1 = Double.parseDouble(operationString.get(0));
		}catch(Exception e){}
		var2 = Double.parseDouble(operationString.get(2));
		
		
		switch(operationString.get(1)){
		case"^":
			total = Math.pow(var1, var2);
			break;
		case"*":
			total = var1*var2;
			break;
		case"/":
			total = var1/var2;
			break;
		case"+":
			total = var1+var2;
			break;
		case"-":
			total = var1-var2;
			break;
		default:
			try{
				Method operationMethod = operationClass.getMethod(classMethod, Double.class);
				Object o = new Object();
				total = (double) operationMethod.invoke(o, var1, var2);
			}catch(Exception e1){
				System.out.println("No such operation: " + operationClass.getSimpleName() + "." + classMethod + "(double var)");
			}
			break;
		}
		operationList.set(listOffset, Double.toString(total));
		operationList.remove(listOffset+1);
		operationList.remove(listOffset+1);
		return true;
	}
	
	private static String listToString(List<String> list){
		String result = "";
		for(int i = 0; i < list.size(); i++){
			result+=list.get(i);
		}
		return result;
	}
	
	private static List shrinkList(List list, int min, int max){
		int size = list.size();
		for(int i = 0; i < size-max-1; i++){
			list.remove(max+1);
		}
		for(int i = 0; i < min; i++){
			list.remove(0);
		}
		return list;
	}
	
	private static List cloneList(List list1, List list2){
		list1.clear();
		for(int i = 0; i < list2.size(); i++){
			list1.add(list2.get(i));
		}
		return list1;
	}
	
	private static boolean existsChar(String string, char character){
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == character) return true;
		}
		return false;
	}
	
	private static boolean isNumber(String string){
		boolean decimal = false;
		for(int i = 0; i < string.length(); i++){
			if(!isNumber(string.charAt(i)) && (string.charAt(i) != '.' || decimal)){
				return false;
			}else if(string.charAt(i) == '.'){
				decimal = true;
			}
		}
		return true;
	}
	
	private static boolean isNumber(char character){
		for(int i = 0; i < PTG.CHARSET_NUMBERS.length; i++){
			if(PTG.CHARSET_NUMBERS[i] == character){
				return true;
			}
		}
		return false;
	}
	
	public static void setOperationString(String string){
		operationString = string;
	}
	
	public static void setParameters(Object... params){
		for(int i = 0; i < params.length; i++){
			parameters.add(params[i]);
		}
	}
	
	private static void promptOperationString(){
		System.out.println("Operation: ");
		operationString = scanner.next();
	}
	
	private static void promptParameters(){
		while(continuePrompting){
			promptParameter();
		}
	}
	
	private static void promptParameter(){
		System.out.println("Is there another parameter (y/n): ");
		String answer = scanner.next();
		if(answer.equals("y")){
			System.out.println("Enter next parameter: ");
			parameters.add(scanner.next());
		}else if(answer.equals("n")){
			continuePrompting = false;
		}else{
			System.out.println("Please enter y or n");
			promptParameter();
		}
	}
	
}
