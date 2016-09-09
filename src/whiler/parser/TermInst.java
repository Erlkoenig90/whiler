package whiler.parser;

import java.util.List;

import whiler.grammar.Grammar;
import whiler.grammar.NonTerminal;
import whiler.grammar.Terminal;

/**
 * An instance of a Terminal symbol in the parsed word.
 */
public class TermInst extends SymbolInst {
	/**
	 * The corresponding Terminal symbol.
	 */
	protected Terminal symbol;
	public TermInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos, Terminal symbol) {
		super (parentDecision, parentSymbol, textPos);
		this.symbol = symbol;
	}
	protected int getEndPos() {
		return textPos + symbol.text.length ();
	}
	protected void toParseTree (StringBuilder sb, int depth) {
		for (int i = 0; i < depth; i++) sb.append('\t');
		sb.append('"');
		sb.append(Grammar.strToBNF (symbol.text));
		sb.append("\"\n");
	}
	public String collectString (Parser p) {
		return symbol.text;
	}
	public void collectNonTerminals (List<NTermInst> collect, NonTerminal search) {
		// Does nothing
	}
};
