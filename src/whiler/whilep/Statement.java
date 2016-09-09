package whiler.whilep;
import java.util.List;

import whiler.gotop.Op;

/**
 * Represents a statement in the while program. A statement can be an assignment or a control structure, which in turn can contain more statements. Multiple
 * statements are soted in a {@link Sequence}
 */
public abstract class Statement {
	/**
	 * @return		The highest index of any variable used in this statement.
	 */
	public abstract int getMaxVar ();
	/**
	 * Recursively run the interpreter on this statement
	 */
	protected abstract void run (Interpreter ip);
	/**
	 * Recursively compile this statement into a GOTO program
	 * @param op	Collection of GOTO instructions
	 * @param c		Compiler instance
	 */
	protected abstract void compileGoto (List<Op> op, CompileGoto c);
}
