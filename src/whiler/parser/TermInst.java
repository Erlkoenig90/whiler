package whiler.parser;

import java.util.List;
import whiler.grammar.NonTerminal;
import whiler.grammar.Terminal;

public class TermInst extends SymbolInst {
	protected Terminal symbol;
	public TermInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos, Terminal symbol) {
		super (parentDecision, parentSymbol, textPos);
		this.symbol = symbol;
	}
	protected int getEndPos() {
		return textPos + symbol.text.length ();
	}
	void toParseTree (StringBuilder sb, int depth) {
		for (int i = 0; i < depth; i++) sb.append('\t');
		sb.append('"');
		sb.append(symbol.text.replace("\"", "\\\""));
		sb.append("\"\n");
	}
	public String collectString (Parser p) {
		return symbol.text;
	}
	public void collectNonTerminals (List<NTermInst> collect, NonTerminal search) {}
};
