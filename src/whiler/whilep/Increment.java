package whiler.whilep;

import java.math.BigInteger;

public class Increment extends Statement {
	protected int variable;
	public Increment (int variable) {
		this.variable = variable;
	}
	public int getMaxVar () {
		return variable;
	}
	protected void run (Interpreter ip) {
		ip.variables [variable] = ip.variables [variable].add (BigInteger.ONE);
	}
}
