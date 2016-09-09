package whiler.gotop;

/**
 * Represents a GOTO program.
 */
public class Program {
	/**
	 * Since GOTO programs are unstructured, a simple array of opcodes is sufficient to represent them.
	 */
	protected Op [] op;
	/**
	 * Number of variables used by the program
	 */
	protected int numVars;
	public Program (Op [] op) {
		this.op = op;
		// Calculate number of variables used, by taking the highest index and adding one
		numVars = 0;
		for (int i = 0; i < op.length; i++)
			numVars = Math.max(numVars, op [i].getMaxVar());
		++numVars;
	}
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < op.length; i++) {
			if (i > 0)
				sb.append('\n');
			// Print line number
			sb.append(String.format("%03d:  ", i));
			op [i].toString (sb);
		}
		return sb.toString ();
	}
}
