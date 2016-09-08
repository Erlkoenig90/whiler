package whiler.whilep;

import java.math.BigInteger;

public class SetZero extends Statement {
	protected int variable;
	public SetZero (int variable) {
		this.variable = variable;
	}
	public int getMaxVar () {
		return variable;
	}
	protected void run (Interpreter ip) {
		ip.variables [variable] = BigInteger.ZERO;
	}
}
