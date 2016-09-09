package whiler.test;

public class TestComp extends TestBase {
	public TestComp () {
		super ("comp");
	}
	
	void progtests () throws Exception {
		prog (1, 3, 7);
		prog (0, 7, 7);
		prog (0, 100, 7);
	}
}
