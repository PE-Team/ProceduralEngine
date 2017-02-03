package pe.engine.main;

import pe.util.console.Console;
import pe.util.math.Mat2f;
import pe.util.math.Vec2f;

public class Main {

	public static void main(String... args){
		Console console = new Console();
		console.start();
		Mat2f percent = new Mat2f(.95f,.05f,.03f,.97f);
		Vec2f population = new Vec2f(800000,600000);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
		population = Mat2f.mul(percent, population);
		console.log(population);
	}
}
