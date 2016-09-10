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
	protected void run (Interpreter ip) {
		// Check for ZERO
		if (ip.variables [variable].compareTo (BigInteger.ZERO) == 0) {
			// Perform jump
			ip.pc = target;
		} else {
			// Continue with next instruction
			ip.pc++;
		}
	}
}
