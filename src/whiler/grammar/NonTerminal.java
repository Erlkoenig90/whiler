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

/**
 * Represents a non-terminal symbol in a context-free grammar including its replacement rules.
 */
public class NonTerminal extends Symbol {
	/**
	 * Replacement/production rules for this NonTerminal
	 */
	public Rule [] rules;
	/**
	 * Name of the NonTerminal, excluding the <> brackets
	 */
	protected String name;
	
	public NonTerminal (String name, Rule [] rules) {
		this.name = name;
		this.rules = rules;
	}
	
	/**
	 * If this constructor is used, {@link setRules} must be called later to set the rules.
	 * 
	 * @param name
	 */
	public NonTerminal (String name) {
		this.name = name;
		this.rules = null;
	}
	
	public NTermInst makeInstance (NTermInst parentDecision, NTermInst parentSymbol, int textPos, int parentChildPos) {
		return new NTermInst (parentDecision, parentSymbol, textPos, this, parentChildPos);
	}
	
	/**
	 * Convenience method for creating NonTerminals
	 * 
	 * @return A NonTerminal instance with the given name and rules
	 */
	public static NonTerminal make (String name, Rule... rules) {
		return new NonTerminal (name, rules);
	}
	
	/**
	 * Alternatively to passing the rules to the constructor, they can be set later using setRules.
	 */
	public void setRules (Rule... rules) {
		this.rules = rules;
	}
	
	/**
	 * @return Name of the NonTerminal, excluding the <> brackets
	 */
	public String getName () {
		return name;
	}
	
	public String toString () {
		return "<" + name + ">";
	}
	
	/**
	 * Makes a string representation of the NonTerminal and its rules in BNF.
	 */
	public void prodToBNF (StringBuilder sb) {
		sb.append (this);
		sb.append (" ::= ");
		for (int i = 0; i < rules.length; i++) {
			if (i > 0)
				sb.append (" | ");
			rules [i].toBNF (sb);
		}
		sb.append ('\n');
	}
	
	/**
	 * Appends a reference to this NonTerminal to the StringBuilder
	 */
	public void toBNF (StringBuilder sb) {
		sb.append (this);
	}
}