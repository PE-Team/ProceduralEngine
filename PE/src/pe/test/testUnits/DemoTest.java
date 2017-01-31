package pe.test.testUnits;

import java.util.ArrayList;
import java.util.List;

import pe.test.main.DemoClass;
import pe.test.main.Test;
import pe.test.main.TestUnit;
import pe.util.Util;
import pe.util.math.Vec3f;

public class DemoTest extends Test{

	public DemoTest() {
		super(DemoClass.class);
	}
	
	public void testReturnInputFloat(){
		paramClasses.add(float.class);
		TestUnit testReturnInputFloat = new TestUnit(this.testedClass,"returnInputFloat",paramClasses);
		testReturnInputFloat.setExpectedCalculation("@p0");
		testReturnInputFloat.run();
	}
	
	public void testAddTwoFloats(){
		paramClasses.add(float.class);
		paramClasses.add(float.class);
		TestUnit testAddTwoFloats = new TestUnit(this.testedClass,"addTwoFloats",paramClasses);
		testAddTwoFloats.setExpectedCalculation("@p0+@p1");
		testAddTwoFloats.run();
	}
	
	public void testAddVec3fs(){
		// Add all of the parameter classes
		paramClasses.add(Vec3f.class);
		paramClasses.add(Vec3f.class);
		
		// Add all of the classes which are needed for a specific constructor
		List<Class<?>> initClasses = new ArrayList<Class<?>>();
			initClasses.add(float.class);
			initClasses.add(float.class);
			initClasses.add(float.class);
		paramConstructors.add(Util.getConstructor(Vec3f.class, initClasses));
		
		// Initialize the test and execute the test
		TestUnit testAddVec3fs = new TestUnit(Vec3f.class, "add", paramClasses, paramConstructors);
		testAddVec3fs.setExpectedCalculation("{@p0+@p3,@p1+@p4,@p2+@p5}");
		testAddVec3fs.run();
	}

}
