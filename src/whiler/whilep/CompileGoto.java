package whiler.whilep;

import java.util.ArrayList;
import java.util.List;

import whiler.gotop.Halt;
import whiler.gotop.Op;

public class CompileGoto {
	protected int tempVar;
	Program prog;
	
	private CompileGoto (Program prog) {
		this.prog = prog;
	}
	
	private whiler.gotop.Program doRun () {
		tempVar = prog.numVars;
		
		List<Op> op = new ArrayList<Op> ();
		
		prog.root.compileGoto (op, this);
		
		op.add (new Halt ());
		
		return new whiler.gotop.Program (op.toArray (new Op [0]));
	}
	
	public static whiler.gotop.Program run (Program prog) {
		return new CompileGoto(prog).doRun ();
	}
}
