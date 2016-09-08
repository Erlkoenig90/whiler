package whiler.whilep;

public class Assign extends Statement {
	protected int destination, source;
	
	public Assign (int destination, int source) {
		this.destination = destination;
		this.source = source;
	}
	public int getMaxVar () {
		return Math.max(destination, source);
	}
	protected void run (Interpreter ip) {
		ip.variables [destination] = ip.variables [source];
	}
}
