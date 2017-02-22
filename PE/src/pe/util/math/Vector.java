package pe.util.math;

public class Vector {
	
	private Fraction[] vector;
	private int length;
	
	public Vector(Fraction... values) {
		vector = values;
		length = values.length;
	}
	
	public Vector add(Vector addedVector){
		if(length() != addedVector.length())
			throw new IllegalArgumentException("Both vectors must be of the same length. ");
		
		for(int i = 0; i < vector.length; i++){
			vector[i].add(addedVector.get(i));
		}
		return this;
	}
	
	public Vector mul(int multiplier){
		for(int i = 0; i < vector.length; i++){
			vector[i].mul(multiplier);
		}
		return this;
	}
	
	public Vector mul(Fraction multiplier){
		for(int i = 0; i < vector.length; i++){
			vector[i].mul(multiplier);
		}
		return this;
	}
	
	public int length(){
		return length;
	}
	
	public Fraction get(int index){
		return vector[index];
	}
	
	public String toString(){
		StringBuilder vecStr = new StringBuilder();
		vecStr.append('{');
		for(int i = 0; i < length; i++){
			vecStr.append(vector[i]);
			vecStr.append(", ");
		}
		vecStr.setLength(vecStr.length() - 2);
		vecStr.append('}');
		return vecStr.toString();
	}
}
