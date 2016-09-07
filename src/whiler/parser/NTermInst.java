package whiler.parser;

import whiler.grammar.NonTerminal;
import whiler.grammar.Symbol;

public class NTermInst extends SymbolInst {
	protected NonTerminal symbol;
	protected SymbolInst [] childSymbols;
	protected int currentDecision, currentChild, parentChildPos, endTextPos;
	
	public NTermInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos, NonTerminal symbol, int parentChildPos) {
		super (parentDecision, parentSymbol, textPos);
		this.symbol = symbol;
		this.parentChildPos = parentChildPos;
		currentDecision = 0;
		endTextPos = textPos;
		
		decide ();
	}
	protected Symbol [] currRuleSymbols () {
		return symbol.rules [currentDecision].symbols;
	}
	protected void decide () {
		endTextPos = textPos;
		
		currentChild = 0;
		if (currRuleSymbols ().length == 0) {
			childSymbols = null;
		} else {
			childSymbols = new SymbolInst [currRuleSymbols ().length];
		}
	}
	protected SymbolInst makeChild (NTermInst lastDecision) {
		int pos = currentChild == 0 ? textPos : childSymbols [currentChild - 1].getEndPos ();
		
		childSymbols [currentChild] = currRuleSymbols () [currentChild].makeInstance (lastDecision, this, pos, currentChild);
		return childSymbols [currentChild];
	}
	protected int getEndPos() {
		return endTextPos;
	}
	
	public void toString (StringBuilder sb, int depth) {
		for (int i = 0; i < depth; i++) sb.append('\t');
		sb.append(symbol);
		sb.append (" {\n");
		for (int i = 0; i < currRuleSymbols ().length; i++)
			childSymbols [i].toString (sb, depth + 1);
		
		for (int i = 0; i < depth; i++) sb.append('\t');
		sb.append ("}\n");
	}
}