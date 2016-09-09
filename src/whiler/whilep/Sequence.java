package whiler.whilep;

import java.util.List;

import whiler.gotop.Op;

/**
 * Represents a sequence of statements in a while program (separated by semicolons in the source code)
 */
public class Sequence {
	protected Statement [] statements;
	public Sequence (Statement [] statements) {
		this.statements = statements;
	}
	public int getMaxVar () {
		int res = -1;
		for (int i = 0; i < statements.length; i++)
			res = Math.max(res, statements [i].getMaxVar());
		return res;
	}
	/**
	 * Run all statements in the interpreter
	 */
	protected void run (Interpreter ip) {
		for (int i = 0; i < statements.length; i++) {
			statements [i].run (ip);
		}
	}
	/**
	 * Compile all statements into a GOTO program.
	 */
	void compileGoto (List<Op> op, CompileGoto c) {
		for (int i = 0; i < statements.length; i++) {
			statements [i].compileGoto (op, c);
		}
	}
}
