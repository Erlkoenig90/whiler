package whiler.gotop;

import java.math.BigInteger;

/**
 * Represents an assignment operation of a GOTO program in the form Xi = Xj + n
 * where n is any integer.
 */
public class Assign extends Op {
	/**
	 * Variable index.
	 */
	protected int destination, source;
	/**
	 * Constant offset to add
	 */
	protected BigInteger add;
	
	public Assign (int destination, int source, BigInteger add) {
		this.destination = destination;
		this.source = source;
		this.add = add;
	}
	
	public void toString (StringBuilder sb) {
		sb.append ("X");
		sb.append (destination);
		sb.append (" := X");
		sb.append (source);
		sb.append (" ");
		if (add.compareTo (BigInteger.ZERO) >= 0) {
			sb.append ('+');
		}
		sb.append (add);
	}
	
	public int getMaxVar () {
		return Math.max (destination, source);
	}
	
	protected void compileJava (CompileJava c) {
		// Get destination/source references
		c.mv.visitVarInsn (org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitIntInsn (org.objectweb.asm.Opcodes.SIPUSH, destination);
		c.mv.visitVarInsn (org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitIntInsn (org.objectweb.asm.Opcodes.SIPUSH, source);
		c.mv.visitInsn (org.objectweb.asm.Opcodes.AALOAD);
		// Load constant for offset
		c.mv.visitFieldInsn (org.objectweb.asm.Opcodes.GETSTATIC, c.className, "const_" + c.constants.get (add).toString (), "Ljava/math/BigInteger;");
		// Perform addition
		c.mv.visitMethodInsn (org.objectweb.asm.Opcodes.INVOKEVIRTUAL, "java/math/BigInteger", "add", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false);
		// Store result
		c.mv.visitInsn (org.objectweb.asm.Opcodes.AASTORE);
	}
}
