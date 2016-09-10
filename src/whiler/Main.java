/*
 * Copyright (c) 2016, Niklas Gürtler
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package whiler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import whiler.gotop.CompileJava;
import whiler.gotop.JavaProg;
import whiler.grammar.Grammar;
import whiler.parser.Parser;
import whiler.whilep.CompileGoto;

/**
 * This is the main entry point for console usage
 */
public class Main {
	static private void usage () throws URISyntaxException {
		String jarpath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		
		System.out.print (	"Usage: java -cp " + jarpath + " whiler.Main Args...\n"
						+	"  where Args is:\n"
						+	"    ParseBNF File\n"
						+   "      - Check syntax of a BNF file, and print canonical form of BNF\n"
						+	"    Parse BNF-File File\n"
						+   "      - Parse a file with given syntax and print syntax tree\n"
						+   "    RunWhile File Input...\n"
						+   "      - Interpret WHILE program from file and print result\n"
						+	"    RunGoto File Input...\n"
						+   "      - Interpret GOTO program from file and print result\n"
						+   "    While2Goto InFile OutFile\n"
						+   "      - Compile WHILE program to GOTO program\n"
						+   "    Goto2Java InFile ClassName OutFile\n"
						+   "      - Compile GOTO program to Java class\n"
						+   "    While2Java InFile ClassName OutFile\n"
						+   "      - Compile WHILE program to Java class\n"
						+   "    RunJavaWhile InFile Input...\n"
						+   "      - Compile WHILE program to Java and run it\n"
						+	"    ShowBNF\n"
						+   "      - Print the BNF of the grammar used to parse BNF's\n"
						+	"    ShowWhile\n"
						+   "      - Print the BNF of the grammar used to parse while progams\n"
						+	"    ShowGoto\n"
						+   "      - Print the BNF of the grammar used to parse goto progams\n\n"
						+   "  OutFile can also be \"-\" to print to the console.\n"
		);
		System.exit (1);
	}
	
	static private String readfile (String filename) throws IOException {
		return new String (Files.readAllBytes (Paths.get (filename)), StandardCharsets.UTF_8);
	}
	
	static private void writefile (String filename, String text) throws IOException {
		if (filename.equals ("-"))
			System.out.print (text);
		else
			try (FileOutputStream fos = new FileOutputStream (filename)) {
				fos.write (text.getBytes (StandardCharsets.UTF_8));
			}
	}
	
	static private void writefile (String filename, byte [] data) throws IOException {
		if (filename.equals ("-"))
			System.out.write (data);
		else
			try (FileOutputStream fos = new FileOutputStream (filename)) {
				fos.write (data);
			}
	}
	
