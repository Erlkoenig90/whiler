package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Assign;
import whiler.gotop.Op;

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
	protected void compileGoto(List<Op> op, CompileGoto c) {
		op.add (new Assign(variable, variable, BigInteger.ONE));
	}
}
