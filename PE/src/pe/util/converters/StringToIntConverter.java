package pe.util.converters;

import java.util.Scanner;

import pe.engine.main.PE;

public class StringToIntConverter {
	private static char[] charset;

	public static void main(String... args) {
		if (args.length == 0) {
			args = new String[1];
			args[0] = promptInputString();
		}
		setCharset();
		int numb = stringToInt(args[0]);
		System.out.println(numb);
	}

	private static String promptInputString() {
		System.out.println("Enter a String");
		Scanner scanner = new Scanner(System.in);
		String str = scanner.next().toLowerCase();
		scanner.close();
		return str;
	}

	private static void setCharset() {
		charset = PE.CHARSET_LETTERS_LOWERCASE;
	}

	public static void setCharset(char[] chars) {
		charset = chars;
	}
	
	public static String cutString(String string, int maxLength){
		return string.length() <= maxLength ? string : string.substring(0,maxLength);
	}

	public static int stringToInt(String string) {
		int numb = -1;
		for (int i = 0; i < string.length(); i++) {
			char charAt = string.charAt(i);
			for (int j = 0; j < charset.length; j++) {
				if (charAt == charset[j]) {
					numb += Math.pow(charset.length, i) + j + 1;
				}
			}
		}
		return numb;
	}
}
