/*
 * Copyright (c) 2016, Niklas Gürtler
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
