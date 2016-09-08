package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Goto;
import whiler.gotop.Op;

public class Loop extends Statement {
	protected int var;
	protected Sequence body;
	public Loop (int var, Sequence body) {
		this.var = var;
		this.body = body;
	}
	public int getMaxVar () {
		return Math.max (body.getMaxVar(), var);
	}
	protected void run (Interpreter ip) {
		BigInteger count = ip.variables [var];
		while (count.compareTo(BigInteger.ZERO) == 1) {
			body.run (ip);
			count = count.subtract (BigInteger.ONE);
		}
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		int t = c.tempVar; c.tempVar++;
		int start = op.size ();
		op.add (new whiler.gotop.Assign (t, var, BigInteger.ZERO));
		
		whiler.gotop.If gtEnd = new whiler.gotop.If (0, t);
		op.add (gtEnd);
		
		op.add (new whiler.gotop.Assign (t, t, BigInteger.ZERO.subtract(BigInteger.ONE)));
		
		body.compileGoto (op, c);
		op.add (new Goto (start + 1));
		gtEnd.setTarget (op.size ());
		c.tempVar--;
	}
}
