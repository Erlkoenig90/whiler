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

package whiler.parser;

import java.util.List;

import whiler.grammar.Grammar;
import whiler.grammar.NonTerminal;
import whiler.grammar.Terminal;

/**
 * An instance of a Terminal symbol in the parsed word.
 */
public class TermInst extends SymbolInst {
	/**
	 * The corresponding Terminal symbol.
	 */
	protected Terminal symbol;
	public TermInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos, Terminal symbol) {
		super (parentDecision, parentSymbol, textPos);
		this.symbol = symbol;
	}
	protected int getEndPos() {
		return textPos + symbol.text.length ();
	}
	protected void toParseTree (StringBuilder sb, int depth) {
		for (int i = 0; i < depth; i++) sb.append('\t');
		sb.append('"');
		sb.append(Grammar.strToBNF (symbol.text));
		sb.append("\"\n");
	}
	public String collectString (Parser p) {
		return symbol.text;
	}
	public void collectNonTerminals (List<NTermInst> collect, NonTerminal search) {
		// Does nothing
	}
};
