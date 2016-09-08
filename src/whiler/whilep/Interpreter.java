package whiler.whilep;

import java.math.BigInteger;

public class Interpreter {
	Program prog;
	protected BigInteger [] variables;
	public Interpreter (Program prog) {
		this.prog = prog;
	}
	public BigInteger run (BigInteger [] input) {
		variables = new BigInteger [prog.numVars];
		variables [0] = BigInteger.ZERO;
		System.arraycopy (input, 0, variables, 1, input.length);
		for (int i = input.length + 1; i < prog.numVars; i++) {
			variables [i] = BigInteger.ZERO;
		}
		
		prog.root.run (this);
		
		return variables [0];
	}
}
