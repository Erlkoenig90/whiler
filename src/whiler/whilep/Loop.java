package whiler.whilep;

import java.math.BigInteger;

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
}
