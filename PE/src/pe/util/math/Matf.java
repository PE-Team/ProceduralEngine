package pe.util.math;

import pe.util.Util;

public class Matf {
	
	private float[][] matrix;
	private int rows;
	private int columns;
	
	public Matf(int dimension){
		if(dimension <= 0)
			throw new IllegalArgumentException("The matrix must have a dimension of at least 1.\n" + dimension + " dimensions were given.");
		
		rows = dimension;
		columns = dimension;
		matrix = new float[dimension][dimension];
		
		for(int i = 0; i < dimension; i++){
			matrix[i][i] = 1;
		}
	}
	
	public Matf(int rows, int columns){
		if(rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("The matrix must have at least 1 row and at least 1 column.\n" + rows + " rows were given and " + columns + " columns were given.");
		
		this.rows = rows;
		this.columns = columns;
		matrix = new float[rows][columns];
		
		int iterations = rows < columns ? rows : columns;
		for(int i = 0; i < iterations; i++){
			matrix[i][i] = 1;
		}
	}
	
	public Matf(int rows, int columns, float... values){
		if(rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("The matrix must have at least 1 row and at least 1 column.\n" + rows + " rows were given and " + columns + " columns were given.");
		
		if(values.length < rows * columns) 
			throw new IllegalArgumentException("Must have the same ammount of values as the dimensions of the Matrix.\n" + values.length + " values were given, " + (rows*columns) + " values were needed.");
		
		this.rows = rows;
		this.columns = columns;
		matrix = new float[rows][columns];
		
		for(int i = 0; i < values.length; i++){
			int row = i / columns;
			int column = i % columns;
			matrix[row][column] = values[i];
		}
	}
	
	public Matf(Matf matrix1, Matf matrix2){
		rows = matrix1.rows() > matrix2.rows() ? matrix1.rows() : matrix2.rows();
		columns = matrix1.columns() + matrix2.columns();
		matrix = new float[rows][columns];
		
		for(int r = 0; r < matrix1.rows(); r++){
			for(int c = 0; c < matrix1.columns(); c++){
				matrix[r][c] = matrix1.get(r, c);
			}
		}
		
		for(int r = 0; r < matrix2.rows(); r++){
			for(int c = 0; c < matrix2.columns(); c++){
				matrix[r][c + matrix1.columns()] = matrix2.get(r, c);
			}
		}
		
		for(int r = matrix2.rows(); r < rows; r++){
			for(int c = matrix1.columns(); c < columns; c++){
				if(r == c){
					matrix[r][c] = 1;
				}else{
					matrix[r][c] = 0;
				}
			}
		}
	}
	
	private void checkIsColInBounds(int column){
		if(column < 0 || column >= columns)
			throw new IllegalArgumentException("The column given must be an actual column.\nThe column " + column + " was given, there are " + columns + " column(s)");
	}
	
	private void checkIsInBounds(int row, int column){
		checkIsRowInBounds(row);
		checkIsColInBounds(column);
	}
	
	private void checkIsRowInBounds(int row){
		if(row < 0 || row >= rows)
			throw new IllegalArgumentException("The row given must be an actual row.\nThe row " + row + " was given, there are " + rows + " row(s)");
	}
	
	public int columns(){
		return columns;
	}
	
	public boolean equals(Object obj){
		if(!obj.getClass().equals(Matf.class)) return false;
		
		Matf comparedMatrix = (Matf) obj;
		
		if(comparedMatrix.rows() != rows || comparedMatrix.columns() != columns) return false;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(comparedMatrix.get(r, c) != matrix[r][c]) return false;
			}
		}
		
		return true;
	}
	
	public float getDet(){
		if(rows == 1 && columns == 1){
			return matrix[0][0];
		}else{
			float determinant = 0;
			for(int c = 0; c < columns; c++){
				determinant += (int) Math.pow(-1, c) * matrix[0][c] * getSubmatrix(0, c).getDet();
			}
			return determinant;
		}
	}
	
	public float get(int row, int column){
		checkIsInBounds(row, column);
		return matrix[row][column];
	}
	
	public Vecf getCol(int column){
		checkIsColInBounds(column);
		
		float[] colVec = new float[rows];
		for(int r = 0; r < rows; r++){
			colVec[r] = matrix[r][column];
		}
		return new Vecf(colVec);
	}
	
	public Matf getMat(int rowStart, int colStart, int rowLength, int colLength){
		checkIsInBounds(rowStart, colStart);
		checkIsInBounds(rowStart + rowLength - 1, colStart + colLength - 1);
		
		float[] values = new float[rowLength * colLength];
		for(int r = 0; r < rowLength; r++){
			for(int c = 0; c < colLength; c++){
				values[r * rowLength + c] = matrix[r + rowStart][c + colStart];
			}
		}
		
		return new Matf(rowLength, colLength, values);
	}
	
	public Vecf getRow(int row){
		checkIsRowInBounds(row);
		
		float[] rowVec = new float[columns];
		for(int c = 0; c < columns; c++){
			rowVec[c] = matrix[row][c];
		}
		return new Vecf(rowVec);
	}
	
	public Matf getInverse(){
		if(columns != rows)
			throw new IllegalArgumentException("The inverse of a matrix can only be found for sqare matricies.\nThis matrix had " + rows + " rows and " + columns + " columns.");
		
		Matf bigM = new Matf(this, new Matf(rows, columns));
		return bigM.RREF().getMat(0, columns, rows, columns);
	}
	
	public boolean isInversible(){
		return false;
	}
	
