package whiler.test;
import org.junit.Test;

public class TestMul extends TestBase {
	public TestMul () {
		super ("mult");
	}
	
	@Test
	public void test1 () {
		interpret (30, 5, 6);
		interpret (56088, 123, 456);
	}
	
	@Test
	public void test2 () throws Exception {
		runJava (30, 5, 6);
		runJava (56088, 123, 456);
	}
}
