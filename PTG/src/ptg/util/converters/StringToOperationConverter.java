package ptg.util.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	public static final String[] STRINGSET_MATH_CONSTANTS = {
			"<pi>"
	};
	
	public static final String[] STRINGSET_MATH_OPERATIONS = {
			"+","-","*","/",
			"<cos>"
	};
	
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
		System.out.println(outputString);
	}
	
	private static String operateOnString(){
		String result = "";
		
		
		
		return result;
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
