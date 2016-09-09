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

import org.junit.Before;
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
	whiler.gotop.JavaProg javaP;
	
	ProgRunner currentRunner;
	
	TestBase (String name) {
		this.name = name;
	}
	
	@Before
	public void prepare () throws Exception {
		String sourcecode = new String (Files.readAllBytes (Paths.get ("progs/" + name + ".while")), StandardCharsets.UTF_8);
		Parser p = whiler.whilep.Parser.parse (sourcecode);
		assertNotNull (p);
		whileP = whiler.whilep.Parser.build (p);
		wInt = new whiler.whilep.Interpreter (whileP);
		
		gotoP = whiler.whilep.CompileGoto.run (whileP);
		javaP = whiler.gotop.CompileJava.run ("whiler.test.Generated_" + name, gotoP);
	}
	
	@Test
	public void checkGotoSource () throws Exception {
		String sourcecode = new String (Files.readAllBytes (Paths.get ("progs/" + name + ".goto")), StandardCharsets.UTF_8);
		assertEquals (sourcecode, gotoP.toString ());
	}
	
	@Test
	public void testInterpreter () throws Exception {
		currentRunner = wInt;
		progtests ();
	}
	
	@Test
	public void testCompiled () throws Exception {
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
