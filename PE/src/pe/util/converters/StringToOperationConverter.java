package pe.util.converters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pe.util.Util;

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
	
	private List<String> parameters = new ArrayList<String>();
	private String operationString;
	private static String outputString;
	private boolean continuePrompting = true;
	private static Scanner scanner;
	
	public static void main(String... args){
		scanner = new Scanner(System.in);
		StringToOperationConverter operation = new StringToOperationConverter();
		operation.promptOperationString();
		operation.promptParameters();
		scanner.close();
		outputString = operation.operateOnString();
		System.out.println(outputString + " -Final output");
	}
	
	public StringToOperationConverter(){}
	
	//{<pi>,1+@p0,{@p1*@p2,49/50}}
	//param0 = 4, param1 = 7, param2 = 31
	//{3.141592653589793,1+4,{7*31,49/50}}
	//{3.14..., 5.0,{217.0,0.98}}
	
	//(1+2)+3
	//6
	public String operateOnString(){
		List<String> split = new ArrayList<String>();
		split.add("");
		int pointer = 0;
		
		// Split the operationString by special characters to make the next step easier
		for(int i = 0; i < operationString.length(); i++){
			switch(operationString.charAt(i)){
				case'@':
					if(split.get(pointer) != ""){
						pointer++;
						split.add("");
					}
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					break;
				case'+':case'-':case'*':case'/':case'^':case'%':case'(':case')':case'<':case'>':
					if(split.get(pointer) != ""){
						pointer++;
						split.add("");
					}
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					pointer++;
					split.add("");
					break;
				case'0':case'1':case'2':case'3':case'4':case'5':case '6':case'7':case'8':case'9':case'.':
					if((i>0 && (Util.isNumber(operationString.charAt(i-1)) || operationString.charAt(i-1) == '.')) || 
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
					if(i>0 && Util.isNumber(operationString.charAt(i-1))){
						pointer++;
						split.add("");
					}
					split.set(pointer, split.get(pointer) + operationString.charAt(i));
					break;
			}
		}
		
		//System.out.println(Util.listToString(split) + " -After inserting all the parameters");
		
		//Remove the last part of the list if it doesn't include anything
		if(split.get(split.size()-1) == ""){
			split.remove(split.size()-1);
		}
		
		// Replace all variables with their numerical values
		for(int i = 0; i < split.size(); i++){
			
			// Replace all @p[number] with its respective parameter
			if(split.get(i).length() > 2 && split.get(i).charAt(0) == '@' && split.get(i).charAt(1) == 'p' && Util.isNumber(split.get(i).charAt(2))){
				String integer = "";
				int paramIndex = 0;
				for(int j = 2; j < split.get(i).length(); j++){
					integer+=split.get(i).charAt(j);
				}
				if(integer != ""){
					paramIndex = Integer.parseInt(integer);
				}else{
					//System.err.println("PARAMETER INDEX INCOMPLETE");
					System.exit(2);
				}
				
				split.set(i, parameters.get(paramIndex));
			}
			
			// Replace all <[letters]> with its respective value
			if(i+2 < split.size() && split.get(i).equals("<") && split.get(i+2).equals(">")){
				if(split.get(i+1).equals("pi")) split.set(i, Double.toString(Math.PI));
				split.remove(i+1);
				split.remove(i+1);
			}
		}
		
		// Combine negatives with their respective numbers
		for(int i = 0; i < split.size(); i++){
			if(i + 1 >= split.size()) continue;
			if(Util.existsChar(split.get(i), '-') && Util.isNumber(split.get(i+1)) && (i == 0 || !Util.isNumber(split.get(i-1)))){
				String newString = split.get(i) + split.get(i+1);
				split.set(i, newString);
				split.remove(i+1);
			}
		}
		
		//System.out.println(split.toString() + " -After inserting all variables");
		
		// Continue to do operations until they are all finished
		boolean operationFound = true;
		List<String> currentOperation = new ArrayList<String>();
		int operationOffset = 0;
		int leftOffset = -1;
		int rightOffset = -1;
		do{
			//System.out.println(split.toString());
			
			operationOffset = 0;
			//Remove unwanted characters
			leftOffset = -1;
			rightOffset = -1;
			for(int i = 0; i < split.size(); i++){
				// Parentheses
				if(Util.existsChar(split.get(i),'(')){
					leftOffset = i;
				}
				if(Util.existsChar(split.get(i),')') && leftOffset >=0){
					rightOffset = i;
				}
				if(rightOffset - leftOffset <= 2 && leftOffset >= 0 && rightOffset >= leftOffset){
					split.remove(leftOffset);
					split.remove(rightOffset-1);
					leftOffset = -1;
					rightOffset = -1;
					//System.out.println(Util.listToString(split) + " -After cutting down parentheses.");
				}
			}
			
			Util.cloneList(currentOperation, split);
			
			// Get a list which has exactly one operation in it but also is of the highest priority
		
			//PEMDAS
			//Parentheses, Exponents, Multiplication, Division, Addition, Subtraction
			//0			 , 1		, 2				, 3		  , 4		, 5
			
			// Parentheses
			leftOffset = -1;
			rightOffset = -1;
			for(int j = 0; j < currentOperation.size(); j++){
				if(Util.existsChar(currentOperation.get(j),'(')){
					leftOffset = j;
				}
				if(Util.existsChar(currentOperation.get(j),')') && leftOffset >=0){
					rightOffset = j;
				}
				if(rightOffset - leftOffset >= 3 && leftOffset >= 0 && rightOffset >= leftOffset){
					pointer = 0;
					operationOffset += leftOffset+1;
					Util.shrinkList(currentOperation, operationOffset, rightOffset-1);
					//System.out.println(Util.listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Custom Functions
			leftOffset = -1;
			rightOffset = -1;
			for(int j = 0; j < currentOperation.size(); j++){
				if(Util.existsChar(currentOperation.get(j),'<')){
					leftOffset = j;
				}
				if(Util.existsChar(currentOperation.get(j),'>') && leftOffset >=0){
					rightOffset = j;
				}
				if(rightOffset - leftOffset >= 2 && leftOffset >= 0 && rightOffset >= leftOffset && Util.isNumber(currentOperation.get(j+1))){
					pointer = 0;
					operationOffset += leftOffset;
					Util.shrinkList(currentOperation, operationOffset, rightOffset+1);
					//System.out.println(Util.listToString(currentOperation) + " -After cutting down outside parts.");
					break;
				}
			}
			
			// Exponentials
			operationOffset += reduceOperations(currentOperation, '^');
			
			// Multiplication
			operationOffset += reduceOperations(currentOperation, '*');
			
			// Division
			operationOffset += reduceOperations(currentOperation, '/');
			
			// Addition
			operationOffset += reduceOperations(currentOperation, '+');
			
			// Subtraction
			operationOffset += reduceOperations(currentOperation, '-');
			
			//System.out.println(Util.listToString(currentOperation) + " -After cutting down to one operation.");
			
			// Do the operation needed
			try{
				//Exponential
				if(doOperation(currentOperation, "^", operationOffset, split, null, null)) continue;
				
				//Multiplication
				if(doOperation(currentOperation, "*", operationOffset, split, null, null)) continue;
				
				//Division
				if(doOperation(currentOperation, "/", operationOffset, split, null, null)) continue;
				
				//Addition
				if(doOperation(currentOperation, "+", operationOffset, split, null, null)) continue;
				
				//Subtraction
				if(doOperation(currentOperation, "-", operationOffset, split, null, null)) continue;
				
				//Cosine
				if(doOperation(currentOperation, "cos", operationOffset, split, Math.class, "cos")) continue;

				//Sine
				if(doOperation(currentOperation, "sin", operationOffset, split, Math.class, "sin")) continue;
				
				//Tangent
				if(doOperation(currentOperation, "tan", operationOffset, split, Math.class, "tan")) continue;
				
				//Inverse Cosine / ArcCosine
				if(doOperation(currentOperation, "arccos", operationOffset, split, Math.class, "acos")) continue;
				
				//Inverse Sine / ArcSine
				if(doOperation(currentOperation, "arcsin", operationOffset, split, Math.class, "asin")) continue;
				
				//Inverse Tangent / ArcTangent
				if(doOperation(currentOperation, "arctan", operationOffset, split, Math.class, "atan")) continue;
				
				operationFound = false;
				//System.out.println("No operations done");
			}catch(Exception e){operationFound = false;}
			
		}while(operationFound);
		
		return Util.listToString(split);
	}

	private static int reduceOperations(List<String> currentOperation, char operation) {
		int leftOffset = -1;
		int rightOffset = -1;
		int operationOffset = 0;
		int chars = currentOperation.size();
		for(int j = 0; j < chars; j++){
			if(Util.existsChar(currentOperation.get(j),operation) && j>0 && !Util.isNumber(currentOperation.get(j))){
				leftOffset = j-1;
				rightOffset = j+1;
				operationOffset += leftOffset;
				Util.shrinkList(currentOperation, leftOffset, rightOffset);
				//System.out.println(Util.listToString(currentOperation) + " -After cutting down outside parts.");
				break;
			}
		}
		return operationOffset;
	}
	
	private static boolean doOperation(List<String> operationString, String operation, int listOffset, List<String> operationList, Class<?> operationClass, String classMethod){
		int length = operationString.size();
		if((!operationString.get(1).equals(operation)) ||
		   (length != 4 && length != 3)) return false;
		
		double total = 0;
		double var1 = 0;
		double var2 = 0;
		
		try{
			var1 = Double.parseDouble(operationString.get(0));
			var2 = Double.parseDouble(operationString.get(2));
		}catch(Exception e){
			var1 = Double.parseDouble(operationString.get(3));
		}
		
		
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
				Method operationMethod = operationClass.getMethod(classMethod, double.class);
				Object o = new Object();
				total = (double) operationMethod.invoke(o, var1);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("No such operation: " + operationClass.getName() + "." + classMethod + "(double var)");
			}
			break;
		}
		operationList.set(listOffset, Double.toString(total));
		operationList.remove(listOffset+1);
		operationList.remove(listOffset+1);
		if(length == 4) operationList.remove(listOffset+1);
		return true;
	}
	
	public void setOperationString(String string){
		this.operationString = string;
	}
	
	public void setParameters(String... params){
		for(int i = 0; i < params.length; i++){
			this.parameters.add(params[i]);
		}
	}
	
	private void promptOperationString(){
		System.out.println("Operation: ");
		operationString = scanner.next();
	}
	
	private void promptParameters(){
		while(continuePrompting){
			promptParameter();
		}
	}
	
	private void promptParameter(){
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
