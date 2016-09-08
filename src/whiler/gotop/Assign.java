package whiler.gotop;

import java.math.BigInteger;

public class Assign extends Op {
	protected int destination, source;
	protected BigInteger add;
	
	public Assign (int destination, int source, BigInteger add) {
		this.destination = destination;
		this.source = source;
		this.add = add;
	}
	public void toString (StringBuilder sb) {
		sb.append("X");
		sb.append(destination);
		sb.append(" := X");
		sb.append(source);
		sb.append(" ");
		if (add.compareTo(BigInteger.ZERO) >= 0) {
			sb.append ('+');
		}
		sb.append(add);
	}
}
