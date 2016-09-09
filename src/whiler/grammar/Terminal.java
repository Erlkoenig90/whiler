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

package whiler.grammar;

import whiler.parser.NTermInst;
import whiler.parser.TermInst;

/**
 * A Terminal symbol for a context free grammar, i.e. a string.
 */
public class Terminal extends Symbol {
	/**
	 * The string to check for in the parsed word
	 */
	public String text;
	
	public Terminal (String text) {
		this.text = text;
	}
	
	public TermInst makeInstance (NTermInst parentDecision, NTermInst parentSymbol, int textPos, int parentChildPos) {
		return new TermInst (parentDecision, parentSymbol, textPos, this);
	}
	
	/**
	 * Convenience method for creating Terminal instances
	 * 
	 * @return A new Terminal instance with the given text
	 */
	static public Terminal make (String text) {
		return new Terminal (text);
	}
	
	public void toBNF (StringBuilder sb) {
		sb.append ('"');
		// Append quoted string
		sb.append (Grammar.strToBNF (text));
		sb.append ('"');
	}
}