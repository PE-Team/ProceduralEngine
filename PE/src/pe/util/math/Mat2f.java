package pe.util.math;

public class Mat2f {

	//s[row][column]
	public float
		s00, s01,
		s10, s11;
	
	public Mat2f(){
		this.s00 = 1; this.s01 = 0;
		this.s10 = 0; this.s11 = 1; 
	}
	
	public Mat2f(float s00, float s10, float s01, float s11){
		this.s00 = s00; this.s01 = s01;
		this.s10 = s10; this.s11 = s11;
	}
	
	public Mat2f(Vec2f vec1, Vec2f vec2){
		this.s00 = vec1.x; 	this.s01 = vec2.x;
		this.s10 = vec1.y; 	this.s11 = vec2.y;
	}
	
	public Mat2f zero(){
		this.s00 = 0; this.s01 = 0;
		this.s10 = 0; this.s11 = 0;
		return this;
	}
	
	public static Mat2f add(Mat2f mat1, Mat2f mat2){
		return new Mat2f(
				mat1.s00+mat2.s00, mat1.s01+mat2.s01,
				mat1.s10+mat2.s10, mat1.s11+mat2.s11
				);
	}
	
	public static Mat2f sub(Mat2f mat1, Mat2f mat2){
		return new Mat2f(
				mat1.s00-mat2.s00, mat1.s01-mat2.s01,
				mat1.s10-mat2.s10, mat1.s11-mat2.s11
				);
	}
	
	public static Vec2f mul(Mat2f mat, Vec2f vec){
		return new Vec2f(
				mat.s00*vec.x + mat.s01*vec.y,
				mat.s10*vec.x + mat.s11*vec.y
				);
	}
	
	public static Mat2f mul(Mat2f mat1, Mat2f mat2){
		return new Mat2f(
				// First Row
				mat1.s00*mat2.s00+mat1.s01*mat2.s10,
				mat1.s00*mat2.s01+mat1.s01*mat2.s11,
				// Second Row
				mat1.s10*mat2.s00+mat1.s11*mat2.s10,
				mat1.s10*mat2.s01+mat1.s11*mat2.s11
				);
	}
	
	public static Mat2f divide(Mat2f mat1, Mat2f mat2){
		return Mat2f.mul(mat1, mat2.inverse());
	}
	
	public Mat2f mul(float scale){
		this.s00 *= scale; this.s01 *= scale;
		this.s10 *= scale; this.s11 *= scale;
		return this;
	}
	
	public Mat2f transpose(){
		return new Mat2f(
				this.s00, this.s10,
				this.s01, this.s11
				);
	}
	
	public float det(){
		return 	this.s00 * this.s11 - this.s01 * this.s10;
	}
	
	public Mat2f inverse(){
		return this.getAdjointMatrix().mul(1/this.det());
	}
	
	public Mat2f getAdjointMatrix(){
		return this.getCofactorMatrix().transpose();
	}
	
	public Mat2f getCofactorMatrix(){
		return new Mat2f(
					s11, -s10,
					-s01, s00
				);
	}
	
	public Vec2f getCol(int col){
		switch(col){
			case 0:
				return new Vec2f(s00,s10);
			case 1:
				return new Vec2f(s01,s11);
		}
		throw new IndexOutOfBoundsException("No Such Column: " + col);
	}
	
	public Vec2f getRow(int row){
		switch(row){
			case 0:
				return new Vec2f(s00,s01);
			case 1:
				return new Vec2f(s10,s11);
		}
		throw new IndexOutOfBoundsException("No such Row: " + row);
	}
	
	public String toString(){
		return "[" + getCol(0).toString() + "," + getCol(1).toString() + "]";
	}
}
