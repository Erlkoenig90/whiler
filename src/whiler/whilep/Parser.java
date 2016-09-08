package whiler.whilep;

import java.util.ArrayList;
import java.util.List;

import whiler.grammar.Grammar;
import whiler.grammar.NonTerminal;
import whiler.parser.NTermInst;
import whiler.parser.SymbolInst;

public class Parser {
	public static Grammar grammar;
	static NonTerminal BNF_Statement;

	static {
		try {
			String bnf ="<File> ::= <SpaceE> <Sequence> <SpaceE>\n"
					+	"<Sequence> ::= <Statement> | <Statement> <SpaceE> \";\" <SpaceE> <Sequence>\n"
					+	"<Statement> ::= <SetZero> | <Assign> | <Increment> | <ITE> | <Loop> | <While>\n"
					+	"<SpaceE> ::= <Space> | \n"
					+	"<Space> ::= \" \" <SpaceE> | \"\\t\" <SpaceE> | \"\\n\" <SpaceE> | \"\\r\" <SpaceE>\n"
					+	"<SetZero> ::= <Var> <SpaceE> \":=\" <SpaceE> \"0\"\n"
					+	"<Assign> ::= <Var> <SpaceE> \":=\" <SpaceE> <Var>\n"
					+	"<Increment> ::= <Var> <SpaceE> \":=\" <SpaceE> <Var> <SpaceE> \"+\" <SpaceE> \"1\"\n"
					+	"<Compare> ::= <SpaceE> <Var> <SpaceE> \"<\" <SpaceE> <Var> <SpaceE>\n"
					+	"<ITE> ::= \"IF\" <Compare> \"THEN\" <SpaceE> <Sequence> <SpaceE> \"ELSE\" <SpaceE> <Sequence> <SpaceE> \"FI\"\n"
					+	"<Loop> ::= \"LOOP\" <SpaceE> <Var> <SpaceE> \"DO\" <SpaceE> <Sequence> <SpaceE> \"OD\"\n"
					+	"<While> ::= \"WHILE\" <Compare> \"DO\" <SpaceE> <Sequence> <SpaceE> \"OD\"\n"
//					+	"<Concat> ::= <Statement> <SpaceE> \";\" <SpaceE> <Statement>\n"
					+	"<Var> ::= \"X\" <Digits>\n"
					+	"<Digits> ::= <Digit> | <Digit> <Digits>\n"
					+	"<Digit> ::= \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\"\n";
			grammar = Grammar.fromBNF (bnf);
			BNF_Statement = grammar.get ("Statement");
		} catch (Exception e) {}
	}
	
	public static whiler.parser.Parser parse (String prog) {
		return whiler.parser.Parser.parse(grammar, prog);
	}
	private static int getVar (SymbolInst varSym, whiler.parser.Parser p) {
		return Integer.parseInt (((NTermInst) varSym).getChild (1).collectString (p));
	}
	
	private static Sequence buildSequence (NTermInst tree, whiler.parser.Parser p) throws Exception {
		List<NTermInst> statementTrees = new ArrayList<NTermInst> ();
		tree.collectNonTerminals(statementTrees, BNF_Statement);
		Statement [] statements = new Statement [statementTrees.size ()];
		for (int i = 0; i < statementTrees.size (); i++) {
			Statement s;
			NTermInst c = (NTermInst) statementTrees.get (i).getChild (0);
			switch (statementTrees.get(i).getAppliedRule ()) {
				case 0:
					s = new SetZero (getVar (c.getChild (0), p));
					break;
				case 1:
					s = new Assign (getVar (c.getChild (0), p), getVar (c.getChild (4), p));
					break;
				case 2:
					{
						int v1 = getVar (c.getChild (0), p);
						int v2 = getVar (c.getChild (4), p);
						if (v1 != v2)
							throw new Exception ("Addition+Increment not allowed!");
						s = new Increment (v1);
					}
					break;
				case 3:
					{
						int v1 = getVar (((NTermInst) c.getChild (1)).getChild (1), p);
						int v2 = getVar (((NTermInst) c.getChild (1)).getChild (5), p);
						if (v1 == v2)
							throw new Exception (String.format("Comparing variable %d with itself!", v1));
						s = new If (v1, v2, buildSequence ((NTermInst) c.getChild(4), p), buildSequence ((NTermInst) c.getChild (8), p));
					}
					break;
				case 4:
					s = new Loop (getVar (c.getChild(2), p), buildSequence ((NTermInst) c.getChild(6), p));
					break;
				case 5:
					{
						int v1 = getVar (((NTermInst) c.getChild (1)).getChild (1), p);
						int v2 = getVar (((NTermInst) c.getChild (1)).getChild (5), p);
						if (v1 == v2)
							throw new Exception (String.format("Comparing variable %d with itself!", v1));
						s = new While (v1, v2, buildSequence ((NTermInst) c.getChild (4), p));
					}
					break;
				default:
					throw new Exception ("Parser bug");
			}
			statements [i] = s;
		}
		return new Sequence (statements);
	}
	public static Program build (whiler.parser.Parser parsed) throws Exception {
		Sequence root = buildSequence (parsed.getRoot (), parsed);
		
		return new Program (root);
	}
}
