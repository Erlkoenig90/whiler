package whiler;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import whiler.whilep.Parser;
import whiler.whilep.Program;
import whiler.whilep.CompileGoto;

public class While2GotoMain {

	public static void main (String [] args) throws Exception {
		if (args.length != 2) {
			System.err.println ("Usage: java -cp ... InterpreterMain FileName FileName");
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
		
		whiler.gotop.Program gprog = CompileGoto.run (prog);
		
		FileWriter fw = new FileWriter(args [1]);
		fw.write(gprog.toString());
		fw.close ();
		
		System.exit (0);
	}
}
