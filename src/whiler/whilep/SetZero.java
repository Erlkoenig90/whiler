package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Goto;
import whiler.gotop.If;
import whiler.gotop.Op;

/**
 * Represents a statement that sets one variable to zero in a while program
 */
public class SetZero extends Statement {
	/**
	 * The variable to set
	 */
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
	protected void compileGoto(List<Op> op, CompileGoto c) {
		// Loop the decrement until we reach zero
		int start = op.size ();
		whiler.gotop.If gtEnd = new If(0, variable);
		op.add (gtEnd);
		
		op.add (new whiler.gotop.Assign (variable, variable, BigInteger.ZERO.subtract(BigInteger.ONE)));
		op.add (new Goto (start));
		
		gtEnd.setTarget (op.size ());
	}
}
