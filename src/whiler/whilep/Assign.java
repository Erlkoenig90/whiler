package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Op;

/**
 * Represents an assignment of variables in a while program.
 */
public class Assign extends Statement {
	/**
	 * Index of the destination variable
	 */
	protected int destination;
	/**
	 * Index of the source variable
	 */
	protected int source;
	
	public Assign (int destination, int source) {
		this.destination = destination;
		this.source = source;
	}
	public int getMaxVar () {
		return Math.max(destination, source);
	}
	protected void run (Interpreter ip) {
		ip.variables [destination] = ip.variables [source];
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		op.add (new whiler.gotop.Assign (destination, source, BigInteger.ZERO));
	}
}
