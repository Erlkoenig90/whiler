package whiler.whilep;

import java.math.BigInteger;

/**
 * Interprets while programs.
 */
public class Interpreter implements whiler.ProgRunner {
	Program prog;
	/**
	 * Memory for variables
	 */
	protected BigInteger [] variables;
	public Interpreter (Program prog) {
		this.prog = prog;
	}
	public BigInteger run (BigInteger [] input) {
		// Initialize variables
		variables = new BigInteger [prog.numVars];
		variables [0] = BigInteger.ZERO;
		System.arraycopy (input, 0, variables, 1, input.length);
		for (int i = input.length + 1; i < prog.numVars; i++) {
			variables [i] = BigInteger.ZERO;
		}
		
		// Run everything
		prog.root.run (this);
		
		return variables [0];
	}
}
