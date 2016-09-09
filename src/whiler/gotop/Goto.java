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
	public int getMaxVar() {
		return -1;
	}
	protected void compileJava(CompileJava c) {
		c.mv.visitJumpInsn (org.objectweb.asm.Opcodes.GOTO, c.labels [target]);
	}
}
