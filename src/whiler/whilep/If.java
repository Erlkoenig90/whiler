package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Goto;
import whiler.gotop.Op;

/**
 * Represents an If-Else construct in a while program.
 */
public class If extends Statement {
	/**
	 * Index of the variable that must be lesser in order for the condition to succeed
	 */
	protected int varL;
	/**
	 * Index of the variable that must be greater in order for the condition to succeed
	 */
	protected int varG;
	/**
	 * Sequence of statements to execute if the condition matches
	 */
	protected Sequence If;
	/**
	 * Sequence of statements to execute if the condition does not match
	 */
	protected Sequence Else;
	public If (int varL, int varG, Sequence If, Sequence Else) {
		this.varL = varL;
		this.varG = varG;
		this.If = If;
		this.Else = Else;
	}
	public int getMaxVar () {
		return Math.max (If.getMaxVar(), Math.max (Else.getMaxVar(), Math.max (varL, varG)));
	}
	protected void run (Interpreter ip) {
		// Compare variable data
		if (ip.variables [varL].compareTo (ip.variables [varG]) == -1) {
			If.run (ip);
		} else {
			Else.run (ip);
		}
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		// Obtain temporary variables
		int t1 = c.tempVar; c.tempVar++;
		int t2 = c.tempVar; c.tempVar++;
		
		// Remember beginning
		int start = op.size ();
		BigInteger mONE = BigInteger.ZERO.subtract (BigInteger.ONE);
		
		// Copmarison loop
		op.add (new whiler.gotop.Assign (t1, varL, BigInteger.ZERO));
		op.add (new whiler.gotop.Assign (t2, varG, BigInteger.ZERO));
		
		whiler.gotop.If gtElse = new whiler.gotop.If (0, t2);
		op.add (gtElse);
		
		op.add (new whiler.gotop.If (start + 7, t1));
		
		op.add (new whiler.gotop.Assign (t1, t1, mONE));
		op.add (new whiler.gotop.Assign (t2, t2, mONE));
		op.add (new Goto (start + 2));
		
		If.compileGoto (op, c);
		
		Goto gtEnd = new Goto (0);
		op.add (gtEnd);
		gtElse.setTarget (op.size ());
		
		Else.compileGoto (op, c);
		
		gtEnd.setTarget (op.size ());
		
		c.tempVar -= 2;
	}
}
