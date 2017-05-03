package pe.util.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Mat4f {

	//s[row][column]
		public float
			s00, s01, s02, s03,
			s10, s11, s12, s13,
			s20, s21, s22, s23,
			s30, s31, s32, s33;
		
		public Mat4f(){
			this.s00 = 1; this.s01 = 0; this.s02 = 0; this.s03 = 0;
			this.s10 = 0; this.s11 = 1; this.s12 = 0; this.s13 = 0;
			this.s20 = 0; this.s21 = 0; this.s22 = 1; this.s23 = 0;
			this.s30 = 0; this.s31 = 0; this.s32 = 0; this.s33 = 1;
		}
		
		// Goes down the column, then goes to the next column
		public Mat4f(float s00, float s10, float s20, float s30, float s01, float s11, float s21, float s31, float s02, float s12, float s22, float s32, float s03, float s13, float s23, float s33){
			this.s00 = s00; this.s01 = s01; this.s02 = s02; this.s03 = s03;
			this.s10 = s10; this.s11 = s11; this.s12 = s12; this.s13 = s13;
			this.s20 = s20; this.s21 = s21; this.s22 = s22; this.s23 = s23;
			this.s30 = s30; this.s31 = s31; this.s32 = s32; this.s33 = s33;
		}
		
		public Mat4f(Vec2f vec1, Vec2f vec2, Vec2f vec3, Vec2f vec4){
			this.s00 = vec1.x; 	this.s01 = vec2.x; 	this.s02 = vec3.x; 	this.s03 = vec4.x;
			this.s10 = vec1.y; 	this.s11 = vec2.y; 	this.s12 = vec3.y; 	this.s13 = vec4.y;
			this.s20 = 0; 		this.s21 = 0; 		this.s22 = 1; 		this.s23 = 0;
			this.s30 = 0; 		this.s31 = 0; 		this.s32 = 0; 		this.s33 = 1;
		}
		
		public Mat4f(Vec3f vec1, Vec3f vec2, Vec3f vec3, Vec3f vec4){
			this.s00 = vec1.x; 	this.s01 = vec2.x; 	this.s02 = vec3.x;	this.s03 = vec4.x;
			this.s10 = vec1.y; 	this.s11 = vec2.y; 	this.s12 = vec3.y;	this.s13 = vec4.x;
			this.s20 = vec1.z; 	this.s21 = vec2.z; 	this.s22 = vec3.z;	this.s23 = vec4.x;
			this.s30 = 0; 		this.s31 = 0; 		this.s32 = 0; 		this.s33 = 1;
		}
		
		public Mat4f(Vec4f vec1, Vec4f vec2, Vec4f vec3, Vec4f vec4){
			this.s00 = vec1.x; 	this.s01 = vec2.x; 	this.s02 = vec3.x;	this.s03 = vec4.x;
			this.s10 = vec1.y; 	this.s11 = vec2.y; 	this.s12 = vec3.y;	this.s13 = vec4.x;
			this.s20 = vec1.z; 	this.s21 = vec2.z; 	this.s22 = vec3.z;	this.s23 = vec4.x;
			this.s30 = vec1.w; 	this.s31 = vec2.w; 	this.s32 = vec3.w; 	this.s33 = vec4.w;
		}
		
		public Mat4f(Mat2f mat){
			this.s00 = mat.s00;	this.s01 = mat.s01;	this.s02 = 0; 		this.s03 = 0;
			this.s10 = mat.s10;	this.s11 = mat.s11;	this.s12 = 0; 		this.s13 = 0;
			this.s20 = 0; 		this.s21 = 0; 		this.s22 = 1; 		this.s23 = 0;
			this.s30 = 0; 		this.s31 = 0; 		this.s32 = 0; 		this.s33 = 1;
		}
		
		public Mat4f(Mat3f mat){
			this.s00 = mat.s00;	this.s01 = mat.s01;	this.s02 = mat.s02; this.s03 = 0;
			this.s10 = mat.s10;	this.s11 = mat.s11;	this.s12 = mat.s12; this.s13 = 0;
			this.s20 = mat.s20; this.s21 = mat.s21; this.s22 = mat.s22; this.s23 = 0;
			this.s30 = 0; 		this.s31 = 0; 		this.s32 = 0; 		this.s33 = 1;
		}
		
		public Mat4f zero(){
			this.s00 = 0; this.s01 = 0; this.s02 = 0; this.s03 = 0;
			this.s10 = 0; this.s11 = 0; this.s12 = 0; this.s13 = 0;
			this.s20 = 0; this.s21 = 0; this.s22 = 0; this.s23 = 0;
			this.s30 = 0; this.s31 = 0; this.s32 = 0; this.s33 = 0;
			return this;
		}
		
		public static Mat4f add(Mat4f mat1, Mat4f mat2){
			return new Mat4f(
					mat1.s00+mat2.s00, mat1.s01+mat2.s01, mat1.s02+mat2.s02, mat1.s03+mat2.s03,
					mat1.s10+mat2.s10, mat1.s11+mat2.s11, mat1.s12+mat2.s12, mat1.s13+mat2.s13,
					mat1.s20+mat2.s20, mat1.s21+mat2.s21, mat1.s22+mat2.s22, mat1.s23+mat2.s23,
					mat1.s30+mat2.s30, mat1.s31+mat2.s31, mat1.s32+mat2.s32, mat1.s33+mat2.s33
					);
		}
		
		public static Mat4f sub(Mat4f mat1, Mat4f mat2){
			return new Mat4f(
					mat1.s00-mat2.s00, mat1.s01-mat2.s01, mat1.s02-mat2.s02, mat1.s03-mat2.s03,
					mat1.s10-mat2.s10, mat1.s11-mat2.s11, mat1.s12-mat2.s12, mat1.s13-mat2.s13,
					mat1.s20-mat2.s20, mat1.s21-mat2.s21, mat1.s22-mat2.s22, mat1.s23-mat2.s23,
					mat1.s30-mat2.s30, mat1.s31-mat2.s31, mat1.s32-mat2.s32, mat1.s33-mat2.s33
					);
		}
		
		public static Vec4f mul(Mat4f mat, Vec4f vec){
			return new Vec4f(
					mat.s00*vec.x + mat.s01*vec.y + mat.s02*vec.z + mat.s03*vec.w,
					mat.s10*vec.x + mat.s11*vec.y + mat.s12*vec.z + mat.s13*vec.w,
					mat.s20*vec.x + mat.s21*vec.y + mat.s22*vec.z + mat.s23*vec.w,
					mat.s30*vec.x + mat.s31*vec.y + mat.s32*vec.z + mat.s33*vec.w
					);
		}
		
		public FloatBuffer toFloatBuffer(){
			FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
			
			buffer.put(s00).put(s10).put(s20).put(s30); // Column 1
			buffer.put(s01).put(s11).put(s21).put(s31); // Column 2
			buffer.put(s02).put(s12).put(s22).put(s32); // Column 3
			buffer.put(s03).put(s13).put(s23).put(s33); // Column 4
			
			buffer.flip();
			return buffer;
		}
		
		public static Mat4f mul(Mat4f mat1, Mat4f mat2){
			return new Mat4f(
					// First Row
					mat1.s00*mat2.s00+mat1.s01*mat2.s10+mat1.s02*mat2.s20+mat1.s03*mat2.s30,
					mat1.s00*mat2.s01+mat1.s01*mat2.s11+mat1.s02*mat2.s21+mat1.s03*mat2.s31,
					mat1.s00*mat2.s02+mat1.s01*mat2.s12+mat1.s02*mat2.s22+mat1.s03*mat2.s32,
					mat1.s00*mat2.s03+mat1.s01*mat2.s13+mat1.s02*mat2.s23+mat1.s03*mat2.s33,
					// Second Row
					mat1.s10*mat2.s00+mat1.s11*mat2.s10+mat1.s12*mat2.s20+mat1.s13*mat2.s30,
					mat1.s10*mat2.s01+mat1.s11*mat2.s11+mat1.s12*mat2.s21+mat1.s13*mat2.s31,
					mat1.s10*mat2.s02+mat1.s11*mat2.s12+mat1.s12*mat2.s22+mat1.s13*mat2.s32,
					mat1.s10*mat2.s03+mat1.s11*mat2.s13+mat1.s12*mat2.s23+mat1.s13*mat2.s33,
					// Third Row
					mat1.s20*mat2.s00+mat1.s21*mat2.s10+mat1.s22*mat2.s20+mat1.s23*mat2.s30,
					mat1.s20*mat2.s01+mat1.s21*mat2.s11+mat1.s22*mat2.s21+mat1.s23*mat2.s31,
					mat1.s20*mat2.s02+mat1.s21*mat2.s12+mat1.s22*mat2.s22+mat1.s23*mat2.s32,
					mat1.s20*mat2.s03+mat1.s21*mat2.s13+mat1.s22*mat2.s23+mat1.s23*mat2.s33,
					//Fourth Row
					mat1.s30*mat2.s00+mat1.s31*mat2.s10+mat1.s32*mat2.s20+mat1.s33*mat2.s30,
					mat1.s30*mat2.s01+mat1.s31*mat2.s11+mat1.s32*mat2.s21+mat1.s33*mat2.s31,
					mat1.s30*mat2.s02+mat1.s31*mat2.s12+mat1.s32*mat2.s22+mat1.s33*mat2.s32,
					mat1.s30*mat2.s03+mat1.s31*mat2.s13+mat1.s32*mat2.s23+mat1.s33*mat2.s33
					);
		}
		
		public static Mat4f divide(Mat4f mat1, Mat4f mat2){
			return Mat4f.mul(mat1, mat2.inverse());
		}
		
		public Mat4f mul(float scale){
			this.s00 *= scale; this.s01 *= scale; this.s02 *= scale; this.s03 *= scale;
			this.s10 *= scale; this.s11 *= scale; this.s12 *= scale; this.s13 *= scale;
			this.s20 *= scale; this.s21 *= scale; this.s22 *= scale; this.s23 *= scale;
			this.s30 *= scale; this.s31 *= scale; this.s32 *= scale; this.s33 *= scale;
			return this;
		}
		
		// For 2D transformations
		// Order = translate * rotate * scale
		
		public Mat4f translate(Vec3f delta){
			Mat4f translation = new Mat4f();
			translation.s03 = delta.x;
			translation.s13 = delta.y;
			translation.s23 = delta.z;
			return Mat4f.mul(this, translation);
		}
		
		public Mat4f rotateX(float roll){
			Mat4f rotation = new Mat4f();
			double rotRadian = Math.toRadians(roll);
			float cos = (float) Math.cos(rotRadian);
			float sin = (float) Math.sin(rotRadian);
			rotation.s00 =  cos;
			rotation.s01 = -sin;
			rotation.s10 =  sin;
			rotation.s11 =  cos;
			return Mat4f.mul(this, rotation);
		}
		
		public Mat4f rotateY(float pitch){
			Mat4f rotation = new Mat4f();
			double rotRadian = Math.toRadians(pitch);
			float cos = (float) Math.cos(rotRadian);
			float sin = (float) Math.sin(rotRadian);
			rotation.s00 =  cos;
			rotation.s02 =  sin;
			rotation.s20 = -sin;
			rotation.s22 =  cos;
			return Mat4f.mul(this, rotation);
		}
		
		public Mat4f rotateZ(float yaw){
			Mat4f rotation = new Mat4f();
			double rotRadian = Math.toRadians(yaw);
			float cos = (float) Math.cos(rotRadian);
			float sin = (float) Math.sin(rotRadian);
			rotation.s00 =  cos;
			rotation.s01 = -sin;
			rotation.s10 =  sin;
			rotation.s11 =  cos;
			return Mat4f.mul(this, rotation);
		}
		
		public Mat4f rotate(Vec3f rotationAngles){
			Mat4f rotation = new Mat4f();
			double rotXRadian = Math.toRadians(rotationAngles.x);
			double rotYRadian = Math.toRadians(rotationAngles.y);
			double rotZRadian = Math.toRadians(rotationAngles.z);
			float cosRoll = (float) Math.cos(rotXRadian);
			float sinRoll = (float) Math.sin(rotXRadian);
			float cosPitch = (float) Math.cos(rotYRadian);
			float sinPitch = (float) Math.sin(rotYRadian);
			float cosYaw = (float) Math.cos(rotZRadian);
			float sinYaw = (float) Math.sin(rotZRadian);
			rotation.s00 = cosYaw*cosPitch;
			rotation.s01 = cosYaw*sinPitch*sinRoll - sinYaw*cosRoll;
			rotation.s02 = cosYaw*sinPitch*sinRoll + sinYaw*cosRoll;
			rotation.s10 = sinYaw*cosPitch;
			rotation.s11 = sinYaw*sinPitch*sinRoll + cosYaw*cosRoll;
			rotation.s12 = sinYaw*sinPitch*sinRoll - cosYaw*cosRoll;
			rotation.s20 = -sinPitch;
			rotation.s21 = cosPitch*sinRoll;
			rotation.s22 = cosPitch*cosRoll;
			return Mat4f.mul(this, rotation);
		}
		
		public Mat4f scale(float delta){
			Mat4f scale = new Mat4f();
			scale.s33 = delta;
			return Mat4f.mul(this, scale);
		}
		
		public Mat4f scale(Vec3f delta){
			Mat4f scale = new Mat4f();
			scale.s00 = delta.x;
			scale.s11 = delta.y;
			scale.s22 = delta.z;
			return Mat4f.mul(this, scale);
		}
		
		public static Mat4f getTransformationMatrix(Vec3f translation, Vec3f rotation, Vec3f scale){
			return new Mat4f().translate(translation).rotate(rotation).scale(scale);
		}
		
		public static Mat4f getTransformationMatrix(Vec3f translation, Vec3f rotation, float scale){
			return new Mat4f().translate(translation).rotate(rotation).scale(scale);
		}
		
		public static Mat4f getOrthographicMatrix(float left, float right, float bottom, float top, float near, float far){
			return new Mat4f(
					2 / (right - left), 	0, 						0, 					-(right + left)/(right - left),
					0, 						2 / (top - bottom), 	0, 					-(top + bottom)/(top - bottom),
					0, 						0, 						2 / (far - near), 	-(far + near)/(far - near),
					0, 						0, 						0, 					1
					);
		}
		
		// End of 2D transformation matrices
		
		public Mat4f transpose(){
			return new Mat4f(
					this.s00, this.s10, this.s20, this.s30,
					this.s01, this.s11, this.s21, this.s31,
					this.s02, this.s12, this.s22, this.s32,
					this.s03, this.s13, this.s23, this.s33
					);
		}
		
		public float det(){
			return 	s00*this.getSubmatrix(0, 0).det() - s01*this.getSubmatrix(0, 1).det() + s02*this.getSubmatrix(0, 2).det() - s03*this.getSubmatrix(0, 3).det();
		}
		
		public Mat4f inverse(){
			return this.getAdjointMatrix().scale(1/this.det());
		}
		
		public Mat4f getAdjointMatrix(){
			return this.getCofactorMatrix().transpose();
		}
		
		public Mat4f getCofactorMatrix(){
			return new Mat4f(
					this.getSubmatrix(0, 0).det(), this.getSubmatrix(1, 0).det(), this.getSubmatrix(2, 0).det(), this.getSubmatrix(3, 0).det(),
					this.getSubmatrix(0, 1).det(), this.getSubmatrix(1, 1).det(), this.getSubmatrix(2, 1).det(), this.getSubmatrix(3, 1).det(),
					this.getSubmatrix(0, 2).det(), this.getSubmatrix(1, 2).det(), this.getSubmatrix(2, 2).det(), this.getSubmatrix(3, 2).det(),
					this.getSubmatrix(0, 3).det(), this.getSubmatrix(1, 3).det(), this.getSubmatrix(2, 3).det(), this.getSubmatrix(3, 3).det()
					);
		}
		
		public Mat3f getSubmatrix(int centerX, int centerY){
			switch(centerX){
				case 0:
					switch(centerY){
						case 0:
							return new Mat3f(
												this.s11, this.s12, this.s13,
												this.s21, this.s22, this.s23,
												this.s31, this.s32, this.s33
											 );
						case 1:
							return new Mat3f(
												this.s01, this.s02, this.s03,
												this.s21, this.s22, this.s23,
												this.s31, this.s32, this.s33
											 );
						case 2:
							return new Mat3f(
												this.s01, this.s02, this.s03,
												this.s11, this.s12, this.s13,
												this.s31, this.s32, this.s33
											 );
						case 3:
							return new Mat3f(
												this.s01, this.s02, this.s03,
												this.s11, this.s12, this.s13,
												this.s21, this.s22, this.s23
											 );
					}
				case 1:
					switch(centerY){
						case 0:
							return new Mat3f(
												this.s10, this.s12, this.s13,
												this.s20, this.s22, this.s23,
												this.s30, this.s32, this.s33
											 );
						case 1:
							return new Mat3f(
												this.s00, this.s02, this.s03,
												this.s20, this.s22, this.s23,
												this.s30, this.s32, this.s33
											 );
						case 2:
							return new Mat3f(
												this.s00, this.s02, this.s03,
												this.s10, this.s12, this.s13,
												this.s30, this.s32, this.s33
											 );
						case 3:
							return new Mat3f(
												this.s00, this.s02, this.s03,
												this.s10, this.s12, this.s13,
												this.s20, this.s22, this.s23
											 );
					}
				case 2:
					switch(centerY){
						case 0:
							return new Mat3f(
												this.s10, this.s11, this.s13,
												this.s20, this.s21, this.s23,
												this.s30, this.s31, this.s33
											 );
						case 1:
							return new Mat3f(
												this.s00, this.s01, this.s03,
												this.s20, this.s21, this.s23,
												this.s30, this.s31, this.s33
											 );
						case 2:
							return new Mat3f(
												this.s00, this.s01, this.s03,
												this.s10, this.s11, this.s13,
												this.s30, this.s31, this.s33
											 );
						case 3:
							return new Mat3f(
												this.s00, this.s01, this.s03,
												this.s10, this.s11, this.s13,
												this.s20, this.s21, this.s23
											 );
					}
				case 3:
					switch(centerY){
						case 0:
							return new Mat3f(
												this.s10, this.s11, this.s12,
												this.s20, this.s21, this.s22,
												this.s30, this.s31, this.s32
											 );
						case 1:
							return new Mat3f(
												this.s00, this.s01, this.s02,
												this.s20, this.s21, this.s22,
												this.s30, this.s31, this.s32
											 );
						case 2:
							return new Mat3f(
												this.s00, this.s01, this.s02,
												this.s10, this.s11, this.s12,
												this.s30, this.s31, this.s32
											 );
						case 3:
							return new Mat3f(
												this.s00, this.s01, this.s02,
												this.s10, this.s11, this.s12,
												this.s20, this.s21, this.s22
											 );
					}
			}
			throw new IndexOutOfBoundsException("Cannot get submatrix at x: " + centerX + ", y: " + centerY);
		}
		
		public Vec4f getCol(int col){
			switch(col){
				case 0:
					return new Vec4f(s00,s10,s20,s30);
				case 1:
					return new Vec4f(s01,s11,s21,s31);
				case 2:
					return new Vec4f(s02,s12,s22,s32);
				case 3:
					return new Vec4f(s03,s13,s23,s33);
			}
			throw new IndexOutOfBoundsException("No Such Column: " + col);
		}
		
		public Vec4f getRow(int row){
			switch(row){
				case 0:
					return new Vec4f(s00,s01,s02,s03);
				case 1:
					return new Vec4f(s10,s11,s12,s13);
				case 2:
					return new Vec4f(s20,s21,s22,s23);
				case 3:
					return new Vec4f(s30,s31,s32,s33);
			}
			throw new IndexOutOfBoundsException("No such Row: " + row);
		}
		
		public String toString(){
			return "[" + getCol(0).toString() + "," + getCol(1).toString() + "," + getCol(2).toString() + "," + getCol(3).toString() + "]";
		}
}
