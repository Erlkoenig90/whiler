package whiler;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import whiler.whilep.Parser;
import whiler.whilep.Interpreter;
import whiler.whilep.Sequence;

public class InterpreterMain {

	public static void main (String [] args) throws Exception {
		if (args.length < 2) {
			System.err.println ("Usage: java -cp ... InterpreterMain FileName ARG1 { ARGS }");
			System.exit (-1);
		}
		String prog = new String(Files.readAllBytes(Paths.get(args [0])), StandardCharsets.UTF_8);
		
	
		whiler.parser.Parser p = Parser.parse (prog);
		if (p == null) {
			System.out.println("Parse Error\n");
			System.exit (-1);
			return;
		}
		
		Sequence root = Parser.build (p);
		
		Interpreter ip = new Interpreter (root);
		
		BigInteger [] input = new BigInteger [args.length - 1];
		for (int i = 0; i < args.length - 1; i++) {
			input [i] = new BigInteger (args [i + 1]);
		}
		
		BigInteger output = ip.run (input);
		
		System.out.println (output);
		System.exit (0);
		
/*		StringBuilder sb = new StringBuilder ();
		p.getRoot().toParseTree(sb, 0);
		String parsedToStr = sb.toString ();
		System.out.println (parsedToStr); */
	}
}
