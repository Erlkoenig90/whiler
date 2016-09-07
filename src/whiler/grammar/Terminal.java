package whiler.grammar;

import whiler.parser.NTermInst;
import whiler.parser.TermInst;

public class Terminal extends Symbol {
	public String text;
	
	public Terminal (String text) {
		this.text = text;
	}

	public TermInst makeInstance (NTermInst parentDecision, NTermInst parentSymbol, int textPos, int parentChildPos) {
		return new TermInst (parentDecision, parentSymbol, textPos, this);
	}
	static public Terminal make (String text) {
		return new Terminal (text);
	}
	public void toBNF (StringBuilder sb) {
		sb.append('"');
		sb.append(Grammar.strToBNF(text));
		sb.append('"');
	}
}