package whiler;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import whiler.whilep.Parser;
import whiler.whilep.Program;
import whiler.whilep.Interpreter;

public class InterpreterMain {

	public static void main (String [] args) throws Exception {
		if (args.length < 2) {
			System.err.println ("Usage: java -cp ... whiler.InterpreterMain FileName ARG1 { ARGS }");
			System.exit (-1);
		}
		String sourcecode = new String(Files.readAllBytes(Paths.get(args [0])), StandardCharsets.UTF_8);
		
	
		whiler.parser.Parser p = Parser.parse (sourcecode);
		if (p == null) {
			System.out.println("Parse Error\n");
			System.exit (-1);
			return;
		}
		
		Program prog = Parser.build (p);
		
		Interpreter ip = new Interpreter (prog);
		
		BigInteger [] input = new BigInteger [args.length - 1];
		for (int i = 0; i < args.length - 1; i++) {
			input [i] = new BigInteger (args [i + 1]);
		}
		
		BigInteger output = ip.run (input);
		
		System.out.println (output);
		System.exit (0);
	}
}
