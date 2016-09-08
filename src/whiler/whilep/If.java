package whiler.whilep;

public class If extends Statement {
	protected int varL, varG;
	protected Sequence If, Else;
	public If (int varL, int varG, Sequence If, Sequence Else) {
		this.varL = varL;
		this.varG = varG;
		this.If = If;
		this.Else = Else;
	}
	public int getMaxVar () {
		return Math.max (If.getMaxVar(), Math.max (Else.getMaxVar(), Math.max (varL, varG)));
	}
	protected void run (Interpreter ip) {
		if (ip.variables [varL].compareTo (ip.variables [varG]) == -1) {
			If.run (ip);
		} else {
			Else.run (ip);
		}
	}
}
