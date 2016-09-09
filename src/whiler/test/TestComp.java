package whiler.test;
import org.junit.Test;

public class TestComp extends TestBase {
	public TestComp () {
		super ("comp");
	}
	
	@Test
	public void test1 () {
		interpret (1, 3, 7);
		interpret (0, 7, 7);
		interpret (0, 100, 7);
	}
	
	@Test
	public void test2 () throws Exception {
		runJava (1, 3, 7);
		runJava (0, 7, 7);
		runJava (0, 100, 7);
	}
}
