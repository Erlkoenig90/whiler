package whiler.whilep;

public abstract class Statement {
	public abstract int getMaxVar ();
	protected abstract void run (Interpreter ip);
}
