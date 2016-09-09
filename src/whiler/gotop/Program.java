package whiler.gotop;

public class Program {
	protected Op [] op;
	protected int numVars;
	public Program (Op [] op) {
		this.op = op;
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
			sb.append(String.format("%03d:  ", i));
			op [i].toString (sb);
		}
		return sb.toString ();
	}
}
