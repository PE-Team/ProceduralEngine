package ptg.test.testUnits;

import ptg.test.main.DemoClass;
import ptg.test.main.Test;
import ptg.test.main.TestUnit;

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
		//testAddTwoFloats.run();
	}

}
