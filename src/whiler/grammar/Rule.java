package whiler.grammar;

public class Rule {
	public Symbol [] symbols;
	
	public Rule (Symbol [] symbols) {
		this.symbols = symbols;
	}
	public static Rule make (Symbol...symbols) {
		return new Rule (symbols);
	}
	public void toBNF (StringBuilder sb) {
		for (int i = 0; i < symbols.length; i++) {
			if (i > 0)
				sb.append (' ');
			symbols [i].toBNF (sb);
		}
	}
}
