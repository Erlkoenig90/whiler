package whiler.gotop;

public class Program {
	Op [] op;
	public Program (Op [] op) {
		this.op = op;
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
