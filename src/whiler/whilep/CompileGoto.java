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

import java.util.ArrayList;
import java.util.List;

import whiler.gotop.Halt;
import whiler.gotop.Op;

/**
 * Compiles while programs into GOTO programs.
 */
public class CompileGoto {
	/**
	 * Contains the index of the first unused variable in the program. Is in/de-creased by the individual statement compilation functions in order to
	 * obtain temporary variables for control structures.
	 */
	protected int tempVar;
	/**
	 * The program to compile
	 */
	Program prog;
	
	private CompileGoto (Program prog) {
		this.prog = prog;
	}
	
	private whiler.gotop.Program doRun () {
		// Use the first unused variable as the first temporary one
		tempVar = prog.numVars;
		
		List<Op> op = new ArrayList<Op> ();
		
		// Compile everything
		prog.root.compileGoto (op, this);
		
		// Append HALT instruction
		op.add (new Halt ());
		
		// Make GOTO program instance
		return new whiler.gotop.Program (op.toArray (new Op [0]));
	}
	
	/**
	 * Performs the compilation of while programs
	 * @param prog	The while program to compile
	 * @return		The compiled GOTO program
	 */
	public static whiler.gotop.Program run (Program prog) {
		return new CompileGoto(prog).doRun ();
	}
}
