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
import whiler.parser.SymbolInst;

/**
 * Base class for symbols in the grammar, i.e. NonTerminals and Terminals.
 */
public abstract class Symbol {
	/**
	 * Used by the parser - create an instance of this symbol for the syntax tree
	 * 
	 * @param parentDecision
	 *            The last decision that was made prior to creation of this symbol instance
	 * @param parentSymbol
	 *            The parent symbol in the syntax tree
	 * @param textPos
	 *            Index in the parsed word where the new symbol instance is located
	 * @param parentChildPos
	 *            Index of the new symbol instance in the parent symbol's childSymbols array
	 * @return The new Symbol instance.
	 */
	public abstract SymbolInst makeInstance (NTermInst parentDecision, NTermInst parentSymbol, int textPos, int parentChildPos);
	
	/**
	 * Makes a String representation of a reference to this symbol, for usage in replacement rules lists.
	 */
	public abstract void toBNF (StringBuilder sb);
}
