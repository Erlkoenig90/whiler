package whiler.grammar;
import whiler.parser.NTermInst;
import whiler.parser.SymbolInst;

public abstract class Symbol {
	public abstract SymbolInst makeInstance (NTermInst parentDecision, NTermInst parentSymbol, int textPos, int parentChildPos);
	public abstract void toBNF (StringBuilder sb);
}
