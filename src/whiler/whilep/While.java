package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Goto;
import whiler.gotop.Op;

public class While extends Statement {
	protected int varL, varG;
	protected Sequence body;
	public While (int varL, int varG, Sequence body) {
		this.varL = varL;
		this.varG = varG;
		this.body = body;
	}
	public int getMaxVar () {
		return Math.max (body.getMaxVar(), Math.max (varL, varG));
	}
	protected void run (Interpreter ip) {
		while (ip.variables [varL].compareTo (ip.variables [varG]) == -1) {
			body.run (ip);
		}
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		int t1 = c.tempVar; c.tempVar++;
		int t2 = c.tempVar; c.tempVar++;
		int start = op.size ();
		BigInteger mONE = BigInteger.ZERO.subtract (BigInteger.ONE);
		
		op.add (new whiler.gotop.Assign (t1, varL, BigInteger.ZERO));
		op.add (new whiler.gotop.Assign (t2, varG, BigInteger.ZERO));
		
		whiler.gotop.If gtEnd = new whiler.gotop.If (0, t2);
		op.add (gtEnd);
		
		op.add (new whiler.gotop.If (start + 7, t1));
		
		op.add (new whiler.gotop.Assign (t1, t1, mONE));
		op.add (new whiler.gotop.Assign (t2, t2, mONE));
		op.add (new Goto (start + 2));
		
		body.compileGoto (op, c);
		
		op.add (new Goto (start));
		
		gtEnd.setTarget (op.size ());
		
		c.tempVar -= 2;
	}
}
