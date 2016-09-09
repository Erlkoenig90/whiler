package whiler.test;

public class TestAdd extends TestBase {
	public TestAdd () {
		super ("add");
	}
	
	void progtests () throws Exception {
		prog (10, 3, 7);
		prog (223, 100, 123);
	}
}
