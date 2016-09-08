package whiler.gotop;

public class If extends Op {
	protected int target, variable;
	public If (int target, int variable) {
		this.target = target;
		this.variable = variable;
	}
	public void toString (StringBuilder sb) {
		sb.append ("IF X");
		sb.append (variable);
		sb.append (" = 0 THEN GOTO ");
		sb.append (target);
	}
	public void setTarget (int target) {
		this.target = target;
	}
}
