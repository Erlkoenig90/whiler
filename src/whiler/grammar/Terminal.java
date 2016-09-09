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