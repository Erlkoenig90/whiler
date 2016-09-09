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
