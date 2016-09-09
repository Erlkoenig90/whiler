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
