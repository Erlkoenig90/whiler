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

package whiler.gotop;

import whiler.grammar.Grammar;
import whiler.grammar.NonTerminal;
import whiler.parser.NTermInst;
import whiler.parser.SymbolInst;

import java.math.BigInteger;
import java.util.ArrayList;

import whiler.gotop.Program;

public class Parser {
	/**
	 * The grammar for GOTO programs
	 */
	public static final Grammar grammar;
	/**
	 * The NonTerminal symbol for operations.
	 */
	private static final NonTerminal BNF_Op;
	static {
		Grammar g;
		NonTerminal o;
		try {
			// The BNF of the grammar for goto programs
			String bnf ="<File> ::= <Sequence> \n"
					+	"<Sequence> ::= <LineNo> <SpaceE> <Op> <SpaceE> | <LineNo> <SpaceE> <Op> <SpaceE> \"\\n\" <Sequence> | <SpaceE> \"\\n\"\n"
					+	"<LineNo> ::= <Digits> <SpaceE> \":\" | \n"
					+	"<Op> ::= <Assign> | <Goto> | <Halt> | <If>\n"
					+	"<SpaceE> ::= <Space> | \n"
					+	"<Space> ::= \" \" <SpaceE> | \"\\t\" <SpaceE> | \"\\r\" <SpaceE>\n"
					+	"<Assign> ::= <Var> <SpaceE> \":=\" <SpaceE> <Var> <SpaceE> <Offset>\n"
					+	"<Offset> ::= \"+\" <SpaceE> <Digits> | \"-\" <SpaceE> <Digits> | \n"
					+	"<Goto> ::= \"GOTO\" <Space> <Digits> \n"
					+	"<Halt> ::= \"HALT\"\n"
					+	"<If> ::= \"IF\" <Space> <Var> <SpaceE> \"=\" <SpaceE> \"0\" <Space> \"THEN\" <Space> \"GOTO\" <Space> <Digits>\n"
					+	"<Var> ::= \"X\" <Digits>\n"
					+	"<Digits> ::= <Digit> | <Digit> <Digits>\n"
					+	"<Digit> ::= \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\"\n";
			g = Grammar.fromBNF (bnf);
			o = g.get ("Op");
		} catch (Exception e) {
			g = null;
			o = null;
			System.exit (-1);
		}
		grammar = g;
		BNF_Op = o;
	}
	
	/**
	 * Parse a GOTO program.
	 * 
	 * @param prog
	 *            The source code
	 * @return The compiled program
	 */
	public static whiler.parser.Parser parse (String prog) {
		return whiler.parser.Parser.parse (grammar, prog);
	}
	
	/**
	 * Given a NTermInst referring to a &lt;Var&gt;, return the variable index
	 */
	private static int getVar (SymbolInst varSym, whiler.parser.Parser p) {
		return Integer.parseInt (((NTermInst) varSym).getChild (1).collectString (p));
	}
	
	/**
	 * Builds a GOTO program in java structures out of a parsed syntax tree.
	 * 
	 * @param parsed
	 *            The parser containing the syntax tree
	 * @return The constructed program
	 */
	public static Program build (whiler.parser.Parser parsed) throws Exception {
		// Collect all operations
		ArrayList<NTermInst> statTrees = new ArrayList<NTermInst> ();
		parsed.getRoot ().collectNonTerminals (statTrees, BNF_Op);
		
		Op [] ops = new Op [statTrees.size ()];
		
		// Iterate & build operations
		for (int i = 0; i < ops.length; i++) {
			NTermInst ot = (NTermInst) statTrees.get (i).getChild (0);
			Op op;
			switch (statTrees.get (i).getAppliedRule ()) {
				case 0: {
					// Assignment
					
					// Get Offset
					NTermInst offtree = (NTermInst) ot.getChild (6);
					int rule = offtree.getAppliedRule ();
					BigInteger off;
					switch (rule) {
						case 0:
							// Positive
							off = new BigInteger (offtree.getChild (2).collectString (parsed));
							break;
						case 1:
							// Negative
							off = new BigInteger (offtree.getChild (2).collectString (parsed)).negate ();
							break;
						case 2:
							// No Offset (assume zero)
							off = BigInteger.ZERO;
						default:
							// Can't happen
							throw new Exception ();
					}
					op = new Assign (getVar (ot.getChild (0), parsed), getVar (ot.getChild (4), parsed), off);
					break;
				}
				case 1:
					// Goto
					op = new Goto (Integer.parseInt (ot.getChild (2).collectString (parsed)));
					break;
				case 2:
					// Halt
					op = new Halt ();
					break;
				case 3:
					// If
					op = new If (Integer.parseInt (ot.getChild (12).collectString (parsed)), getVar (ot.getChild (2), parsed));
					break;
				default:
					// Can't happen
					throw new Exception ();
			}
			ops [i] = op;
		}
		
		return new Program (ops);
	}
}
