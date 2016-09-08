package whiler.whilep;

import java.math.BigInteger;

public class Interpreter {
	protected int numVars;
	protected BigInteger [] variables;
	protected Sequence root;
	public Interpreter (Sequence root) {
		numVars = root.getMaxVar () + 1;
		this.root = root;
	}
	public BigInteger run (BigInteger [] input) {
		variables = new BigInteger [numVars];
		variables [0] = BigInteger.ZERO;
		System.arraycopy (input, 0, variables, 1, input.length);
		for (int i = input.length + 1; i < numVars; i++) {
			variables [i] = BigInteger.ZERO;
		}
		
		root.run (this);
		
		return variables [0];
	}
}
