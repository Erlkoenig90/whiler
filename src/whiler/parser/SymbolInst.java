package whiler.parser;

public abstract class SymbolInst {
	protected NTermInst parentDecision, parentSymbol;
	protected int textPos;
	SymbolInst (NTermInst parentDecision, NTermInst parentSymbol, int textPos) {
		this.parentDecision = parentDecision;
		this.parentSymbol = parentSymbol;
		this.textPos = textPos;
	}
	abstract protected int getEndPos ();
	abstract void toString (StringBuilder sb, int depth);
};