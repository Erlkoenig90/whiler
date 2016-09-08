package whiler.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import whiler.parser.NTermInst;
import whiler.parser.Parser;

public class Grammar {
	protected NonTerminal [] productions;
	protected Map<String, NonTerminal> nonTerminals;
	
	public Grammar (NonTerminal [] productions) {
		this.productions = productions;
		nonTerminals = new TreeMap <String, NonTerminal> ();
		for (int i = 0; i < productions.length; i++) {
			nonTerminals.put(productions [i].name, productions [i]);
		}
	}
	public Grammar (NonTerminal [] productions, Map<String, NonTerminal> nonTerminals) {
		this.productions = productions;
		this.nonTerminals = nonTerminals;
	}
	
	public NonTerminal root () { return productions [0]; }
	
	public static Grammar make (NonTerminal... productions) {
		return new Grammar (productions);
	}
	
	public NonTerminal get (String name) {
		return nonTerminals.get (name);
	}
	
	public static Grammar bnf;
	
	public void toBNF (StringBuilder sb) {
		for (int i = 0; i < productions.length; i++) {
			productions [i].prodToBNF (sb);
		}
	}
	
	public static String strToBNF (String in) {
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt (i);
			if (c == '\n')
				sb.append("\\n");
			else if (c == '\r')
				sb.append("\\r");
			else if (c == '\t')
				sb.append("\\t");
			else if (c == '\\')
				sb.append("\\\\");
			else if (c == '\"')
				sb.append("\\\"");
			else if (c >= 32 && c <= 126)
				sb.append(c);
			else
				sb.append(String.format("\\x%02x", (int) c));
		}
		return sb.toString ();
	}
	public static String strFromBNF (String in) {
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt (i);
			if (c == '\n')
				sb.append("\\n");
			else if (c == '\r')
				sb.append("\\r");
			else if (c == '\t')
				sb.append("\\t");
			else if (c == '\\')
				sb.append("\\\\");
			else if (c >= 32 && c <= 126)
				sb.append(c);
			else
				sb.append(String.format("\\x%02x", (int) c));
		}
		return sb.toString ();
	}
	
	public static Grammar fromBNF (String strBNF) throws Exception {
		Parser p = Parser.parse (bnf, strBNF);
		if (p == null) return null;
		
		NonTerminal BNF_Def = bnf.get("Def");
		NonTerminal BNF_Rule = bnf.get("Rule");
		NonTerminal BNF_Char = bnf.get("Char");
		NonTerminal BNF_Symbol = bnf.get("Symbol");
		
		List<NTermInst> definitions = new ArrayList<NTermInst> ();
		p.getRoot ().collectNonTerminals(definitions, BNF_Def);
		
		Map<String, NonTerminal> productions = new TreeMap<String, NonTerminal> ();
		NonTerminal [] productionsOrdered = new NonTerminal [definitions.size ()];
		
		for (int i = 0; i < definitions.size (); i++) {
			productionsOrdered [i] = new NonTerminal (((NTermInst) definitions.get (i).getChild (1)).getChild (1).collectString (p));
			productions.put (productionsOrdered [i].name, productionsOrdered [i]);
		}
		
		for (int i = 0; i < definitions.size (); i++) {
			List<NTermInst> ruleTrees = new ArrayList <NTermInst> ();
			definitions.get (i).getChild (4).collectNonTerminals (ruleTrees, BNF_Rule);
			
			Rule [] rules = new Rule [ruleTrees.size ()];
			for (int j = 0; j < rules.length; j++) {
				List<NTermInst> symbolTrees = new ArrayList<NTermInst> ();
				ruleTrees.get (j).collectNonTerminals (symbolTrees, BNF_Symbol);
				Symbol [] symbols = new Symbol [symbolTrees.size ()];
				for (int k = 0; k < symbols.length; k++) {
					if (symbolTrees.get (k).getAppliedRule () == 0) {
						List<NTermInst> charTrees = new ArrayList<NTermInst> ();
						((NTermInst) symbolTrees.get (k).getChild (0)).getChild (1).collectNonTerminals(charTrees, BNF_Char);
						
						StringBuilder sb = new StringBuilder (charTrees.size ());
						for (int l = 0; l < charTrees.size (); l++) {
							char c = (char) charTrees.get(l).getAppliedRule ();
							sb.append(c);
						}
						
						symbols [k] = new Terminal (sb.toString ());
					} else {
						String name = ((NTermInst) symbolTrees.get (k).getChild (0)).getChild (1).collectString (p);
						if (!productions.containsKey(name))
							throw new Exception ("Invalid NonTerminal \"" + name + "\"");
						symbols [k] = productions.get (name);
					}
				}
				rules [j] = new Rule (symbols);
			}
			productionsOrdered [i].setRules(rules);
		}
		
		return new Grammar (productionsOrdered);
	}
	static {
		NonTerminal BNF2 = NonTerminal.make ("BNF2");
		NonTerminal BNF = NonTerminal.make ("BNF");
		NonTerminal Def = NonTerminal.make ("Def");
		NonTerminal Rules = NonTerminal.make ("Rules");
		NonTerminal Rules2 = NonTerminal.make ("Rules2");
		NonTerminal Rule_ = NonTerminal.make ("Rule");
		NonTerminal Symbol = NonTerminal.make ("Symbol");
		NonTerminal String = NonTerminal.make ("String");
		NonTerminal Chars = NonTerminal.make ("Chars");
		NonTerminal NTerm = NonTerminal.make ("NTerm");
		NonTerminal Name = NonTerminal.make ("Name");
		NonTerminal Name2 = NonTerminal.make ("Name2");
		NonTerminal SpaceE = NonTerminal.make ("SpaceE");
		NonTerminal Space = NonTerminal.make ("Space");
		
		Rule [] charRules = new Rule [128];
		for (char i = 0; i <= 127; i++) {
			String s = strToBNF(Character.toString(i));
			charRules [i] = Rule.make(Terminal.make(s));
		}
		NonTerminal Char = new NonTerminal ("Char", charRules);
		Rule [] AlphaRules = new Rule [52];
		Rule [] AlnumRules = new Rule [62];
		for (char i = 0; i < 26; i++) {
			AlnumRules [2 * i] = AlphaRules [2 * i] = Rule.make(Terminal.make(Character.toString((char) ('a' + i))));
			AlnumRules [2 * i + 1] = AlphaRules [2 * i + 1] = Rule.make(Terminal.make(Character.toString((char) ('A' + i))));
		}
		for (char i = 0; i < 10; i++) {
			AlnumRules [52 + i] = Rule.make(Terminal.make("" + Character.toString((char) ('0' + i))));
		}
		NonTerminal Alpha = new NonTerminal ("Alpha", AlphaRules);
		NonTerminal Alnum = new NonTerminal ("Alnum", AlnumRules);

		
		
		BNF.setRules(Rule.make (Def, BNF2));
		
		BNF2.setRules(
				Rule.make (Terminal.make("\n"), Def, BNF2),
				Rule.make (Terminal.make("\n"), BNF2),
				Rule.make ()
		);
		Def.setRules(Rule.make (SpaceE, NTerm, SpaceE, Terminal.make("::="), Rules));
		Rules.setRules(Rule.make (Rule_, Rules2));
		Rules2.setRules (
				Rule.make (Terminal.make("|"), Rule_, Rules2),
				Rule.make()
		);
		Rule_.setRules(
				Rule.make (SpaceE),
				Rule.make (SpaceE, Symbol, Rule_)
		);
		Symbol.setRules(
				Rule.make(String),
				Rule.make(NTerm)
		);
		String.setRules (Rule.make(Terminal.make("\""), Chars, Terminal.make("\"")));

		Chars.setRules(
				Rule.make(Char, Chars),
				Rule.make()
		);
		NTerm.setRules(Rule.make(Terminal.make("<"), Name, Terminal.make(">")));
		Name.setRules(
				Rule.make(Alpha),
				Rule.make(Alpha, Name2)
		);
		Name2.setRules(
				Rule.make(Alnum, Name2),
				Rule.make()
		);
		SpaceE.setRules(
				Rule.make(Space),
				Rule.make()
		);
		Space.setRules(
				Rule.make(Terminal.make(" "), SpaceE),
				Rule.make(Terminal.make("\t"), SpaceE),
				Rule.make(Terminal.make("\r"), SpaceE)
		);
		
		bnf = make (BNF, BNF2, Def, Rules, Rules2, Rule_, Symbol, String, Chars, Char, NTerm, Name, Name2, SpaceE, Space, Alpha, Alnum);
	}
}
