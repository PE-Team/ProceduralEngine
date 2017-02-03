package pe.util.math;

public class Mat3f {

	//s[row][column]
	public float
		s00, s01, s02,
		s10, s11, s12,
		s20, s21, s22;
	
	public Mat3f(){
		this.s00 = 1; this.s01 = 0; this.s02 = 0;
		this.s10 = 0; this.s11 = 1; this.s12 = 0;
		this.s20 = 0; this.s21 = 0; this.s22 = 1;
	}
	
	// Goes down the row, then goes to the next row
	public Mat3f(float s00, float s10, float s20, float s01, float s11, float s21, float s02, float s12, float s22){
		this.s00 = s00; this.s01 = s01; this.s02 = s02;
		this.s10 = s10; this.s11 = s11; this.s12 = s12;
		this.s20 = s20; this.s21 = s21; this.s22 = s22;
	}
	
	public Mat3f(Vec2f vec1, Vec2f vec2, Vec2f vec3){
		this.s00 = vec1.x; 	this.s01 = vec2.x; 	this.s02 = vec3.x;
		this.s10 = vec1.y; 	this.s11 = vec2.y; 	this.s12 = vec3.y;
		this.s20 = 0; 		this.s21 = 0; 		this.s22 = 1;
	}
	
	public Mat3f(Vec3f vec1, Vec3f vec2, Vec3f vec3){
		this.s00 = vec1.x; this.s01 = vec2.x; this.s02 = vec3.x;
		this.s10 = vec1.y; this.s11 = vec2.y; this.s12 = vec3.y;
		this.s20 = vec1.z; this.s21 = vec2.z; this.s22 = vec3.z;
	}
	
	public Mat3f(Mat2f mat){
		this.s00 = mat.s00;	this.s01 = mat.s01;	this.s02 = 0;
		this.s10 = mat.s10;	this.s11 = mat.s11;	this.s12 = 0;
		this.s20 = 0; 		this.s21 = 0; 		this.s22 = 1;
	}
	
	public Mat3f zero(){
		this.s00 = 0; this.s01 = 0; this.s02 = 0;
		this.s10 = 0; this.s11 = 0; this.s12 = 0;
		this.s20 = 0; this.s21 = 0; this.s22 = 0;
		return this;
	}
	
	public static Mat3f add(Mat3f mat1, Mat3f mat2){
		return new Mat3f(
				mat1.s00+mat2.s00, mat1.s01+mat2.s01, mat1.s02+mat2.s02,
				mat1.s10+mat2.s10, mat1.s11+mat2.s11, mat1.s12+mat2.s12,
				mat1.s20+mat2.s20, mat1.s21+mat2.s21, mat1.s22+mat2.s22
				);
	}
	
	public static Mat3f sub(Mat3f mat1, Mat3f mat2){
		return new Mat3f(
				mat1.s00-mat2.s00, mat1.s01-mat2.s01, mat1.s02-mat2.s02,
				mat1.s10-mat2.s10, mat1.s11-mat2.s11, mat1.s12-mat2.s12,
				mat1.s20-mat2.s20, mat1.s21-mat2.s21, mat1.s22-mat2.s22
				);
	}
	
	public static Vec3f mul(Mat3f mat, Vec3f vec){
		return new Vec3f(
				mat.s00*vec.x + mat.s01*vec.y + mat.s02*vec.z,
				mat.s10*vec.x + mat.s11*vec.y + mat.s12*vec.z,
				mat.s20*vec.x + mat.s21*vec.y + mat.s22*vec.z
				);
	}
	
	public static Mat3f mul(Mat3f mat1, Mat3f mat2){
		return new Mat3f(
				// First Row
				mat1.s00*mat2.s00+mat1.s01*mat2.s10+mat1.s02*mat2.s20,
				mat1.s00*mat2.s01+mat1.s01*mat2.s11+mat1.s02*mat2.s21,
				mat1.s00*mat2.s02+mat1.s01*mat2.s12+mat1.s02*mat2.s22,
				// Second Row
				mat1.s10*mat2.s00+mat1.s11*mat2.s10+mat1.s12*mat2.s20,
				mat1.s10*mat2.s01+mat1.s11*mat2.s11+mat1.s12*mat2.s21,
				mat1.s10*mat2.s02+mat1.s11*mat2.s12+mat1.s12*mat2.s22,
				// Third Row
				mat1.s20*mat2.s00+mat1.s21*mat2.s10+mat1.s22*mat2.s20,
				mat1.s20*mat2.s01+mat1.s21*mat2.s11+mat1.s22*mat2.s21,
				mat1.s20*mat2.s02+mat1.s21*mat2.s12+mat1.s22*mat2.s22
				);
	}
	
	public static Mat3f divide(Mat3f mat1, Mat3f mat2){
		return Mat3f.mul(mat1, mat2.inverse());
	}
	
	public Mat3f mul(float scale){
		this.s00 *= scale; this.s01 *= scale; this.s02 *= scale;
		this.s10 *= scale; this.s11 *= scale; this.s12 *= scale;
		this.s20 *= scale; this.s21 *= scale; this.s22 *= scale;
		return this;
	}
	
	// For 2D transformations
	// Order = translate * rotate * scale
	
