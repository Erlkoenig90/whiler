package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Assign;
import whiler.gotop.Op;

/**
 * Represents an increment operation in a while program.
 */
public class Increment extends Statement {
	/**
	 * Index of the variable to increment
	 */
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
	protected void compileGoto(List<Op> op, CompileGoto c) {
		op.add (new Assign(variable, variable, BigInteger.ONE));
	}
}
