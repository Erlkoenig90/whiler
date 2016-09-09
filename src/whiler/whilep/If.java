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
 * Represents an If-Else construct in a while program.
 */
public class If extends Statement {
	/**
	 * Index of the variable that must be lesser in order for the condition to succeed
	 */
	protected int varL;
	/**
	 * Index of the variable that must be greater in order for the condition to succeed
	 */
	protected int varG;
	/**
	 * Sequence of statements to execute if the condition matches
	 */
	protected Sequence If;
	/**
	 * Sequence of statements to execute if the condition does not match
	 */
	protected Sequence Else;
	public If (int varL, int varG, Sequence If, Sequence Else) {
		this.varL = varL;
		this.varG = varG;
		this.If = If;
		this.Else = Else;
	}
	public int getMaxVar () {
		return Math.max (If.getMaxVar(), Math.max (Else.getMaxVar(), Math.max (varL, varG)));
	}
	protected void run (Interpreter ip) {
		// Compare variable data
		if (ip.variables [varL].compareTo (ip.variables [varG]) == -1) {
			If.run (ip);
		} else {
			Else.run (ip);
		}
	}
	protected void compileGoto(List<Op> op, CompileGoto c) {
		// Obtain temporary variables
		int t1 = c.tempVar; c.tempVar++;
		int t2 = c.tempVar; c.tempVar++;
		
		// Remember beginning
		int start = op.size ();
		BigInteger mONE = BigInteger.ZERO.subtract (BigInteger.ONE);
		
		// Copmarison loop
		op.add (new whiler.gotop.Assign (t1, varL, BigInteger.ZERO));
		op.add (new whiler.gotop.Assign (t2, varG, BigInteger.ZERO));
		
		whiler.gotop.If gtElse = new whiler.gotop.If (0, t2);
		op.add (gtElse);
		
		op.add (new whiler.gotop.If (start + 7, t1));
		
		op.add (new whiler.gotop.Assign (t1, t1, mONE));
		op.add (new whiler.gotop.Assign (t2, t2, mONE));
		op.add (new Goto (start + 2));
		
		If.compileGoto (op, c);
		
		Goto gtEnd = new Goto (0);
		op.add (gtEnd);
		gtElse.setTarget (op.size ());
		
		Else.compileGoto (op, c);
		
		gtEnd.setTarget (op.size ());
		
		c.tempVar -= 2;
	}
}
