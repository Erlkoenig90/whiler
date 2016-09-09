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
 * Represents a GOTO program.
 */
public class Program {
	/**
	 * Since GOTO programs are unstructured, a simple array of opcodes is sufficient to represent them.
	 */
	protected Op [] op;
	/**
	 * Number of variables used by the program
	 */
	protected int numVars;
	public Program (Op [] op) {
		this.op = op;
		// Calculate number of variables used, by taking the highest index and adding one
		numVars = 0;
		for (int i = 0; i < op.length; i++)
			numVars = Math.max(numVars, op [i].getMaxVar());
		++numVars;
	}
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < op.length; i++) {
			if (i > 0)
				sb.append('\n');
			// Print line number
			sb.append(String.format("%03d:  ", i));
			op [i].toString (sb);
		}
		return sb.toString ();
	}
}
