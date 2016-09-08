package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Goto;
import whiler.gotop.Op;

public class If extends Statement {
	protected int varL, varG;
	protected Sequence If, Else;
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
		if (ip.variables [varL].compareTo (ip.variables [varG]) == -1) {
			If.run (ip);
		} else {
			Else.run (ip);
		}
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		int t1 = c.tempVar; c.tempVar++;
		int t2 = c.tempVar; c.tempVar++;
		
		int start = op.size ();
		BigInteger mONE = BigInteger.ZERO.subtract (BigInteger.ONE);
		
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
