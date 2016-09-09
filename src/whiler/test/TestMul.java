package whiler.test;

public class TestMul extends TestBase {
	public TestMul () {
		super ("mult");
	}
	
	public void progtests () throws Exception {
		prog (30, 5, 6);
		prog (56088, 123, 456);
	}
}
