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

import whiler.grammar.NonTerminal;
import whiler.grammar.Symbol;

/**
 * Represents an instance of a NonTerminal symbol in the parsed word. The parser tries the replacement rules until one
 * matches. The index of the matched rule can be queried via {@link getAppliedRule}. A NTermInst contains an array of
 * child symbols according to the selected replacement rule. These constitute a tree, the syntax tree.
 */
public class NTermInst extends SymbolInst {
	/**
	 * The NonTerminal of which this is an instance
	 */
	protected NonTerminal symbol;
	/**
	 * The child symbols in the syntax tree. The length of this array is equal to symbol.rules [getAppliedRule
	 * ()].symbols.length
	 */
	protected SymbolInst [] childSymbols;
	/**
	 * During the parsing process, contains the index of the rule which is currently tested. It is incremented during
	 * backtracking.
	 */
	protected int currentDecision;
	/**
	 * During the parsing process, contains the index of the child which is currently being parsed.
	 */
	protected int currentChild;
	/**
	 * Index of this instance in the parent symbol's childSymbols array, or -1 for the root
	 */
	protected int parentChildPos;
	/**
	 * The index of the first character in the parsed word after this symbol. Is updated by the Parsed when this
	 * instance has been parsed completely and a recursion return is performed.
	 */
	protected int endTextPos;
	
	public NTermInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos, NonTerminal symbol, int parentChildPos) {
		super (parentDecision, parentSymbol, textPos);
		this.symbol = symbol;
		this.parentChildPos = parentChildPos;
		currentDecision = 0;
		endTextPos = textPos;
		
		decide ();
	}
	
	/**
	 * @return The symbols of the rule currently being tried
	 */
	protected Symbol [] currRuleSymbols () {
		return symbol.rules [currentDecision].symbols;
	}
	
	/**
	 * Used by the parser. Creates a new childSymbols array according to the current decision.
	 */
	protected void decide () {
		endTextPos = textPos;
		
		currentChild = 0;
		if (currRuleSymbols ().length == 0) {
			childSymbols = null;
		} else {
			childSymbols = new SymbolInst [currRuleSymbols ().length];
		}
	}
	
	/**
	 * Creates a child symbol instance according to the current rule and current child and saves it in the childSymbols
	 * array.
	 * 
	 * @param lastDecision
	 *            The current last decision, is passed to the new child
	 * @return The new child
	 */
	protected SymbolInst makeChild (NTermInst lastDecision) {
		// Calculate text position of the new child
		int pos = currentChild == 0 ? textPos : childSymbols [currentChild - 1].getEndPos ();
		
		childSymbols [currentChild] = currRuleSymbols () [currentChild].makeInstance (lastDecision, this, pos, currentChild);
		return childSymbols [currentChild];
	}
	
	protected int getEndPos () {
		return endTextPos;
	}
	
	protected void toParseTree (StringBuilder sb, int depth) {
		for (int i = 0; i < depth; i++)
			sb.append ('\t');
		sb.append (symbol);
		sb.append (" {\n");
		for (int i = 0; i < currRuleSymbols ().length; i++)
			childSymbols [i].toParseTree (sb, depth + 1);
		
		for (int i = 0; i < depth; i++)
			sb.append ('\t');
		sb.append ("}\n");
	}
	
	public String collectString (Parser p) {
		if (childSymbols == null)
			return "";
		return p.text.substring (textPos, endTextPos);
	}
	
	/**
	 * @return	The number of the children in the syntax tree. Is equal to the number of symbols in the replacement rule used, which can be queried by {@link getAppliedRule}.
	 */
	public int getChildCount () {
		return childSymbols.length;
	}
	/**
	 * @return	The child in the syntax tree with the index i.
	 */
	public SymbolInst getChild (int i) {
		return childSymbols [i];
	}
	/**
	 * @return	The index of the replacement rule that was used for this NonTerminal. Can be used as an index for the {@link whiler.grammar.Rule#symbols} array.
	 */
	public int getAppliedRule () {
		return currentDecision;
	}
	
	public void collectNonTerminals (List<NTermInst> collect, NonTerminal search) {
		if (symbol == search)
			collect.add (this);
		else if (childSymbols != null)
			for (int i = 0; i < childSymbols.length; i++)
				childSymbols [i].collectNonTerminals (collect, search);
	}
}