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
