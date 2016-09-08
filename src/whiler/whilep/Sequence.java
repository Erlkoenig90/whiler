package whiler.whilep;

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
	protected void run (Interpreter ip) {
		for (int i = 0; i < statements.length; i++) {
			statements [i].run (ip);
		}
	}
}
