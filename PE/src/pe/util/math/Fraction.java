package pe.util.math;

import pe.util.Util;

public class Fraction{

	private int numerator, denominator;
	private boolean autoSimplify;
	
	public static Fraction ZERO = new Fraction(0, 1);
	
	public Fraction(int constant){
		this.numerator = constant;
		this.denominator = 1;
		this.autoSimplify = true;
	}
	
	public Fraction(int numerator, int denominator){
		if(denominator == 0) throw new IllegalArgumentException("The denominator for a fraction cannot be zero.");
		this.numerator = numerator;
		this.denominator = denominator;
		this.autoSimplify = true;
		simplify();
	}
	
	public Fraction(int numerator, int denominator, boolean autoSimplify){
		if(denominator == 0) throw new IllegalArgumentException("The denominator for a fraction cannot be zero.");
		this.numerator = numerator;
		this.denominator = denominator;
		this.autoSimplify = autoSimplify;
		if(autoSimplify) simplify();
	}
	
	public Fraction add(int numb){
		numerator += numb * denominator;
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction add(Fraction fraction){
		int LCM = Maths.LCM(denominator, fraction.getDenominator());
		numerator = LCM / denominator * numerator + LCM / fraction.getDenominator() * fraction.getNumerator();
		denominator = LCM;
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction copy(){
		return new Fraction(numerator, denominator, autoSimplify);
	}
	
	public Fraction divide(int numb){
		if(numb == 0) throw new IllegalArgumentException("Cannot divide by zero.");
		denominator *= numb;
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction divide(Fraction fraction){
		if(fraction.getNumerator() == 0) throw new IllegalArgumentException("Cannot divide by a fraction who's numerator is zero.");
		numerator *= fraction.getDenominator();
		denominator *= fraction.getNumerator();
		if(autoSimplify) simplify();
		return this;
	}
	
	public boolean equals(Fraction frac){
		return numerator * frac.getDenominator() == denominator * frac.getNumerator();
	}
	
	public Fraction flip(){
		if(numerator == 0) throw new IllegalArgumentException("Cannot flip a fraction who's numerator is zero.");
		int tempNum = numerator;
		numerator = denominator;
		denominator = tempNum;
		return this;
	}
	
	public int getDenominator(){
		return denominator;
	}
	
	public int getNumerator(){
		return numerator;
	}
	
	public boolean isAutoSimplified(){
		return autoSimplify;
	}
	
	public boolean isSimplified(){
		return autoSimplify || copy().simplify().equals(this);
	}
	
	public Fraction mul(int numb){
		numerator *= numb;
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction mul(Fraction fraction){
		numerator *= fraction.getNumerator();
		denominator *= fraction.getDenominator();
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction pow(int exponent){
		if(exponent < 0){
			flip();
			exponent *= -1;
		}
		
		int numCopy = numerator;
		int denomCopy = denominator;
		for(int i = 0; i < exponent - 1; i++){
			numerator *= numCopy;
			denominator *= denomCopy;
		}
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction setAutoSimplify(boolean autoSimplify){
		this.autoSimplify = autoSimplify;
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction simplify(){
		int GCF = Maths.GCF(numerator, denominator);
		numerator /= GCF;
		denominator /= GCF;
		if(denominator < 0){
			numerator *= -1;
			denominator *= -1;
		}
		return this;
	}
	
	public Fraction subtract(int numb){
		numerator -= numb * denominator;
		if(autoSimplify) simplify();
		return this;
	}
	
	public Fraction subtract(Fraction fraction){
		int LCM = Maths.LCM(denominator, fraction.getDenominator());
		numerator = LCM / denominator * numerator - LCM / fraction.getDenominator() * fraction.getNumerator();
		denominator = LCM;
		if(autoSimplify) simplify();
		return this;
	}
	
	public double toDec(){
		return ((double) numerator) / denominator;
	}
	
	public float toDecf(){
		return ((float) numerator) / denominator;
	}
	
	public String toFracString(){
		if(numerator == 0) return "0";
		if(denominator == 1) return Integer.toString(numerator);
		
		int numStrLen = Integer.toString(numerator).length();
		int denomStrLen = Integer.toString(denominator).length();
		int strLen = numStrLen > denomStrLen ? numStrLen : denomStrLen;
		StringBuilder fracString = new StringBuilder();
		fracString.append(Util.centerAlignString(Integer.toString(numerator), strLen));
		fracString.append('\n');
		fracString.append(Util.repeatCharFor('-', strLen));
		fracString.append('\n');
		fracString.append(Util.centerAlignString(Integer.toString(denominator), strLen));
		return fracString.toString();
	}
	
	public String toString(){
		if(numerator == 0) return "0";
		if(denominator == 1) return Integer.toString(numerator);
		return numerator + "/" + denominator;
	}
}
