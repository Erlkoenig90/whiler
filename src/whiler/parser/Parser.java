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

import whiler.grammar.Grammar;

/**
 * Parses a word using a context free grammar by simulating a nondeterministic stack machine that produces the given
 * word.
 * 
 * A depth-first-search is used to traverse the decision tree, which is faster than a breadth-first but imposes certain
 * restrictions on the grammar. Specifically, the first element in any replacement rule may not be equal to the
 * NonTerminal being replaced, and may not be (indirectly) be replaced by the NonTerminal. In other words, a recursion
 * may not be directly or indirectly be initiated by the first symbol in a rule.
 * 
 * For example:
 * 
 * <pre>
 * {@code
 * <Sequence> ::= <Statement> | <Statement> \";\" <Sequence>
 * <Statement> ::= <SetZero> | <Assign> | <Increment> | <While>
 * }
 * </pre>
 * 
 * is ok, but
 * 
 * <pre>
 * {@code
 * <Statement> ::= <SetZero> | <Assign> | <Increment> | <While> | <Statement> \";\" <Statement>
 * }
 * </pre>
 * 
 * is not (will lead to an infinite recursion/memory overflow).
 * 
 * The decision tree is not explicitly modeled, since only the current path (from root decision to the current decision)
 * is of interest, and all old paths and nodes correspond to invalid productions. Since decisions only occur on
 * NonTerminal instances, where rules are selected, the current path on the decision tree is comprised of a linked list
 * of NTermInst instances. Specifically, the {@link Parser#lastDecision} variable refers to the NonTerminal instance
 * where the last decision was made. Since each NonTerminal instance contains a reference
 * ({@link SymbolInst#parentDecision}) to the previous decision, the reverse path to the first decision can be obtained.
 * 
 * The syntax tree, however, is modeled explicitly. The {@link Parser#root} variable contains the root NonTerminal
 * instance of the syntax tree. The {@link Parser#current} variable contains a reference to the node currently being
 * investigated (same as {@link Parser#root} in the beginning). In each parser step, for NonTerminal instances, a new
 * child for the current node is created and set as the current one. For Terminal instances, their text is checked for
 * equality with the parsed word. After processing of a Terminal instance, and when all children of a NonTerminal
 * instance have been parsed, a recursion return is performed, i.e. the parent of the current node is set as the current
 * node. When the root node is reached again, the word is accepted if no more characters remain to be parsed, and
 * rejected if there are superfluous characters.
 * 
 * If a decision turns out to be wrong - i.e. a Terminal symbol does not match the parsed word - a backtrack step is
 * performed, i.e. the last decision is revised, and the next replacement rule for that NTermInst is checked. If all
 * rules have been tried, another backtrack step is performed. If during backtracking a NTermInst with
 * {@link SymbolInst#parentDecision} = null is found, i.e. there are no more decisions to revise, a syntax error in the
 * word is assumed, i.e. the word is not accepted. During backtracking, all syntax tree elements that were generated
 * since the last decision have to be removed ({@link Parser#clearDecisions}), and new ones will be generated according
 * to the new decision.
 */
public class Parser {
	/**
	 * The grammar to use for parsing.
	 */
	private Grammar grammar;
	/**
	 * The word to parse.
	 */
	protected String text;
	/**
	 * The root of the syntax tree
	 */
	private NTermInst root;
	/**
	 * The symbol currently being investigated
	 */
	private SymbolInst current;
	/**
	 * Control word for the parsing process. 0=Continue parsing, 1=Accept word, -1=Reject word
	 */
	private int code;
	/**
	 * Reference to the NonTerminal instance, where the last decision was made. Forms the head of a linked list of
	 * decisions.
	 */
	private NTermInst lastDecision;
	
	/**
	 * Parse a word using the given grammar.
	 * 
	 * @param grammar
	 *            The context-free grammar to use
	 * @param text
	 *            The word to parse
	 * @return A Parser instance containing the syntax tree, or null in case of a syntax error (rejection of the word).
	 */
	public static Parser parse (Grammar grammar, String text) {
		return new Parser (grammar, text).run ();
	}
	
	private Parser (Grammar grammar, String text) {
		this.grammar = grammar;
		this.text = text;
		lastDecision = null;
		code = 0;
	}
	
