package whiler.gotop;

public abstract class Op {
	abstract public void toString (StringBuilder sb);
	public abstract int getMaxVar ();
	protected abstract void compileJava (CompileJava c);
}