	public Mat3f translate(Vec2f delta){
		Mat3f translation = new Mat3f();
		translation.s02 = delta.x;
		translation.s12 = delta.y;
		return Mat3f.mul(this, translation);
	}
	
	public Mat3f rotate(float theta){
		Mat3f rotation = new Mat3f();
		double rotRadian = Math.toRadians(theta);
		float cos = (float) Math.cos(rotRadian);
		float sin = (float) Math.sin(rotRadian);
		rotation.s00 =  cos;
		rotation.s01 =  sin;
		rotation.s10 = -sin;
		rotation.s11 =  cos;
		return Mat3f.mul(this, rotation);
	}
	
	public Mat3f scale(Vec2f delta){
		Mat3f scale = new Mat3f();
		scale.s00 = delta.x;
		scale.s11 = delta.y;
		return Mat3f.mul(this, scale);
	}
	
	public static Mat3f getTransformationMatrix(Vec2f translation, float rotation, float scale){
		double rotRadian = Math.toRadians(rotation);
		float cos = (float) Math.cos(rotRadian);
		float sin = (float) Math.sin(rotRadian);
		return new Mat3f(
				scale*cos,	scale*sin,	translation.x,
				-scale*sin,	scale*cos,	translation.y,
				0,			0,			1
				);
	}
	
	public static Mat3f getTransformationMatrix(Vec2f translation, float rotation, Vec2f scale){
		double rotRadian = Math.toRadians(rotation);
		float cos = (float) Math.cos(rotRadian);
		float sin = (float) Math.sin(rotRadian);
		return new Mat3f(
				scale.x*cos,	scale.y*sin,	translation.x,
				-scale.x*sin,	scale.y*cos,	translation.y,
				0,				0,				1
				);
	}
	
	// End of 2D transformation matrices
	
	public Mat3f transpose(){
		return new Mat3f(
				this.s00, this.s10, this.s20,
				this.s01, this.s11, this.s21,
				this.s02, this.s12, this.s22
				);
	}
	/*
	public float det(){
		return 	this.s00*this.s11*this.s22 + this.s01*this.s12*this.s20 + this.s02*this.s10*this.s21 
				- this.s20*this.s11*this.s02 - this.s21*this.s12*this.s00 - this.s22*this.s10*this.s01;
	}
	*/
	
	public float det(){
		return s00*this.getSubmatrix(0, 0).det() - s01*this.getSubmatrix(0, 1).det() + s02*this.getSubmatrix(0, 2).det();
	}
	
	public Mat3f inverse(){
		return this.getAdjointMatrix().mul(1/this.det());
	}
	
	public Mat3f getAdjointMatrix(){
		return this.getCofactorMatrix().transpose();
	}
	
	public Mat3f getCofactorMatrix(){
		return new Mat3f(
				this.getSubmatrix(0, 0).det(), this.getSubmatrix(1, 0).det(), this.getSubmatrix(2, 0).det(),
				this.getSubmatrix(0, 1).det(), this.getSubmatrix(1, 1).det(), this.getSubmatrix(2, 1).det(),
				this.getSubmatrix(0, 2).det(), this.getSubmatrix(1, 2).det(), this.getSubmatrix(2, 2).det()
				);
	}
	
	public Mat2f getSubmatrix(int centerX, int centerY){
		switch(centerX){
			case 0:
				switch(centerY){
					case 0:
						return new Mat2f(
											this.s11, this.s12,
											this.s21, this.s22
										 );
					case 1:
						return new Mat2f(
											this.s10, this.s12, 
											this.s20, this.s22
										);
					case 2:
						return new Mat2f(
											this.s10, this.s11, 
											this.s20, this.s21
										);
				}
			case 1:
				switch(centerY){
					case 0:
						return new Mat2f(
											this.s01, this.s02, 
											this.s21, this.s22
										);
					case 1:
						return new Mat2f(
											this.s00, this.s02, 
											this.s20, this.s22
										);
					case 2:
						return new Mat2f(
											this.s00, this.s02, 
											this.s10, this.s12
										);
				}
			case 2:
				switch(centerY){
					case 0:
						return new Mat2f(
											this.s10, this.s11, 
											this.s20, this.s21
										);
					case 1:
						return new Mat2f(
											this.s00, this.s01, 
											this.s20, this.s21
										);
					case 2:
						return new Mat2f(
											this.s00, this.s01, 
											this.s10, this.s11
										);
				}
		}
		throw new IndexOutOfBoundsException("Cannot get submatrix at x: " + centerX + ", y: " + centerY);
	}
	
	public Vec3f getCol(int col){
		switch(col){
			case 0:
				return new Vec3f(s00,s10,s20);
			case 1:
				return new Vec3f(s01,s11,s21);
			case 2:
				return new Vec3f(s02,s12,s22);
		}
		throw new IndexOutOfBoundsException("No Such Column: " + col);
	}
	
	public Vec3f getRow(int row){
		switch(row){
			case 0:
				return new Vec3f(s00,s01,s02);
			case 1:
				return new Vec3f(s10,s11,s12);
			case 2:
				return new Vec3f(s20,s21,s22);
		}
		throw new IndexOutOfBoundsException("No such Row: " + row);
	}
	
	public String toString(){
		return "[" + getCol(0).toString() + "," + getCol(1).toString() + "," + getCol(2).toString() + "]";
	}
}
