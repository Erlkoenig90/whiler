package whiler.parser;

import java.util.List;
import whiler.grammar.NonTerminal;

public abstract class SymbolInst {
	protected NTermInst parentDecision, parentSymbol;
	protected int textPos;
	SymbolInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos) {
		this.parentDecision = parentDecision;
		this.parentSymbol = parentSymbol;
		this.textPos = textPos;
	}
	abstract protected int getEndPos ();
	abstract void toParseTree (StringBuilder sb, int depth);
	public abstract String collectString (Parser p);
	abstract public void collectNonTerminals (List<NTermInst> collect, NonTerminal search);
};