	/**
	 * In all (recursive) parents of the "inst" node, clear all children with higher indices than the respective
	 * iterated child instance (set the entry in the #{link {@link NTermInst#childSymbols}} to null). In other words, if
	 * the syntax tree were printed out with lower-numbered children to the left, clear everything right of the "inst"
	 * node. Additionally, the #{@link NTermInst#currentChild} variable is set to the respective child. This is used
	 * during backtracking, to remove all now invalid syntax elements.
	 */
	private void clearDecisions (NTermInst inst) {
		inst.childSymbols = null;
		inst.currentChild = 0;
		// Ascend in the tree until reaching the root node
		while (inst != null && inst.parentSymbol != null) {
			inst.parentSymbol.currentChild = inst.parentChildPos;
			// Clear children
			for (int i = inst.parentChildPos + 1; i < inst.parentSymbol.childSymbols.length; i++) {
				inst.parentSymbol.childSymbols [i] = null;
			}
			// Process parent
			inst = inst.parentSymbol;
		}
	}
	
	/**
	 * Perform a backtracking step, i.e. return to the syntax tree node where the last decision was made. There select
	 * the next decision (replacement rule). If there is no next rule, perform another backtracking step. The whole
	 * parser state is reset to that decision point, with only the {@link NTermInst#currentDecision} of the respective
	 * node incremented.
	 * 
	 * @return true if the backtracking was successful, and false if there are no more decisions to revise, i.e. the
	 *         word is rejected.
	 */
	private boolean backtrack () {
		NTermInst pd = lastDecision;
		while (pd != null) {
			// Check if another rule can be selected
			if (pd.currentDecision + 1 < pd.symbol.rules.length) {
				// Reset parser state
				current = pd;
				clearDecisions (pd);
				lastDecision = pd;
				pd.currentDecision++;
				pd.decide ();
				return true;
			}
			// If not, continue with the previous decision
			pd = pd.parentDecision;
		}
		// No more decisions
		return false;
	}
	
	/**
	 * Performs a recursion step, i.e. sets the current node to its parent. If the current node is the root node, and
	 * the whole text has been parsed, accept the word. Else, perform a backtrack step.
	 */
	private void returnRecursion () {
		// root?
		if (current.parentSymbol == null) {
			if (current.getEndPos () == text.length ()) {
				// Fertig geparst
				code = 1;
			} else {
				code = backtrack () ? 0 : -1;
			}
		} else {
			// Go to the parent node, and continue with its next child.
			current.parentSymbol.currentChild++;
			current = current.parentSymbol;
		}
	}
	
	/**
	 * Perform the actual parsing process
	 * 
	 * @return this if successful, null else (syntax error)
	 */
	private Parser run () {
		// Create root node
		root = new NTermInst (null, null, 0, grammar.root (), -1);
		current = root;
		lastDecision = root;
		
		// Continue until returnRecursion signals success/error
		while (code == 0) {
			if (current instanceof TermInst) {
				// Process Terminal symbols
				TermInst currterm = (TermInst) current;
				int begin = currterm.textPos;
				int end = currterm.textPos + currterm.symbol.text.length ();
				// Check if the symbol matches the word
				if (text.length () >= end && currterm.symbol.text.equals (text.substring (begin, end))) {
					// If yes, continue with the next child of the parent
					returnRecursion ();
				} else {
					// Last decision was wrong - perform backtracking
					if (!backtrack ())
						code = -1;
				}
			} else if (current instanceof NTermInst) {
				// Process NonTerminal Symbols
				NTermInst currnterm = (NTermInst) current;
				if (currnterm.currentChild < currnterm.currRuleSymbols ().length) {
					// The replacement rule has more symbols
					
					// Make child symbol
					current = currnterm.makeChild (lastDecision);
					// If this child represents a decision (has more than one rule), remember it as the last decision
					if (current instanceof NTermInst && ((NTermInst) current).symbol.rules.length > 1) {
						lastDecision = (NTermInst) current;
					}
				} else {
					// NonTerminal was completely processed - calculate its length in the word
					currnterm.endTextPos = currnterm.currRuleSymbols ().length == 0 ? currnterm.textPos : currnterm.childSymbols [currnterm.currRuleSymbols ().length - 1].getEndPos ();
					
					// Return to parent
					returnRecursion ();
				}
			}
		}
		return code == 1 ? this : null;
	}
	
	/**
	 * @return The root node of the syntax tree
	 */
	public NTermInst getRoot () {
		return root;
	}
}
