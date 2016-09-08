package whiler.whilep;

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
}
