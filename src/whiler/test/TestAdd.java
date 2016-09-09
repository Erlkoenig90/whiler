package whiler.test;
import org.junit.Test;

public class TestAdd extends TestBase {
	public TestAdd () {
		super ("add");
	}
	
	@Test
	public void test1 () {
		interpret (10, 3, 7);
		interpret (223, 100, 123);
	}
	
	@Test
	public void test2 () throws Exception {
		runJava (10, 3, 7);
		runJava (223, 100, 123);
	}
}
