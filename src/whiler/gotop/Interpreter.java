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
import whiler.ProgRunner;

public class Interpreter implements ProgRunner {
	/**
	 * The program to run
	 */
	Program prog;
	/**
	 * Memory for variables
	 */
	protected BigInteger [] variables;
	/**
	 * The Program Counter, i.e. the index of the next instruction to run.
	 */
	protected int pc;
	/**
	 * Whether to continue interpreting the program. Is set to false by "HALT"
	 */
	protected boolean run;
	
	public Interpreter (Program prog) {
		this.prog = prog;
	}

	public BigInteger run (BigInteger [] input) {
		// Initialize variables
		variables = new BigInteger [prog.numVars];
		variables [0] = BigInteger.ZERO;
		System.arraycopy (input, 0, variables, 1, Math.min (input.length, variables.length - 1));
		for (int i = input.length + 1; i < prog.numVars; i++) {
			variables [i] = BigInteger.ZERO;
		}
		
		// Start with first instruction
		run = true;
		pc = 0;
		
		// Run until HALT
		while (run) {
			prog.op [pc].run (this);
		}
		
		return variables [0];
	}
	
}
