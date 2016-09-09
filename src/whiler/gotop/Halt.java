package whiler.gotop;

/**
 * The HALT opcode terminates the program immediately.
 */
public class Halt extends Op {
	public Halt () {
	}
	
	public void toString (StringBuilder sb) {
		sb.append ("HALT");
	}
	
	public int getMaxVar () {
		return -1;
	}
	
	protected void compileJava (CompileJava c) {
		// Load entry 0 from variables array
		c.mv.visitVarInsn (org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.ICONST_0);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.AALOAD);
		// And return it
		c.mv.visitInsn (org.objectweb.asm.Opcodes.ARETURN);
	}
}
