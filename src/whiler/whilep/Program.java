package whiler.whilep;

public class Program {
	Sequence root;
	protected int numVars;
	public Program (Sequence root) {
		numVars = root.getMaxVar () + 1;
		this.root = root;
	}
}
