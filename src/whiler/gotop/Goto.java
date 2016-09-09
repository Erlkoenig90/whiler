package whiler.gotop;

/**
 * The GOTO Opcode in a GOTO program
 */
public class Goto extends Op {
	/**
	 * The target opcode.
	 */
	protected int target;
	
	public Goto (int target) {
		this.target = target;
	}
	
	public void toString (StringBuilder sb) {
		sb.append ("GOTO ");
		sb.append (target);
	}
	
	/**
	 * In case the target instruction can only be calculated after creation of this opcode, it can be modified later
	 * 
	 * @param target
	 *            The jump target
	 */
	public void setTarget (int target) {
		this.target = target;
	}
	
	public int getMaxVar () {
		return -1;
	}
	
	protected void compileJava (CompileJava c) {
		// Jump to label of target instruction
		c.mv.visitJumpInsn (org.objectweb.asm.Opcodes.GOTO, c.labels [target]);
	}
}
