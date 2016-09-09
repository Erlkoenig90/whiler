package whiler.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import whiler.parser.NTermInst;
import whiler.parser.Parser;

/**
 * Represents a context free grammar.
 */
public class Grammar {
	/**
	 * Array of non terminal symbols, which include their respective replacement rules.
	 */
	protected NonTerminal [] productions;
	/**
	 * Maps NonTerminal names to the instances in the {@link productions} array.
	 */
	protected Map<String, NonTerminal> nonTerminals;
	
	/**
	 * Creates the Grammar from an array of productions, filling the map automatically
	 */
	public Grammar (NonTerminal [] productions) {
		this.productions = productions;
		nonTerminals = new TreeMap<String, NonTerminal> ();
		for (int i = 0; i < productions.length; i++) {
			nonTerminals.put (productions [i].name, productions [i]);
		}
	}
	
	/**
	 * Creates the Grammar from an array of productions and a given map
	 */
	public Grammar (NonTerminal [] productions, Map<String, NonTerminal> nonTerminals) {
		this.productions = productions;
		this.nonTerminals = nonTerminals;
	}
	
	/**
	 * @return the NonTerminal that represents the whole parsed word. This is per convention the first NonTerminal
	 *         symbol.
	 */
	public NonTerminal root () {
		return productions [0];
	}
	
	/**
	 * @return A new grammar with the given NonTerminals
	 */
	public static Grammar make (NonTerminal... productions) {
		return new Grammar (productions);
	}
	
	/**
	 * @return The NonTerminal symbol with the given name
	 */
	public NonTerminal get (String name) {
		return nonTerminals.get (name);
	}
	
	/**
	 * The Grammar for the BNF.
	 */
	public static final Grammar bnf;
	
	/**
	 * Converts this Grammar into BNF form.
	 * 
	 * @param sb
	 *            StringBuilder to append the BNF form to
	 */
	public void toBNF (StringBuilder sb) {
		for (int i = 0; i < productions.length; i++) {
			productions [i].prodToBNF (sb);
		}
	}
	
