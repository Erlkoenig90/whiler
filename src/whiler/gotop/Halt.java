package whiler.gotop;

public class Halt extends Op {
	public Halt() {
	}

	public void toString(StringBuilder sb) {
		sb.append("HALT");
	}

	public int getMaxVar() {
		return -1;
	}

	protected void compileJava(CompileJava c) {
		c.mv.visitVarInsn (org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.ICONST_0);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.AALOAD);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.ARETURN);
	}
}