	public static void main (String [] args) throws Exception {
		if (args.length < 1) {
			usage ();
			return;
		}
		
		switch (args [0]) {
			case "ParseBNF": {
				if (args.length != 2) {
					usage ();
					return;
				}
				Grammar g = Grammar.fromBNF (readfile (args [1]));
				if (g == null) {
					System.out.println ("Syntax error");
					System.exit (1);
				} else {
					StringBuilder sb = new StringBuilder ();
					g.toBNF (sb);
					System.out.println (sb);
					
					System.exit (0);
				}
				break;
			}
			case "Parse": {
				if (args.length != 3) {
					usage ();
					return;
				}
				Grammar g = Grammar.fromBNF (readfile (args [1]));
				if (g == null) {
					System.out.println ("Syntax error in BNF");
					System.exit (1);
					return;
				}
				Parser p = Parser.parse (g, readfile (args [2]));
				if (p == null) {
					System.out.println ("Syntax error in source file");
					System.exit (1);
					return;
				} else {
					StringBuilder sb = new StringBuilder ();
					p.getRoot ().toParseTree (sb);
					System.out.print (sb);
					System.exit (0);
					return;
				}
			}
			case "RunWhile": {
				if (args.length < 2) {
					usage ();
					return;
				}
				Parser p = whiler.whilep.Parser.parse (readfile (args [1]));
				if (p == null) {
					System.out.println ("Syntax error in WHILE source file");
					System.exit (1);
					return;
				}
				
				whiler.whilep.Program prog = whiler.whilep.Parser.build (p);
				
				whiler.whilep.Interpreter ip = new whiler.whilep.Interpreter (prog);
				
				BigInteger [] input = new BigInteger [args.length - 2];
				for (int i = 0; i < args.length - 2; i++) {
					input [i] = new BigInteger (args [i + 2]);
				}
				
				BigInteger output = ip.run (input);
				
				System.out.println (output);
				System.exit (0);
				break;
			}
			case "RunGoto": {
				if (args.length < 2) {
					usage ();
					return;
				}
				Parser p = whiler.gotop.Parser.parse (readfile (args [1]));
				if (p == null) {
					System.out.println ("Syntax error in GOTO source file");
					System.exit (1);
					return;
				}
				
				whiler.gotop.Program prog = whiler.gotop.Parser.build (p);
				
				whiler.gotop.Interpreter ip = new whiler.gotop.Interpreter (prog);
				
				BigInteger [] input = new BigInteger [args.length - 2];
				for (int i = 0; i < args.length - 2; i++) {
					input [i] = new BigInteger (args [i + 2]);
				}
				
				BigInteger output = ip.run (input);
				
				System.out.println (output);
				System.exit (0);
				break;
			}
			case "While2Goto": {
				if (args.length != 3) {
					usage ();
					return;
				}
				Parser p = whiler.whilep.Parser.parse (readfile (args [1]));
				if (p == null) {
					System.out.println ("Syntax error in WHILE source file");
					System.exit (1);
					return;
				}
				
				whiler.whilep.Program prog = whiler.whilep.Parser.build (p);
				whiler.gotop.Program gprog = CompileGoto.run (prog);
				writefile (args [2], gprog.toString ());
				System.exit (0);
				return;
			}
			case "Goto2Java": {
				if (args.length != 4) {
					usage ();
					return;
				}
				Parser p = whiler.gotop.Parser.parse (readfile (args [1]));
				if (p == null) {
					System.out.println ("Syntax error in WHILE source file");
					System.exit (1);
					return;
				}
				
				whiler.gotop.Program prog = whiler.gotop.Parser.build (p);
				
				JavaProg jprog = CompileJava.run (args [2], prog);
				writefile (args [3], jprog.getBinary ());
				System.exit (0);
				break;
			}
			case "While2Java": {
				if (args.length != 4) {
					usage ();
					return;
				}
				Parser p = whiler.whilep.Parser.parse (readfile (args [1]));
				if (p == null) {
					System.out.println ("Syntax error in WHILE source file");
					System.exit (1);
					return;
				}
				
				whiler.whilep.Program prog = whiler.whilep.Parser.build (p);
				whiler.gotop.Program gprog = CompileGoto.run (prog);
				
				JavaProg jprog = CompileJava.run (args [2], gprog);
				writefile (args [3], jprog.getBinary ());
				System.exit (0);
				break;
			}
			case "RunJavaWhile": {
				if (args.length < 2) {
					usage ();
					return;
				}
				Parser p = whiler.whilep.Parser.parse (readfile (args [1]));
				if (p == null) {
					System.out.println ("Syntax error in WHILE source file");
					System.exit (1);
					return;
				}
				
				whiler.whilep.Program prog = whiler.whilep.Parser.build (p);
				whiler.gotop.Program gprog = CompileGoto.run (prog);
				
				JavaProg jprog = CompileJava.run ("whiler.GeneratedGoto", gprog);
				
				BigInteger [] input = new BigInteger [args.length - 2];
				for (int i = 0; i < args.length - 2; i++) {
					input [i] = new BigInteger (args [i + 2]);
				}
				
				BigInteger output = jprog.run (input);
				System.out.println (output);
				System.exit (0);
				break;
			}
			case "ShowBNF": {
				if (args.length != 1) {
					usage ();
					return;
				}
				StringBuilder sb = new StringBuilder ();
				Grammar.bnf.toBNF (sb);
				System.out.println (sb);
				System.exit (0);
				break;
			}
			case "ShowWhile": {
				if (args.length != 1) {
					usage ();
					return;
				}
				StringBuilder sb = new StringBuilder ();
				whiler.whilep.Parser.grammar.toBNF (sb);
				System.out.println (sb);
				System.exit (0);
				break;
			}
			case "ShowGoto": {
				if (args.length != 1) {
					usage ();
					return;
				}
				StringBuilder sb = new StringBuilder ();
				whiler.gotop.Parser.grammar.toBNF (sb);
				System.out.println (sb);
				System.exit (0);
				break;
			}
			default:
				usage ();
				System.exit (1);
		}
	}
}
