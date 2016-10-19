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

package whiler.test;

import org.junit.Test;

import whiler.ProgRunner;
import whiler.parser.Parser;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class TestBase {
	whiler.whilep.Program whileP;
	whiler.gotop.Program gotoP;
	String gotoSource;
	String name;
	whiler.whilep.Interpreter wInt;
	whiler.gotop.Interpreter gInt;
	whiler.gotop.JavaProg javaP;
	
	ProgRunner currentRunner;
	
	Parser parsedWhile, parsedGoto;
	
	TestBase (String name) {
		this.name = name;
	}
	
	@Test
	public void runTests () throws Exception {
		t1_parseWhile ();
		t2_buildWhile ();
		t3_checkGoto ();
		t4_parseGoto ();
		t5_testWhileInt ();
		t6_testGotoInt ();
		t7_testCompiled ();
	}
	
	public void t1_parseWhile () throws Exception {
		String sourcecode = new String (Files.readAllBytes (Paths.get ("progs/" + name + ".while")), StandardCharsets.UTF_8);
		parsedWhile = whiler.whilep.Parser.parse (sourcecode);
		assertNotNull (parsedWhile);
	}
	
	public void t2_buildWhile () throws Exception {
		whileP = whiler.whilep.Parser.build (parsedWhile);
		wInt = new whiler.whilep.Interpreter (whileP);
		gotoP = whiler.whilep.CompileGoto.run (whileP);
		gInt = new whiler.gotop.Interpreter (gotoP);
		javaP = whiler.gotop.CompileJava.run ("whiler.test.Generated_" + name, gotoP);
	}
	
	public void t3_checkGoto () throws Exception {
		String sourcecode = new String (Files.readAllBytes (Paths.get ("progs/" + name + ".goto")), StandardCharsets.UTF_8);
		assertEquals (sourcecode, gotoP.toString ());
	}

	public void t4_parseGoto () throws Exception {
		// Convert Goto program to string, parse back, convert to string again, and compare
		parsedGoto = whiler.gotop.Parser.parse (gotoP.toString ());
		assertNotNull (parsedGoto);
		
		assertEquals (gotoP.toString (), whiler.gotop.Parser.build (parsedGoto).toString ()); 
	}
	
	public void t5_testWhileInt () throws Exception {
		currentRunner = wInt;
		progtests ();
	}
	
	public void t6_testGotoInt () throws Exception {
		currentRunner = gInt;
		progtests ();
	}
	
	public void t7_testCompiled () throws Exception {
		currentRunner = javaP;
		progtests ();
	}
	
	public void prog (int expRes, int... input) throws Exception {
		BigInteger [] bi = new BigInteger [input.length];
		for (int i = 0; i < bi.length; i++) {
			bi [i] = BigInteger.valueOf (input [i]);
		}
		BigInteger out = currentRunner.run (bi);
		assertEquals (0, out.compareTo (BigInteger.valueOf (expRes)));
	}
	
	abstract void progtests () throws Exception;
}
