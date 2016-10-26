package ptg.test.testUnits;

import ptg.test.main.Test;
import ptg.test.main.TestUnit;
import ptg.util.maths.Vec3f;

public class Vec3fTest extends Test{
	
	public Vec3fTest(){
		super(Vec3f.class);
	}
	
	public void testAdd(){
		paramClasses.add(Vec3f.class);
		paramClasses.add(Vec3f.class);
		TestUnit testAdd = new TestUnit(this.testedClass,"add",paramClasses);
		testAdd.setExpectedCalculation("{@p0+@p3,@p1+@p4,@p2+@p5}");
		//testAdd.run();
	}
	
	public void testAdd1(){
		System.out.println(1);
	}
	
	public void testAdd2(){
		System.out.println(2);
	}
}
