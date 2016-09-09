package whiler.gotop;

import java.math.BigInteger;

public class Assign extends Op {
	protected int destination, source;
	protected BigInteger add;
	
	public Assign (int destination, int source, BigInteger add) {
		this.destination = destination;
		this.source = source;
		this.add = add;
	}
	public void toString (StringBuilder sb) {
		sb.append("X");
		sb.append(destination);
		sb.append(" := X");
		sb.append(source);
		sb.append(" ");
		if (add.compareTo(BigInteger.ZERO) >= 0) {
			sb.append ('+');
		}
		sb.append(add);
	}
	public int getMaxVar() {
		return Math.max (destination, source);
	}
	protected void compileJava(CompileJava c) {
		c.mv.visitVarInsn(org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitIntInsn(org.objectweb.asm.Opcodes.SIPUSH, destination);
		c.mv.visitVarInsn(org.objectweb.asm.Opcodes.ALOAD, 1);
		c.mv.visitIntInsn(org.objectweb.asm.Opcodes.SIPUSH, source);
		c.mv.visitInsn(org.objectweb.asm.Opcodes.AALOAD);
		c.mv.visitFieldInsn(org.objectweb.asm.Opcodes.GETSTATIC, c.className, "const_" + c.constants.get (add).toString (), "Ljava/math/BigInteger;");
		c.mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKEVIRTUAL, "java/math/BigInteger", "add", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false);
		c.mv.visitInsn(org.objectweb.asm.Opcodes.AASTORE);		
	}
}
