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

import java.util.List;

import whiler.gotop.Op;

/**
 * Represents a sequence of statements in a while program (separated by semicolons in the source code)
 */
public class Sequence {
	protected Statement [] statements;
	public Sequence (Statement [] statements) {
		this.statements = statements;
	}
	public int getMaxVar () {
		int res = -1;
		for (int i = 0; i < statements.length; i++)
			res = Math.max(res, statements [i].getMaxVar());
		return res;
	}
	/**
	 * Run all statements in the interpreter
	 */
	protected void run (Interpreter ip) {
		for (int i = 0; i < statements.length; i++) {
			statements [i].run (ip);
		}
	}
	/**
	 * Compile all statements into a GOTO program.
	 */
	void compileGoto (List<Op> op, CompileGoto c) {
		for (int i = 0; i < statements.length; i++) {
			statements [i].compileGoto (op, c);
		}
	}
}
