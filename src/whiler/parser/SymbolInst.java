package whiler.parser;

import java.util.List;
import whiler.grammar.NonTerminal;

/**
 * Base class for symbol instances in the syntax tree. Since the derived NTermInst contains child symbols, a tree is
 * constructed via the Composite Pattern.
 */
public abstract class SymbolInst {
	/**
	 * The last decision that was made prior to creation of this symbol instance
	 */
	protected NTermInst parentDecision;
	/**
	 * The parent symbol in the syntax tree
	 */
	protected NTermInst parentSymbol;
	/**
	 * Index in the parsed word where the new symbol instance is located
	 */
	protected int textPos;
	
	SymbolInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos) {
		this.parentDecision = parentDecision;
		this.parentSymbol = parentSymbol;
		this.textPos = textPos;
	}
	
	/**
	 * @return The index of the first character in the parsed word after this symbol.
	 */
	abstract protected int getEndPos ();
	
	/**
	 * Call only after completion of the parsing process. Makes a string representation of the syntax tree.
	 * 
	 * @param depth
	 *            Indentation of the text. Pass 0 for the root.
	 */
	protected abstract void toParseTree (StringBuilder sb, int depth);
	
	/**
	 * Call only after completion of the parsing process. Makes a string representation of the syntax tree.
	 */
	public void toParseTree (StringBuilder sb) {
		toParseTree (sb, 0);
	}
	
	/**
	 * Call only after completion of the parsing process.
	 * 
	 * @return The whole string in the parsed word that was parsed as this Symbol.
	 */
	public abstract String collectString (Parser p);
	
	/**
	 * Call only after completion of the parsing process. Recursively searches for occurrences of the given NonTerminal
	 * search, and return them in the given List. The recursion stops at the found instances.
	 * 
	 * @param collect
	 *            The found instances are returned in this list
	 * @param search
	 *            The NonTerminal for whose instances to search
	 */
	abstract public void collectNonTerminals (List<NTermInst> collect, NonTerminal search);
};