package pe.util;

import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private List<Byte> byteList = null;
	private List<Short> shortList = null;
	private List<Integer> intList = null;
	private List<Long> longList = null;
	private List<Float> floatList = null;
	private List<Double> doubleList = null;
	private List<Boolean> booleanList = null;
	private List<Character> charList = null;
	private List<Object> objList = null;
	
	public Data(){};
	
	public Data store(byte b){
		if(byteList == null) byteList = new ArrayList<Byte>();
		byteList.add(b);
		return this;
	}
	
	public Data store(short s){
		if(shortList == null) shortList = new ArrayList<Short>();
		shortList.add(s);
		return this;
	}
	
	public Data store(int i){
		if(intList == null) intList = new ArrayList<Integer>();
		intList.add(i);
		return this;
	}
	
	public Data store(long l){
		if(longList == null) longList = new ArrayList<Long>();
		longList.add(l);
		return this;
	}
	
	public Data store(float f){
		if(floatList == null) floatList = new ArrayList<Float>();
		floatList.add(f);
		return this;
	}
	
	public Data store(double d){
		if(doubleList == null) doubleList = new ArrayList<Double>();
		doubleList.add(d);
		return this;
	}
	
	public Data store(boolean b){
		if(booleanList == null) booleanList = new ArrayList<Boolean>();
		booleanList.add(b);
		return this;
	}
	
	public Data store(char b){
		if(charList == null) charList = new ArrayList<Character>();
		charList.add(b);
		return this;
	}
	
	public Data store(Object o){
		if(objList == null) objList = new ArrayList<Object>();
		objList.add(o);
		return this;
	}
	
	public byte getByte(int index){
		return byteList.get(index);
	}
	
	public short getShort(int index){
		return shortList.get(index);
	}
	
	public int getInt(int index){
		return intList.get(index);
	}
	
	public long getLong(int index){
		return longList.get(index);
	}
	
	public float getFloat(int index){
		return floatList.get(index);
	}
	
	public double getDouble(int index){
		return doubleList.get(index);
	}
	
	public boolean getBoolean(int index){
		return booleanList.get(index);
	}
	
	public char getChar(int index){
		return charList.get(index);
	}
	
	public Object getObject(int index){
		return objList.get(index);
	}
	
	public List<Byte> getByteList(){
		return byteList;
	}
	
	public List<Short> getShortList(){
		return shortList;
	}
	
	public List<Integer> getIntList(){
		return intList;
	}
	
	public List<Long> getLongList(){
		return longList;
	}
	
	public List<Float> getFloatList(){
		return floatList;
	}
	
	public List<Double> getDoubleList(){
		return doubleList;
	}
	
	public List<Boolean> getBooleanList(){
		return booleanList;
	}
	
	public List<Character> getCharList(){
		return charList;
	}
	
	public List<Object> getObjectList(){
		return objList;
	}
}
