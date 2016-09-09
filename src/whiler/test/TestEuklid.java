package whiler.test;

import java.math.BigInteger;
import static org.junit.Assert.*;

public class TestEuklid extends TestBase {
	public TestEuklid () {
		super ("euklid");
	}
	
	void testGCD (int a, int b) throws Exception {
		BigInteger ba = BigInteger.valueOf (a), bb = BigInteger.valueOf (b);
		assertEquals (ba.gcd (bb), currentRunner.run (new BigInteger [] { ba, bb }));
	}
	
	void progtests () throws Exception {
		testGCD (15, 21);
		testGCD (28, 32);
		testGCD (52, 24);
		testGCD (408, 672);
	}
}
