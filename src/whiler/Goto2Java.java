package whiler;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import whiler.gotop.CompileJava;
import whiler.gotop.JavaProg;
import whiler.whilep.CompileGoto;
import whiler.whilep.Parser;
import whiler.whilep.Program;

class Goto2Java {

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println ("Usage: java -cp ... whiler.Goto2Java ClassName SourceFileName DestFileName");
			System.exit (1);
			return;
		}
		String sourcecode = new String(Files.readAllBytes(Paths.get(args [1])), StandardCharsets.UTF_8);
		
		whiler.parser.Parser p = Parser.parse (sourcecode);
		if (p == null) {
			System.out.println("Parse Error\n");
			System.exit (-1);
			return;
		}
		
		Program prog = Parser.build (p);
		
		whiler.gotop.Program gprog = CompileGoto.run (prog);
		
		JavaProg jprog = CompileJava.run (args [0], gprog);
		
		FileOutputStream fw = new FileOutputStream(args [2]);
		fw.write (jprog.getBinary ());
		fw.close ();
	}

}
