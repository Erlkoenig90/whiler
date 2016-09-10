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

package whiler.whilep;

import java.util.ArrayList;
import java.util.List;

import whiler.grammar.Grammar;
import whiler.grammar.NonTerminal;
import whiler.parser.NTermInst;
import whiler.parser.SymbolInst;

public class Parser {
	/**
	 * The grammar for while programs
	 */
	public static final Grammar grammar;
	/**
	 * The NonTerminal symbol for statements.
	 */
	private static final NonTerminal BNF_Statement;

	static {
		Grammar g;
		NonTerminal s;
		try {
			// The BNF of the grammar for while programs
			String bnf ="<File> ::= <SpaceE> <Sequence> <SpaceE>\n"
					+	"<Sequence> ::= <Statement> | <Statement> <SpaceE> \";\" <SpaceE> <Sequence>\n"
					+	"<Statement> ::= <SetZero> | <Assign> | <Increment> | <ITE> | <Loop> | <While>\n"
					+	"<SpaceE> ::= <Space> | \n"
					+	"<Space> ::= \" \" <SpaceE> | \"\\t\" <SpaceE> | \"\\n\" <SpaceE> | \"\\r\" <SpaceE>\n"
					+	"<SetZero> ::= <Var> <SpaceE> \":=\" <SpaceE> \"0\"\n"
					+	"<Assign> ::= <Var> <SpaceE> \":=\" <SpaceE> <Var>\n"
					+	"<Increment> ::= <Var> <SpaceE> \":=\" <SpaceE> <Var> <SpaceE> \"+\" <SpaceE> \"1\"\n"
					+	"<Compare> ::= <Space> <Var> <SpaceE> \"<\" <SpaceE> <Var> <Space>\n"
					+	"<ITE> ::= \"IF\" <Compare> \"THEN\" <Space> <Sequence> <Space> \"ELSE\" <Space> <Sequence> <Space> \"FI\"\n"
					+	"<Loop> ::= \"LOOP\" <Space> <Var> <Space> \"DO\" <Space> <Sequence> <Space> \"OD\"\n"
					+	"<While> ::= \"WHILE\" <Compare> \"DO\" <Space> <Sequence> <Space> \"OD\"\n"
//					+	"<Concat> ::= <Statement> <SpaceE> \";\" <SpaceE> <Statement>\n"
					+	"<Var> ::= \"X\" <Digits>\n"
					+	"<Digits> ::= <Digit> | <Digit> <Digits>\n"
					+	"<Digit> ::= \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\"\n";
			g = Grammar.fromBNF (bnf);
			s = g.get ("Statement");
		} catch (Exception e) {
			g = null;
			s = null;
			System.exit (-1);
		}
		grammar = g;
		BNF_Statement = s;
	}
	/**
	 * Parse a while program.
	 * @param prog		The source code
	 * @return			The compiled program
	 */
	public static whiler.parser.Parser parse (String prog) {
		return whiler.parser.Parser.parse(grammar, prog);
	}
	/**
	 * Given a NTermInst referring to a &lt;Var&gt;, return the variable index
	 */
	private static int getVar (SymbolInst varSym, whiler.parser.Parser p) {
		return Integer.parseInt (((NTermInst) varSym).getChild (1).collectString (p));
	}
	/**
	 * Recursively evaluate a syntax tree of a while program, and construct java structures
	 * @param tree			Branch of the syntax tree containing a sequence
	 * @return				Parsed Sequence of Statements
	 */
	private static Sequence buildSequence (NTermInst tree, whiler.parser.Parser p) throws Exception {
		// Collect statements
		List<NTermInst> statementTrees = new ArrayList<NTermInst> ();
		tree.collectNonTerminals(statementTrees, BNF_Statement);
		Statement [] statements = new Statement [statementTrees.size ()];
		for (int i = 0; i < statementTrees.size (); i++) {
			Statement s;
			NTermInst c = (NTermInst) statementTrees.get (i).getChild (0);
			
			// Parse different kinds of statements
			switch (statementTrees.get(i).getAppliedRule ()) {
				case 0:
					// Parse Xi := 0
					s = new SetZero (getVar (c.getChild (0), p));
					break;
				case 1:
					// Parse Xi := Xj
					s = new Assign (getVar (c.getChild (0), p), getVar (c.getChild (4), p));
					break;
				case 2:
					{
						// Parse Xi := Xi + 1
						int v1 = getVar (c.getChild (0), p);
						int v2 = getVar (c.getChild (4), p);
						if (v1 != v2)
							throw new Exception ("Addition+Increment not allowed!");
						s = new Increment (v1);
					}
					break;
				case 3:
					{
						// Parse If-Then-Else
						int v1 = getVar (((NTermInst) c.getChild (1)).getChild (1), p);
						int v2 = getVar (((NTermInst) c.getChild (1)).getChild (5), p);
						if (v1 == v2)
							throw new Exception (String.format("Comparing variable %d with itself!", v1));
						s = new If (v1, v2, buildSequence ((NTermInst) c.getChild(4), p), buildSequence ((NTermInst) c.getChild (8), p));
					}
					break;
				case 4:
					// Parse Loop
					s = new Loop (getVar (c.getChild(2), p), buildSequence ((NTermInst) c.getChild(6), p));
					break;
				case 5:
					{
						// Parse While
						int v1 = getVar (((NTermInst) c.getChild (1)).getChild (1), p);
						int v2 = getVar (((NTermInst) c.getChild (1)).getChild (5), p);
						if (v1 == v2)
							throw new Exception (String.format("Comparing variable %d with itself!", v1));
						s = new While (v1, v2, buildSequence ((NTermInst) c.getChild (4), p));
					}
					break;
				default:
					// Should not happen.
					throw new Exception ("Parser bug");
			}
			statements [i] = s;
		}
		return new Sequence (statements);
	}
	/**
	 * Builds a while program in java structures out of a parsed syntax tree.
	 * @param parsed		The parser containing the syntax tree
	 * @return				The constructed program
	 */
	public static Program build (whiler.parser.Parser parsed) throws Exception {
		Sequence root = buildSequence (parsed.getRoot (), parsed);
		
		return new Program (root);
	}
}
