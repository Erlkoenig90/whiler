package whiler.gotop;

/**
 * The IF opcode performs a conditional jump, if the given variable is zero.
 */
public class If extends Op {
	/**
	 * The target opcode
	 */
	protected int target;
	/**
	 * Index of the variable to check
	 */
	protected int variable;
	
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
		return variable;
	}
	
	protected void compileJava (CompileJava c) {
		// Load variable from array
		c.mv.visitVarInsn (org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitIntInsn (org.objectweb.asm.Opcodes.SIPUSH, variable);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.AALOAD);
		// Load ZERO constant
		c.mv.visitFieldInsn (org.objectweb.asm.Opcodes.GETSTATIC, "java/math/BigInteger", "ZERO", "Ljava/math/BigInteger;");
		// call compareTo method
		c.mv.visitMethodInsn (org.objectweb.asm.Opcodes.INVOKEVIRTUAL, "java/math/BigInteger", "compareTo", "(Ljava/math/BigInteger;)I", false);
		// If return value was zero, jump to target
		c.mv.visitJumpInsn (org.objectweb.asm.Opcodes.IFEQ, c.labels [target]);
	}
}
