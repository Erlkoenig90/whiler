package whiler.whilep;

/**
 * Represents a while program
 */
public class Program {
	/**
	 * The top-level statements
	 */
	Sequence root;
	/**
	 * Number of variables used by the program
	 */
	protected int numVars;
	public Program (Sequence root) {
		numVars = root.getMaxVar () + 1;
		this.root = root;
	}
}
