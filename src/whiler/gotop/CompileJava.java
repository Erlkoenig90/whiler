package whiler.gotop;

import org.objectweb.asm.*;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class CompileJava implements Opcodes {
	Program prog;
	MethodVisitor mv;
	Label [] labels;
	String className, classNameOrig;
	
	Map<BigInteger, Integer> constants;
	
	private CompileJava (String className, Program prog) {
		this.prog = prog;
		this.className = className.replace('.', '/');
		classNameOrig = className;
		constants = new TreeMap<BigInteger, Integer> ();
		
		for (int i = 0; i < prog.op.length; i++) {
			if (prog.op [i] instanceof Assign) {
				Assign a = (Assign) prog.op [i];
				if (!constants.containsKey (a.add)) {
					constants.put (a.add, constants.size ());
				}
			}
		}
	}
	private JavaProg doRun () throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		FieldVisitor fv;

		cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);

		{
			for (Map.Entry<BigInteger, Integer> entry : constants.entrySet ()) {
				fv = cw.visitField(ACC_FINAL + ACC_STATIC, "const_" + entry.getValue ().toString (), "Ljava/math/BigInteger;", null, null);
				fv.visitEnd();
			}
		}

		{
			mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
			mv.visitCode();
			
			for (Map.Entry<BigInteger, Integer> entry : constants.entrySet ()) {
				byte [] binary = entry.getKey ().toByteArray ();
					
				mv.visitTypeInsn(NEW, "java/math/BigInteger");
				mv.visitInsn(DUP);
				mv.visitIntInsn(SIPUSH, (short) binary.length);
				mv.visitIntInsn(NEWARRAY, T_BYTE);
				
				for (int j = 0; j < binary.length; j++) {
					mv.visitInsn(DUP);
					mv.visitInsn(ICONST_0);
					mv.visitIntInsn(BIPUSH, binary [j]);
					mv.visitInsn(BASTORE);
				}
				
				mv.visitMethodInsn(INVOKESPECIAL, "java/math/BigInteger", "<init>", "([B)V", false);
				mv.visitFieldInsn(PUTSTATIC, className, "const_" + entry.getValue ().toString (), "Ljava/math/BigInteger;");
			}
			
			mv.visitInsn(RETURN);
			
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		
		{
			/*
			 * Local Variables
			 * 0 => Input
			 * 1 => Variables
			 */
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "run", "([Ljava/math/BigInteger;)Ljava/math/BigInteger;", null,
					null);
			mv.visitCode();
			mv.visitIntInsn(BIPUSH, prog.numVars);
			mv.visitTypeInsn(ANEWARRAY, "java/math/BigInteger");
			mv.visitVarInsn(ASTORE, 1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(GETSTATIC, "java/math/BigInteger", "ZERO", "Ljava/math/BigInteger;");
			mv.visitInsn(AASTORE);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(ISUB);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "min", "(II)I", false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "arraycopy",
					"(Ljava/lang/Object;ILjava/lang/Object;II)V", false);
			
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitVarInsn(ISTORE, 2);
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {"[Ljava/math/BigInteger;", Opcodes.INTEGER}, 0, null);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ARRAYLENGTH);
			Label l1 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitFieldInsn(GETSTATIC, "java/math/BigInteger", "ZERO", "Ljava/math/BigInteger;");
			mv.visitInsn(AASTORE);
			mv.visitIincInsn(2, 1);
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_CHOP,1, null, 0, null);

			
			labels = new Label [prog.op.length];
			for (int i = 0; i < prog.op.length; i++) {
				labels [i] = new Label ();
			}
			for (int i = 0; i < prog.op.length; i++) {
				mv.visitLabel (labels [i]);
				prog.op [i].compileJava (this);
			}
			
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitTypeInsn(ANEWARRAY, "java/math/BigInteger");
			mv.visitVarInsn(ASTORE, 1);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 2);
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {"[Ljava/math/BigInteger;", Opcodes.INTEGER}, 0, null);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARRAYLENGTH);
			Label l1 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitTypeInsn(NEW, "java/math/BigInteger");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESPECIAL, "java/math/BigInteger", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitInsn(AASTORE);
			mv.visitIincInsn(2, 1);
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_CHOP,1, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, className, "run", "([Ljava/math/BigInteger;)Ljava/math/BigInteger;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(6, 3);
			mv.visitEnd();
		}
		cw.visitEnd();

		return new JavaProg (cw.toByteArray (), className, classNameOrig);
	}
	public static JavaProg run (String className, Program prog) throws Exception {
		return new CompileJava (className, prog).doRun ();
	}
}
