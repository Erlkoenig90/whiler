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

/**
 * Each {@link NonTerminal} contains 0 or more Rule instances. Each Rule instance contains a sequence of Symbols with
 * which the NonTerminal can be replaced.
 */
public class Rule {
	/**
	 * The replacement symbols
	 */
	public Symbol [] symbols;
	
	public Rule (Symbol [] symbols) {
		this.symbols = symbols;
	}
	
	/**
	 * Convenience method for creating new Rules.
	 * 
	 * @param symbols
	 * @return A new Rule instance with the given symbols
	 */
	public static Rule make (Symbol... symbols) {
		return new Rule (symbols);
	}
	
	/**
	 * Makes a string representation of the replacement rule (sequence of symbols).
	 */
	public void toBNF (StringBuilder sb) {
		for (int i = 0; i < symbols.length; i++) {
			if (i > 0)
				sb.append (' ');
			symbols [i].toBNF (sb);
		}
	}
}
