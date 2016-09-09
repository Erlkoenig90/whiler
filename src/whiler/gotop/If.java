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
	public int getMaxVar() {
		return variable;
	}
	protected void compileJava(CompileJava c) {
		c.mv.visitVarInsn(org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitIntInsn(org.objectweb.asm.Opcodes.SIPUSH, variable);
		c.mv.visitInsn(org.objectweb.asm.Opcodes.AALOAD);
		c.mv.visitFieldInsn(org.objectweb.asm.Opcodes.GETSTATIC, "java/math/BigInteger", "ZERO", "Ljava/math/BigInteger;");
		c.mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKEVIRTUAL, "java/math/BigInteger", "compareTo", "(Ljava/math/BigInteger;)I", false);
		c.mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFEQ, c.labels[target]);
	}
}
