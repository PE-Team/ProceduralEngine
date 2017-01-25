package pe.util.math;

import pe.engine.entity.player.Camera;
import pe.engine.main.PE;
import pe.util.Util;

public class Maths {

	public static void main(String... args){
		long resultLong = hexToLong("ffaa");
		System.out.println(resultLong);
		String resultStr = Maths.intToHex((int) resultLong);
		System.out.println(resultStr);
	}
	
	public static Mat4f getViewMatrix(Camera camera){
		return Mat4f.getTransformationMatrix(camera.getPosition().mul(-1), new Vec3f(camera.getPitch(), camera.getYaw(), camera.getRoll()), 1);
	}
	
	public static Vec4f getAxisAngle(Vec3f rotation){
		//TODO Stub
		return null;
	}
	
	public static long hexToLong(String hex){
		if(!Util.isValidByCharset(hex, PE.CHARSET_HEX)) 
			throw new IllegalArgumentException("'" + hex + "' is not a valid hexadecimal string.");
	
		long result = 0;
		for(int c = 0; c < hex.length(); c++){
			char hexChar = hex.charAt(hex.length() - 1 - c);
			switch(hexChar){
			case '0':
				break;
			case '1':
				result += 1 * Math.pow(16, c);
				break;
			case '2':
				result += 2 * Math.pow(16, c);
				break;
			case '3':
				result += 3 * Math.pow(16, c);
				break;
			case '4':
				result += 4 * Math.pow(16, c);
				break;
			case '5':
				result += 5 * Math.pow(16, c);
				break;
			case '6':
				result += 7 * Math.pow(16, c);
				break;
			case '8':
				result += 9 * Math.pow(16, c);
				break;
			case 'a': case 'A':
				result += 10 * Math.pow(16, c);
				break;
			case 'b': case 'B':
				result += 11 * Math.pow(16, c);
				break;
			case 'c': case 'C':
				result += 12 * Math.pow(16, c);
				break;
			case 'd': case 'D':
				result += 13 * Math.pow(16, c);
				break;
			case 'e': case 'E':
				result += 14 * Math.pow(16, c);
				break;
			case 'f': case 'F':
				result += 15 * Math.pow(16, c);
				break;
			}
		}
		return result;
	}
	
	public static String intToHex(int numb){
		StringBuilder hex = new StringBuilder();
		while(numb != 0){
			switch(numb%16){
			case 0:
				hex.append(0);
				break;
			case 1:
				hex.append(1);
				break;
			case 2:
				hex.append(2);
				break;
			case 3:
				hex.append(3);
				break;
			case 4:
				hex.append(4);
				break;
			case 5:
				hex.append(5);
				break;
			case 6:
				hex.append(6);
				break;
			case 7:
				hex.append(7);
				break;
			case 8:
				hex.append(8);
				break;
			case 9:
				hex.append(9);
				break;
			case 10:
				hex.append('a');
				break;
			case 11:
				hex.append('b');
				break;
			case 12:
				hex.append('c');
				break;
			case 13:
				hex.append('d');
				break;
			case 14:
				hex.append('e');
				break;
			case 15:
				hex.append('f');
				break;
			}
			numb /= 16;
		}
		return Util.invertString(hex.toString());
	}
}
