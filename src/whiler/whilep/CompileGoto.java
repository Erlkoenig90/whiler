package whiler.whilep;

import java.util.ArrayList;
import java.util.List;

import whiler.gotop.Halt;
import whiler.gotop.Op;

/**
 * Compiles while programs into GOTO programs.
 */
public class CompileGoto {
	/**
	 * Contains the index of the first unused variable in the program. Is in/de-creased by the individual statement compilation functions in order to
	 * obtain temporary variables for control structures.
	 */
	protected int tempVar;
	/**
	 * The program to compile
	 */
	Program prog;
	
	private CompileGoto (Program prog) {
		this.prog = prog;
	}
	
	private whiler.gotop.Program doRun () {
		// Use the first unused variable as the first temporary one
		tempVar = prog.numVars;
		
		List<Op> op = new ArrayList<Op> ();
		
		// Compile everything
		prog.root.compileGoto (op, this);
		
		// Append HALT instruction
		op.add (new Halt ());
		
		// Make GOTO program instance
		return new whiler.gotop.Program (op.toArray (new Op [0]));
	}
	
	/**
	 * Performs the compilation of while programs
	 * @param prog	The while program to compile
	 * @return		The compiled GOTO program
	 */
	public static whiler.gotop.Program run (Program prog) {
		return new CompileGoto(prog).doRun ();
	}
}
