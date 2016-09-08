package whiler.whilep;
import java.util.List;

import whiler.gotop.Op;

public abstract class Statement {
	public abstract int getMaxVar ();
	protected abstract void run (Interpreter ip);
	protected abstract void compileGoto (List<Op> op, CompileGoto c);
}
