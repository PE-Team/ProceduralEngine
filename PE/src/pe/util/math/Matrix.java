package pe.util.math;

import pe.util.Util;

public class Matrix {
	
	private Fraction[][] matrix;
	private int rows;
	private int columns;
	
	public Matrix divide(int numb) {
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				matrix[r][c].divide(numb);
			}
		}
		return this;
	}

	public Matrix(int dimension){
		if(dimension <= 0)
			throw new IllegalArgumentException("The matrix must have a dimension of at least 1.\n" + dimension + " dimensions were given.");
		
		rows = dimension;
		columns = dimension;
		matrix = new Fraction[dimension][dimension];
		
		for(int r = 0; r < dimension; r++){
			for(int c = 0; c < dimension; c++){
				if(r == c){
					matrix[r][c] = new Fraction(1,1);
				}else{
					matrix[r][c] = Fraction.ZERO;
				}
			}
		}
	}
	
	public Matrix(int rows, int columns){
		if(rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("The matrix must have at least 1 row and at least 1 column.\n" + rows + " rows were given and " + columns + " columns were given.");
		
		this.rows = rows;
		this.columns = columns;
		matrix = new Fraction[rows][columns];

		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(r == c){
					matrix[r][c] = new Fraction(1,1);
				}else{
					matrix[r][c] = Fraction.ZERO;
				}
			}
		}
	}
	
	public Matrix(int rows, int columns, int... values){
		if(rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("The matrix must have at least 1 row and at least 1 column.\n" + rows + " rows were given and " + columns + " columns were given.");
		
		if(values.length < rows * columns) 
			throw new IllegalArgumentException("Must have the same ammount of values as the dimensions of the Matrix.\n" + values.length + " values were given, " + (rows*columns) + " values were needed.");
		
		this.rows = rows;
		this.columns = columns;
		matrix = new Fraction[rows][columns];
		
		for(int i = 0; i < values.length; i++){
			int row = i / columns;
			int column = i % columns;
			matrix[row][column] = new Fraction(values[i]);
		}
	}
	
	public Matrix(int rows, int columns, Fraction... values){
		if(rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("The matrix must have at least 1 row and at least 1 column.\n" + rows + " rows were given and " + columns + " columns were given.");
		
		if(values.length < rows * columns) 
			throw new IllegalArgumentException("Must have the same ammount of values as the dimensions of the Matrix.\n" + values.length + " values were given, " + (rows*columns) + " values were needed.");
		
		this.rows = rows;
		this.columns = columns;
		matrix = new Fraction[rows][columns];
		
		for(int i = 0; i < values.length; i++){
			int row = i / columns;
			int column = i % columns;
			matrix[row][column] = values[i];
		}
	}
	
	public Matrix(Matrix matrix1, Matrix matrix2){
		rows = matrix1.rows() > matrix2.rows() ? matrix1.rows() : matrix2.rows();
		columns = matrix1.columns() + matrix2.columns();
		matrix = new Fraction[rows][columns];
		
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
					matrix[r][c] = new Fraction(1,1);
				}else{
					matrix[r][c] = new Fraction(0,1);
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
		if(!obj.getClass().equals(Matrix.class)) return false;
		
		Matrix comparedMatrix = (Matrix) obj;
		
		if(comparedMatrix.rows() != rows || comparedMatrix.columns() != columns) return false;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(comparedMatrix.get(r, c) != matrix[r][c]) return false;
			}
		}
		
		return true;
	}
	
	public Fraction getDet(){
		if(rows == 1 && columns == 1){
			return matrix[0][0].copy();
		}else{
			Fraction determinant = new Fraction(0, 1);
			for(int c = 0; c < columns; c++){
				determinant.add(matrix[0][c].copy().mul(new Fraction((int) Math.pow(-1, c), 1)).mul(getSubmatrix(0, c).getDet()));
			}
			return determinant;
		}
	}
	
	public Fraction get(int row, int column){
		checkIsInBounds(row, column);
		return matrix[row][column].copy();
	}
	
	public Vector getCol(int column){
		checkIsColInBounds(column);
		
		Fraction[] colVec = new Fraction[rows];
		for(int r = 0; r < rows; r++){
			colVec[r] = matrix[r][column].copy();
		}
		return new Vector(colVec);
	}
	
	public Matrix getMat(int rowStart, int colStart, int rowLength, int colLength){
		checkIsInBounds(rowStart, colStart);
		checkIsInBounds(rowStart + rowLength - 1, colStart + colLength - 1);
		
		Fraction[] values = new Fraction[rowLength * colLength];
		for(int r = 0; r < rowLength; r++){
			for(int c = 0; c < colLength; c++){
				values[r * rowLength + c] = matrix[r + rowStart][c + colStart].copy();
			}
		}
		
		return new Matrix(rowLength, colLength, values);
	}
	
	public Vector getRow(int row){
		checkIsRowInBounds(row);
		
		Fraction[] rowVec = new Fraction[columns];
		for(int c = 0; c < columns; c++){
			rowVec[c] = matrix[row][c].copy();
		}
		return new Vector(rowVec);
	}
	
	public Matrix getInverse(){
		if(columns != rows)
			throw new IllegalArgumentException("The inverse of a matrix can only be found for sqare matricies.\nThis matrix had " + rows + " rows and " + columns + " columns.");
		
		Matrix bigM = new Matrix(this, new Matrix(rows, columns));
		return bigM.RREF().getMat(0, columns, rows, columns);
	}
	
	public boolean isInversible(){
		return false;
	}
	
	public float largestValue(){
		float largestVal = Float.MIN_VALUE;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(largestVal < matrix[r][c].toDecf())
					largestVal = matrix[r][c].toDecf();
			}
		}
		
		return largestVal;
	}
	
	public float[] largestValueInCols(){
		float[] largestVals = new float[columns];
		
		for(int c = 0; c < columns; c++){
			float largestVal = Float.MIN_VALUE;
			for(int r = 0; r < rows; r++){
				if(largestVal < matrix[r][c].toDecf())
					largestVal = matrix[r][c].toDecf();
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
				int digitsInCol = matrix[r][c].toString().length();
				if(mostDigitsInCol < digitsInCol)
					mostDigitsInCol = digitsInCol;
			}
			mostDigits[c] = mostDigitsInCol;
		}
		return mostDigits;
	}
	
	public Matrix REF(){
		int iterations = rows < columns ? rows : columns;
		
		for(int i = 0; i < iterations; i++){
			if(matrix[i][i].equals(Fraction.ZERO)){
				boolean foundSwap = false;
				for(int r = i + 1; r < rows; r++){
					if(!matrix[r][i].equals(Fraction.ZERO)){
						foundSwap = true;
						rowInterchange(i, r);
						break;
					}
				}
				if(!foundSwap) continue;
			}
			rowScale(i, get(i,i).flip());
			
			for(int r = i + 1; r < rows; r++){
				rowAdd(r, i, get(r,i).mul(-1));
			}
		}
		
		return this;
	}
	
	public Matrix rowAdd(int row1, int row2, Fraction row2Multiplier){
		checkIsRowInBounds(row1);
		checkIsRowInBounds(row2);
		
		setRow(row1, getRow(row1).add(getRow(row2).mul(row2Multiplier)));
		return this;
	}
	
	public Matrix rowInterchange(int row1, int row2){
		checkIsRowInBounds(row1);
		checkIsRowInBounds(row2);
		
		Vector row1Vec = getRow(row1);
		setRow(row1, getRow(row2));
		setRow(row2, row1Vec);
		
		return this;
	}
	
	public int rows(){
		return rows;
	}
	
	public Matrix rowScale(int row, Fraction scale){
		checkIsRowInBounds(row);
		
		setRow(row, getRow(row).mul(scale));
		
		return this;
	}
	
	public Matrix RREF(){
		REF();
		
		for(int r = 1; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(!matrix[r - 1][c].equals(Fraction.ZERO)){
					if(c + 1 < columns){
						for(int row = 0; row < r; row++){
							rowAdd(row, r, get(row, c + 1).mul(-1));
						}
					}
					break;
				}
			}
		}
		
		return this;
	}
	
	public Matrix set(int row, int column, Fraction value){
		checkIsInBounds(row, column);
		
		matrix[row][column] = value;
		return this;
	}
	
	public Matrix setCol(int column, Vector values){
		checkIsColInBounds(column);
		
		if(values.length() != rows)
			throw new IllegalArgumentException("The number of values must be the same as the number of rows in the matrix.\n" + values.length() + " values were given, " + rows + " values were needed.");
		
		for(int r = 0; r < rows; r++){
			set(r, column, values.get(r));
		}
		
		return this;
	}
	
	public Matrix setRow(int row, Vector values){
		checkIsRowInBounds(row);
		
		if(values.length() != columns)
			throw new IllegalArgumentException("The number of values must be the same as the number of columns in the matrix.\n" + values.length() + " values were given, " + columns + " values were needed.");
		
		for(int c = 0; c < columns; c++){
			set(row, c, values.get(c));
		}
		
		return this;
	}
	
	public Matrix getSubmatrix(int row, int column){
		checkIsInBounds(row, column);
		
		Fraction[] values = new Fraction[(rows - 1) * (columns - 1)];
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
		
		return new Matrix(rows - 1, columns -1, values);
	}
	
	public String toMatString(){
		int[] digitsInCol = mostDigitsInCols();
		
		StringBuilder str = new StringBuilder();
		for(int r = 0; r < rows; r++){
			
			str.append("|");
			for(int c = 0; c < columns; c++){
				
				str.append(Util.rightAlignString(matrix[r][c].toString(), digitsInCol[c]));
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
