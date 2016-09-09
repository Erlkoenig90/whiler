package whiler.gotop;

public abstract class Op {
	/**
	 * Build a String representation of the operation
	 * @param sb	The StringBuilder to which the text should be added
	 */
	abstract public void toString (StringBuilder sb);
	/**
	 * @return		The highest index of any variable used in this opcode.
	 */
	public abstract int getMaxVar ();
	/**
	 * Compile the opcode into Java Bytecode.
	 * @param c		The Compiler instance
	 */
	protected abstract void compileJava (CompileJava c);
}
