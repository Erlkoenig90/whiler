package whiler.grammar;

import whiler.parser.NTermInst;

public class NonTerminal extends Symbol {
	public Rule [] rules;
	protected String name;
	
	public NonTerminal (String name, Rule [] rules) {
		this.name = name;
		this.rules = rules;
	}
	
	public NonTerminal (String name) {
		this.name = name;
		this.rules = null;
	}

	public NTermInst makeInstance (NTermInst parentDecision, NTermInst parentSymbol, int textPos, int parentChildPos) {
		return new NTermInst(parentDecision, parentSymbol, textPos, this, parentChildPos);
	}
	
	public static NonTerminal make (String name, Rule... rules) {
		return new NonTerminal (name, rules);
	}
	
	public void setRules (Rule... rules) {
		this.rules = rules;
	}
	
	public String getName () { return name; }
	
	public String toString () { return "<" + name + ">"; }
	
	public void prodToBNF (StringBuilder sb) {
		sb.append(this);
		sb.append(" ::= ");
		for (int i = 0; i < rules.length; i++) {
			if (i > 0)
				sb.append(" | ");
			rules [i].toBNF (sb);
		}
		sb.append('\n');
	}
	public void toBNF (StringBuilder sb) {
		sb.append (this);
	}
}