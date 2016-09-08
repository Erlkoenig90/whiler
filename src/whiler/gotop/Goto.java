package whiler.gotop;

public class Goto extends Op {
	protected int target;
	public Goto (int target) {
		this.target = target;
	}
	public void toString (StringBuilder sb) {
		sb.append ("GOTO ");
		sb.append (target);
	}
	public void setTarget (int target) {
		this.target = target;
	}
}
