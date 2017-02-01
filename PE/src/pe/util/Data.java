package pe.util;

import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private List<Byte> byteArray = null;
	private List<Short> shortArray = null;
	private List<Integer> intArray = null;
	private List<Long> longArray = null;
	private List<Float> floatArray = null;
	private List<Double> doubleArray = null;
	private List<Boolean> booleanArray = null;
	private List<Character> charArray = null;
	private List<Object> objArray = null;
	
	public Data(){};
	
	public Data store(byte b){
		if(byteArray == null) byteArray = new ArrayList<Byte>();
		byteArray.add(b);
		return this;
	}
	
	public Data store(short s){
		if(shortArray == null) shortArray = new ArrayList<Short>();
		shortArray.add(s);
		return this;
	}
	
	public Data store(int i){
		if(intArray == null) intArray = new ArrayList<Integer>();
		intArray.add(i);
		return this;
	}
	
	public Data store(long l){
		if(longArray == null) longArray = new ArrayList<Long>();
		longArray.add(l);
		return this;
	}
	
	public Data store(float f){
		if(floatArray == null) floatArray = new ArrayList<Float>();
		floatArray.add(f);
		return this;
	}
	
	public Data store(double d){
		if(doubleArray == null) doubleArray = new ArrayList<Double>();
		doubleArray.add(d);
		return this;
	}
	
	public Data store(boolean b){
		if(booleanArray == null) booleanArray = new ArrayList<Boolean>();
		booleanArray.add(b);
		return this;
	}
	
	public Data store(char b){
		if(charArray == null) charArray = new ArrayList<Character>();
		charArray.add(b);
		return this;
	}
	
	public Data store(Object o){
		if(objArray == null) objArray = new ArrayList<Object>();
		objArray.add(o);
		return this;
	}
}
