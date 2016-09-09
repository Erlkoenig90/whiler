package whiler;

import java.math.BigInteger;

/**
 * Common interface for interpreters/Java program runner.
 */
public interface ProgRunner {
	/**
	 * Runs the program.
	 * 
	 * @param input			The input variables. Does not need to contain variables for internal data
	 * @return				The program result
	 */
	public BigInteger run (BigInteger [] input) throws Exception;
}