	/**
	 * Escapes a string suitable for placing it into a BNF file.
	 * 
	 * @param in
	 *            A string
	 * @return The string with all non-printable characters and \n,\r,\t,\," escaped
	 */
	public static String strToBNF (String in) {
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < in.length (); i++) {
			char c = in.charAt (i);
			if (c == '\n')
				sb.append ("\\n");
			else if (c == '\r')
				sb.append ("\\r");
			else if (c == '\t')
				sb.append ("\\t");
			else if (c == '\\')
				sb.append ("\\\\");
			else if (c == '\"')
				sb.append ("\\\"");
			else if (c >= 32 && c <= 126)
				sb.append (c);
			else
				sb.append (String.format ("\\x%02x", (int) c));
		}
		return sb.toString ();
	}
	
	/**
	 * Parses a BNF grammar and constructs a Grammar object.
	 * 
	 * @param strBNF
	 *            A context free grammar in BNF.
	 * @return A new Grammar instance or null in case of a parser error
	 */
	public static Grammar fromBNF (String strBNF) throws Exception {
		Parser p = Parser.parse (bnf, strBNF);
		// Parser error
		if (p == null)
			return null;
		
		// Shortcuts for NonTerminals of the BNF
		NonTerminal BNF_Def = bnf.get ("Def");
		NonTerminal BNF_Rule = bnf.get ("Rule");
		NonTerminal BNF_Char = bnf.get ("Char");
		NonTerminal BNF_Symbol = bnf.get ("Symbol");
		
		// Collect list of NonTerminal definitions
		List<NTermInst> definitions = new ArrayList<NTermInst> ();
		p.getRoot ().collectNonTerminals (definitions, BNF_Def);
		
		// Make Array & Map for found NonTerminals
		Map<String, NonTerminal> productions = new TreeMap<String, NonTerminal> ();
		NonTerminal [] productionsOrdered = new NonTerminal [definitions.size ()];
		
		// Make NonTerminal instances using the parsed names. These have to be created first because rules may reference
		// NonTerminals on following lines
		for (int i = 0; i < definitions.size (); i++) {
			productionsOrdered [i] = new NonTerminal (((NTermInst) definitions.get (i).getChild (1)).getChild (1).collectString (p));
			productions.put (productionsOrdered [i].name, productionsOrdered [i]);
		}
		
		// Fill NonTerminal instances with rules
		for (int i = 0; i < definitions.size (); i++) {
			// Collect rule definitions
			List<NTermInst> ruleTrees = new ArrayList<NTermInst> ();
			definitions.get (i).getChild (4).collectNonTerminals (ruleTrees, BNF_Rule);
			
			Rule [] rules = new Rule [ruleTrees.size ()];
			// Iterate rules for this NonTerminal
			for (int j = 0; j < rules.length; j++) {
				// Collect Symbols for this rule
				List<NTermInst> symbolTrees = new ArrayList<NTermInst> ();
				ruleTrees.get (j).collectNonTerminals (symbolTrees, BNF_Symbol);
				Symbol [] symbols = new Symbol [symbolTrees.size ()];
				
				// Iterate Symbols
				for (int k = 0; k < symbols.length; k++) {
					if (symbolTrees.get (k).getAppliedRule () == 0) {
						// Is a terminal symbol (string)
						
						// Collect Char Instances
						List<NTermInst> charTrees = new ArrayList<NTermInst> ();
						((NTermInst) symbolTrees.get (k).getChild (0)).getChild (1).collectNonTerminals (charTrees, BNF_Char);
						
						// Iterate all Char's
						StringBuilder sb = new StringBuilder (charTrees.size ());
						for (int l = 0; l < charTrees.size (); l++) {
							/*
							 * Use applied rule to create a "char". Since the rules 0-127 correspond to the
							 * ASCII-characters 0-127, no de-escaping/unquoting is required, e.g.
							 * \" is parsed as rule 34, which is the ASCII code of ".
							 */
							char c = (char) charTrees.get (l).getAppliedRule ();
							sb.append (c);
						}
						// Make Terminal instance using the built string
						symbols [k] = new Terminal (sb.toString ());
					} else {
						// Get reference to NonTerminal symbol
						String name = ((NTermInst) symbolTrees.get (k).getChild (0)).getChild (1).collectString (p);
						if (!productions.containsKey (name))
							throw new Exception ("Invalid NonTerminal \"" + name + "\"");
						symbols [k] = productions.get (name);
					}
				}
				rules [j] = new Rule (symbols);
			}
			// Save the generated rules into the NonTerminal instance
			productionsOrdered [i].setRules (rules);
		}
		
		// Make Grammar object.
		return new Grammar (productionsOrdered);
	}
	
	static {
		/*
		 * Hardcoded grammar for the BNF as Java objects.
		 */
		
		// Define NonTerminal symbols
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
		
		// Define 128 rules for Char, corresponding to the ASCII values 0-127
		Rule [] charRules = new Rule [128];
		for (char i = 0; i <= 127; i++) {
			// Escape the character
			String s = strToBNF (Character.toString (i));
			charRules [i] = Rule.make (Terminal.make (s));
		}
		NonTerminal Char = new NonTerminal ("Char", charRules);
		
		// Make rules for alpha/alphanumeric chars (used for NonTerminal names)
		Rule [] AlphaRules = new Rule [52];
		Rule [] AlnumRules = new Rule [62];
		for (char i = 0; i < 26; i++) {
			AlnumRules [2 * i] = AlphaRules [2 * i] = Rule.make (Terminal.make (Character.toString ((char) ('a' + i))));
			AlnumRules [2 * i + 1] = AlphaRules [2 * i + 1] = Rule.make (Terminal.make (Character.toString ((char) ('A' + i))));
		}
		for (char i = 0; i < 10; i++) {
			AlnumRules [52 + i] = Rule.make (Terminal.make ("" + Character.toString ((char) ('0' + i))));
		}
		NonTerminal Alpha = new NonTerminal ("Alpha", AlphaRules);
		NonTerminal Alnum = new NonTerminal ("Alnum", AlnumRules);
		
		// Make rules for defining NonTerminals
		BNF.setRules (Rule.make (Def, BNF2));
		
		BNF2.setRules (Rule.make (Terminal.make ("\n"), Def, BNF2), Rule.make (Terminal.make ("\n"), BNF2), Rule.make ());
		Def.setRules (Rule.make (SpaceE, NTerm, SpaceE, Terminal.make ("::="), Rules));
		
		// Rules for defining rules
		Rules.setRules (Rule.make (Rule_, Rules2));
		Rules2.setRules (Rule.make (Terminal.make ("|"), Rule_, Rules2), Rule.make ());
		Rule_.setRules (Rule.make (SpaceE), Rule.make (SpaceE, Symbol, Rule_));
		// A Symbol is either a String or a NonTerminal
		Symbol.setRules (Rule.make (String), Rule.make (NTerm));
		String.setRules (Rule.make (Terminal.make ("\""), Chars, Terminal.make ("\"")));
		
		// Rules for Names & Strings
		Chars.setRules (Rule.make (Char, Chars), Rule.make ());
		NTerm.setRules (Rule.make (Terminal.make ("<"), Name, Terminal.make (">")));
		Name.setRules (Rule.make (Alpha), Rule.make (Alpha, Name2));
		Name2.setRules (Rule.make (Alnum, Name2), Rule.make ());
		SpaceE.setRules (Rule.make (Space), Rule.make ());
		Space.setRules (Rule.make (Terminal.make (" "), SpaceE), Rule.make (Terminal.make ("\t"), SpaceE), Rule.make (Terminal.make ("\r"), SpaceE));
		
		// Make Grammar object and store it in static field
		bnf = make (BNF, BNF2, Def, Rules, Rules2, Rule_, Symbol, String, Chars, Char, NTerm, Name, Name2, SpaceE, Space, Alpha, Alnum);
	}
}
