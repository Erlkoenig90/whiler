package whiler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import whiler.grammar.Grammar;
import whiler.grammar.NonTerminal;
import whiler.grammar.Rule;
import whiler.grammar.Terminal;
import whiler.parser.NTermInst;
import whiler.parser.Parser;

public class Main {

	public static void main (String [] args) throws IOException {
		StringBuilder sb = new StringBuilder ();
		Grammar.bnf.toBNF (sb);
		String str = sb.toString ();
		
		System.out.println ("Parsing String: " + str);
		NTermInst tree = Parser.parse (Grammar.bnf, str);
		if (tree == null)
			System.out.println ("Parse Error");
		else {
			sb = new StringBuilder ();
			tree.toString (sb, 0);
			System.out.println (sb);
		}
	}
}
