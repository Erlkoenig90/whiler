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

package whiler.whilep;

import java.math.BigInteger;
import java.util.List;

import whiler.gotop.Goto;
import whiler.gotop.Op;

/**
 * Represents a LOOP-Statement in a while program.
 */
public class Loop extends Statement {
	/**
	 * Variable containing the number of iterations
	 */
	protected int var;
	/**
	 * Sequence of statements that make up the loop body
	 */
	protected Sequence body;
	public Loop (int var, Sequence body) {
		this.var = var;
		this.body = body;
	}
	public int getMaxVar () {
		return Math.max (body.getMaxVar(), var);
	}
	protected void run (Interpreter ip) {
		// Make copy of variable
		BigInteger count = ip.variables [var];
		while (count.compareTo(BigInteger.ZERO) == 1) {
			body.run (ip);
			count = count.subtract (BigInteger.ONE);
		}
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		// A temporary variable is needed because the loop body could modify the original one.
		int t = c.tempVar; c.tempVar++;
		int start = op.size ();
		op.add (new whiler.gotop.Assign (t, var, BigInteger.ZERO));
		
		whiler.gotop.If gtEnd = new whiler.gotop.If (0, t);
		op.add (gtEnd);
		
		op.add (new whiler.gotop.Assign (t, t, BigInteger.ZERO.subtract(BigInteger.ONE)));
		
		body.compileGoto (op, c);
		op.add (new Goto (start + 1));
		gtEnd.setTarget (op.size ());
		c.tempVar--;
	}
}