	public float largestValue(){
		float largestVal = Float.MIN_VALUE;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(largestVal < matrix[r][c])
					largestVal = matrix[r][c];
			}
		}
		
		return largestVal;
	}
	
	public float[] largestValueInCols(){
		float[] largestVals = new float[columns];
		
		for(int c = 0; c < columns; c++){
			float largestVal = Float.MIN_VALUE;
			for(int r = 0; r < rows; r++){
				if(largestVal < matrix[r][c])
					largestVal = matrix[r][c];
			}
			largestVals[c] = largestVal;
		}
		return largestVals;
	}
	
	private int[] mostDigitsInCols(){
		int[] mostDigits = new int[columns];
		
		for(int c = 0; c < columns; c++){
			int mostDigitsInCol = Integer.MIN_VALUE;
			for(int r = 0; r < rows; r++){
				int digitsInCol = Maths.digitsIn(matrix[r][c]);
				if(mostDigitsInCol < digitsInCol)
					mostDigitsInCol = digitsInCol;
			}
			mostDigits[c] = mostDigitsInCol;
		}
		return mostDigits;
	}
	
	public Matf REF(){
		int iterations = rows < columns ? rows : columns;
		
		for(int i = 0; i < iterations; i++){
			if(matrix[i][i] == 0){
				boolean foundSwap = false;
				for(int r = i + 1; r < rows; r++){
					if(matrix[r][i] != 0){
						foundSwap = true;
						rowInterchange(i, r);
						break;
					}
				}
				if(!foundSwap) continue;
			}
			rowScale(i, 1/matrix[i][i]);
			
			for(int r = i + 1; r < rows; r++){
				rowAdd(r, i, -matrix[r][i]);
			}
		}
		
		return this;
	}
	
	public Matf rowAdd(int row1, int row2, float matrix2){
		checkIsRowInBounds(row1);
		checkIsRowInBounds(row2);
		
		setRow(row1, getRow(row1).add(getRow(row2).mul(matrix2)));
		
		return null;
	}
	
	public Matf rowInterchange(int row1, int row2){
		checkIsRowInBounds(row1);
		checkIsRowInBounds(row2);
		
		Vecf row1Vec = getRow(row1);
		setRow(row1, getRow(row2));
		setRow(row2, row1Vec);
		
		return this;
	}
	
	public int rows(){
		return rows;
	}
	
	public Matf rowScale(int row, float scale){
		checkIsRowInBounds(row);
		
		setRow(row, getRow(row).mul(scale));
		
		return this;
	}
	
	public Matf RREF(){
		REF();
		
		for(int r = 1; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(matrix[r - 1][c] !=0){
					if(c + 1 < columns){
						for(int row = 0; row < r; row++){
							rowAdd(row, r, -matrix[row][c + 1]);
						}
					}
					break;
				}
			}
		}
		
		return this;
	}
	
	public Matf set(int row, int column, float value){
		checkIsInBounds(row, column);
		
		matrix[row][column] = value;
		return this;
	}
	
	public Matf setCol(int column, Vecf values){
		checkIsColInBounds(column);
		
		if(values.length() != rows)
			throw new IllegalArgumentException("The number of values must be the same as the number of rows in the matrix.\n" + values.length() + " values were given, " + rows + " values were needed.");
		
		for(int r = 0; r < rows; r++){
			set(r, column, values.get(r));
		}
		
		return this;
	}
	
	public Matf setRow(int row, Vecf values){
		checkIsRowInBounds(row);
		
		if(values.length() != columns)
			throw new IllegalArgumentException("The number of values must be the same as the number of columns in the matrix.\n" + values.length() + " values were given, " + columns + " values were needed.");
		
		for(int c = 0; c < columns; c++){
			set(row, c, values.get(c));
		}
		
		return this;
	}
	
	public Matf getSubmatrix(int row, int column){
		checkIsInBounds(row, column);
		
		float[] values = new float[(rows - 1) * (columns - 1)];
		for(int r = 0; r < row; r++){
			for(int c = 0; c < column; c++){
				values[r * (columns - 1) + c] = matrix[r][c];
			}
		}
		
		for(int r = row + 1; r < rows; r++){
			for(int c = 0; c < column; c++){
				values[(r - 1) * (columns - 1) + c] = matrix[r][c];
			}
		}
		
		for(int r = 0; r < row; r++){
			for(int c = column + 1; c < columns; c++){
				values[r * (columns - 1) + (c - 1)] = matrix[r][c];
			}
		}
		
		for(int r = row + 1; r < rows; r++){
			for(int c = column + 1; c < columns; c++){
				values[(r - 1) * (columns - 1) + (c - 1)] = matrix[r][c];
			}
		}
		
		return new Matf(rows - 1, columns -1, values);
	}
	
	public String toMatString(){
		int[] digitsInCol = mostDigitsInCols();
		
		StringBuilder str = new StringBuilder();
		for(int r = 0; r < rows; r++){
			
			str.append("|");
			for(int c = 0; c < columns; c++){
				
				str.append(Util.addSpaces(Float.toString(matrix[r][c]), digitsInCol[c]));
				str.append("  ");
			}
			str.deleteCharAt(str.length() - 1);
			str.deleteCharAt(str.length() - 1);
			str.append("|\n");
		}
		str.deleteCharAt(str.length() - 1);
		return str.toString();
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(int c = 0; c < columns; c++){
			str.append("{");
			for(int r = 0; r < rows; r++){
				str.append(matrix[r][c]);
				str.append(",");
			}
			str.deleteCharAt(str.length() - 1);
			str.append("},");
		}
		str.deleteCharAt(str.length() - 1);
		str.append("]");
		return str.toString();
	}
}
