package pe.util.math;

public class Vecf {
	
	private float[] vector;
	private int length;

	public Vecf(float... values) {
		vector = values;
		length = values.length;
	}
	
	public Vecf add(Vecf addedVector){
		if(length() != addedVector.length())
			throw new IllegalArgumentException("Both vectors must be of the same length. ");
		
		for(int i = 0; i < vector.length; i++){
			vector[i] += addedVector.get(i);
		}
		return this;
	}
	
	public Vecf mul(float multiplier){
		for(int i = 0; i < vector.length; i++){
			vector[i] *= multiplier;
		}
		return this;
	}
	
	public int length(){
		return length;
	}
	
	public float get(int index){
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
