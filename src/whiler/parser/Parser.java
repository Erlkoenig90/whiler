package whiler.parser;

import whiler.grammar.Grammar;

public class Parser {
	private Grammar grammar;
	private String text;
	private NTermInst root;
	private SymbolInst current;
	private int code;
	private NTermInst lastDecision;
	
	public static Parser parse (Grammar grammar, String text) {
		return new Parser (grammar, text).run ();
	}
	
	private Parser (Grammar grammar, String text) {
		this.grammar = grammar;
		this.text = text;
		lastDecision = null;
		code = 0;
	}
	private void clearDecisions (NTermInst inst) {
		inst.childSymbols = null;
		inst.currentChild = 0;
		while (inst != null && inst.parentSymbol != null) {
			inst.parentSymbol.currentChild = inst.parentChildPos;
			for (int i = inst.parentChildPos + 1; i < inst.parentSymbol.childSymbols.length; i++) {
				inst.parentSymbol.childSymbols [i] = null;
			}
			inst = inst.parentSymbol;
		}
	}
	private boolean backtrack () {
		NTermInst pd = lastDecision;
		while (pd != null) {
			if (pd.currentDecision + 1 < pd.symbol.rules.length) {
				current = pd;
				clearDecisions (pd);
				lastDecision = pd;
				pd.currentDecision++;
				pd.decide ();
				return true;
			}
			pd = pd.parentDecision;
		}
		return false;
	}
	private void returnRecursion () {
		// root?
		if (current.parentSymbol == null) {
			if (current.getEndPos () == text.length ()) {
				// Fertig geparst
				code = 1;
			} else {
				code = backtrack () ? 0 : -1;
			}
		} else {
			current.parentSymbol.currentChild++;
			current = current.parentSymbol;
		}
	}
	private Parser run () {
		root = new NTermInst (null, null, 0, grammar.root (), -1);
		current = root;
		lastDecision = root;
		
		while (code == 0) {
			if (current instanceof TermInst) {
				TermInst currterm = (TermInst) current;
				int begin = currterm.textPos;
				int end = currterm.textPos + currterm.symbol.text.length ();
				if (text.length () >= end && currterm.symbol.text.equals (text.substring (begin, end))) {
					returnRecursion ();
				} else {
					if (!backtrack ()) code = -1;
				}
			} else if (current instanceof NTermInst) {
				NTermInst currnterm = (NTermInst) current;
				if (currnterm.currentChild < currnterm.currRuleSymbols ().length) {
					current = currnterm.makeChild (lastDecision);
					if (current instanceof NTermInst && ((NTermInst) current).symbol.rules.length > 1) {
						lastDecision = (NTermInst) current;
					}
				} else {
					currnterm.endTextPos = currnterm.currRuleSymbols ().length == 0 ? currnterm.textPos : currnterm.childSymbols [currnterm.currRuleSymbols ().length - 1].getEndPos ();
					
					returnRecursion ();
				}
			}
		}
		return code == 1 ? this : null;
	}
	public NTermInst getRoot () {
		return root;
	}
}
