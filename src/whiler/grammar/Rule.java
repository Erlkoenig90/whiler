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
