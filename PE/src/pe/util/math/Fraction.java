package pe.util.math;

import pe.util.Util;

public class Fraction{

	private int numerator, denominator;
	private boolean autoSimplify;
	
	public Fraction(int numerator, int denominator){
		this.numerator = numerator;
		this.denominator = denominator;
		this.autoSimplify = true;
		simplify();
	}
	
	public Fraction(int numerator, int denominator, boolean autoSimplify){
		this.numerator = numerator;
		this.denominator = denominator;
		this.autoSimplify = autoSimplify;
		if(autoSimplify) simplify();
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
	
	public Fraction divide(Fraction fraction){
		numerator *= fraction.getDenominator();
		denominator *= fraction.getNumerator();
		if(autoSimplify) simplify();
		return this;
	}
	
	public boolean equals(Fraction frac){
		return numerator == frac.getNumerator() && denominator == frac.getDenominator();
	}
	
	public Fraction flip(){
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
		return numerator + "/" + denominator;
	}
}
