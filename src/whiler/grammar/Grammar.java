package whiler.grammar;

public class Grammar {
	protected NonTerminal [] productions;
	
	public Grammar (NonTerminal [] productions) {
		this.productions = productions;
	}
	
	public NonTerminal root () { return productions [0]; }
	
	public static Grammar make (NonTerminal... productions) {
		return new Grammar (productions);
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
			else if (c >= 32 && c <= 126)
				sb.append(c);
			else
				sb.append(String.format("\\x%02x", (int) c));
		}
		return sb.toString ();
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

/*

<BNF> ::= <Def> <BNF2>
<BNF2> ::= "\n" <Def> <BNF2> |  | "\n" <BNF2>
<Def> ::= <SpaceE> <NTerm> <SpaceE> "::=" <Rules>
<Rules> ::= <Rule> <Rules2>
<Rules2> ::= "|" <Rule> <Rules2> | 
<Rule> ::= <SpaceE> | <SpaceE> <Symbol> <Rule>
<Symbol> ::= <String> | <NTerm>
<String> ::= "\"" <Chars> "\""
<Chars> ::=  | <Char> <Chars>
<Char> ::= "\\x00" | "\\x01" | "\\x02" | "\\x03" | "\\x04" | "\\x05" | "\\x06" | "\\a" | "\\b" | "\\t" | "\\n" | "\\v" | "\\f" | "\\r" | "\\x0E" | "\\x0F" | "\\x10" | "\\x11" | "\\x12" | "\\x13" | "\\x14" | "\\x15" | "\\x16" | "\\x17" | "\\x18" | "\\x19" | "\\x1A" | "\\e" | "\\x1C" | "\\x1D" | "\\x1E" | "\\x1F" | " " | "!" | "\\\"" | "#" | "$" | "%" | "&" | "'" | "(" | ")" | "*" | "+" | "," | "-" | "." | "/" | "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" | ":" | ";" | "<" | "=" | ">" | "?" | "@" | "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z" | "[" | "\\\\" | "]" | "^" | "_" | "`" | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z" | "{" | "|" | "}" | "~" | "\\x7F" | "\\x80" | "\\x81" | "\\x82" | "\\x83" | "\\x84" | "\\x85" | "\\x86" | "\\x87" | "\\x88" | "\\x89" | "\\x8A" | "\\x8B" | "\\x8C" | "\\x8D" | "\\x8E" | "\\x8F" | "\\x90" | "\\x91" | "\\x92" | "\\x93" | "\\x94" | "\\x95" | "\\x96" | "\\x97" | "\\x98" | "\\x99" | "\\x9A" | "\\x9B" | "\\x9C" | "\\x9D" | "\\x9E" | "\\x9F" | "\\xA0" | "\\xA1" | "\\xA2" | "\\xA3" | "\\xA4" | "\\xA5" | "\\xA6" | "\\xA7" | "\\xA8" | "\\xA9" | "\\xAA" | "\\xAB" | "\\xAC" | "\\xAD" | "\\xAE" | "\\xAF" | "\\xB0" | "\\xB1" | "\\xB2" | "\\xB3" | "\\xB4" | "\\xB5" | "\\xB6" | "\\xB7" | "\\xB8" | "\\xB9" | "\\xBA" | "\\xBB" | "\\xBC" | "\\xBD" | "\\xBE" | "\\xBF" | "\\xC0" | "\\xC1" | "\\xC2" | "\\xC3" | "\\xC4" | "\\xC5" | "\\xC6" | "\\xC7" | "\\xC8" | "\\xC9" | "\\xCA" | "\\xCB" | "\\xCC" | "\\xCD" | "\\xCE" | "\\xCF" | "\\xD0" | "\\xD1" | "\\xD2" | "\\xD3" | "\\xD4" | "\\xD5" | "\\xD6" | "\\xD7" | "\\xD8" | "\\xD9" | "\\xDA" | "\\xDB" | "\\xDC" | "\\xDD" | "\\xDE" | "\\xDF" | "\\xE0" | "\\xE1" | "\\xE2" | "\\xE3" | "\\xE4" | "\\xE5" | "\\xE6" | "\\xE7" | "\\xE8" | "\\xE9" | "\\xEA" | "\\xEB" | "\\xEC" | "\\xED" | "\\xEE" | "\\xEF" | "\\xF0" | "\\xF1" | "\\xF2" | "\\xF3" | "\\xF4" | "\\xF5" | "\\xF6" | "\\xF7" | "\\xF8" | "\\xF9" | "\\xFA" | "\\xFB" | "\\xFC" | "\\xFD" | "\\xFE" | "\\xFF"
<NTerm> ::= "<" <Name> ">"
<Name> ::= <Alpha> | <Alpha> <Name2>
<Name2> ::= <Alnum> <Name2> | 
<SpaceE> ::= <Space> | 
<Space> ::= " " <SpaceE> | "\t" <SpaceE> | "\r" <SpaceE>
<Alpha> ::= "A" | "a" | "B" | "b" | "C" | "c" | "D" | "d" | "E" | "e" | "F" | "f" | "G" | "g" | "H" | "h" | "I" | "i" | "J" | "j" | "K" | "k" | "L" | "l" | "M" | "m" | "N" | "n" | "O" | "o" | "P" | "p" | "Q" | "q" | "R" | "r" | "S" | "s" | "T" | "t" | "U" | "u" | "V" | "v" | "W" | "w" | "X" | "x" | "Y" | "y" | "Z" | "z"
<Alnum> ::= "A" | "a" | "B" | "b" | "C" | "c" | "D" | "d" | "E" | "e" | "F" | "f" | "G" | "g" | "H" | "h" | "I" | "i" | "J" | "j" | "K" | "k" | "L" | "l" | "M" | "m" | "N" | "n" | "O" | "o" | "P" | "p" | "Q" | "q" | "R" | "r" | "S" | "s" | "T" | "t" | "U" | "u" | "V" | "v" | "W" | "w" | "X" | "x" | "Y" | "y" | "Z" | "z" | "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"




@@bnf ||= Grammar.new(["BNF", "BNF2", "Def", "Rules", "Rules2", "Rule", "Symbol", "String", "Chars", "Char", "NTerm", "Name", "Name2", "SpaceE", "Space", "Alpha", "Alnum"]) { |g|
g["BNF"] <<  [g["Def"], g["BNF2"]]
g["BNF2"].rules = [["\n", g["Def"], g["BNF2"]], [], ["\n", g["BNF2"]]]
g["Def"] << [g["SpaceE"], g["NTerm"], g["SpaceE"], "::=", g["Rules"]]
g["Rules"] << [g["Rule"], g["Rules2"]]
g["Rules2"].rules= [["|", g["Rule"], g["Rules2"]], []]
g["Rule"] << [g["SpaceE"]]
g["Rule"] << [g["SpaceE"], g["Symbol"], g["Rule"]]

g["Symbol"].rules = [[g["String"]], [g["NTerm"]]]
g["String"] << ["\"", g["Chars"], "\""]
g["Chars"] << []
g["Chars"] << [g["Char"], g["Chars"]]
for i in 0..255 do
	g["Char"] << [[i].pack("C").inspect[1..-2]]
end

g["NTerm"] << ["<", g["Name"], ">"]
g["Name"] << [g["Alpha"]]
g["Name"] << [g["Alpha"], g["Name2"]]
g["Name2"] << [g["Alnum"], g["Name2"]]
g["Name2"] << []

g["SpaceE"] << [g["Space"]]
g["SpaceE"] << []
g["Space"] << [" ",  g["SpaceE"]]
g["Space"] << ["\t", g["SpaceE"]]
g["Space"] << ["\r", g["SpaceE"]]

for i in 0..25 do
	a = [[[65 + i].pack("C")], [[97 + i].pack("C")]]
	g["Alpha"].rules.concat(a)
	g["Alnum"].rules.concat(a)
end
for i in 0..9 do
	g["Alnum"] << [[48 + i].pack("C")]
end
g.start = g["BNF"]
}



*/