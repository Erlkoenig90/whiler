package whiler;

import whiler.grammar.Grammar;

public class Main {

	public static void main (String [] args) throws Exception {
		StringBuilder sb = new StringBuilder ();
		Grammar.bnf.toBNF (sb);
		String staticToStr = sb.toString ();
		
		System.out.println (staticToStr);
		
		Grammar g = Grammar.fromBNF (staticToStr);
		
		sb = new StringBuilder ();
		g.toBNF (sb);
		String parsedToStr = sb.toString ();
		System.out.println (parsedToStr);
		
		if (staticToStr.equals(parsedToStr))
			System.out.println ("Strings match!");
		else
			System.out.println ("Strings mismatch!");
	}
}
