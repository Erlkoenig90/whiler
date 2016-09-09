package whiler.gotop;

import java.math.BigInteger;

class ClassLoader extends java.lang.ClassLoader {
	public Class<?> defineClass (JavaProg prog) {
		return defineClass (prog.classNameOrig, prog.binary, 0, prog.binary.length);
	}
}

public class JavaProg {
	byte [] binary;
	String className, classNameOrig;
	Class<?> klass;
	static final ClassLoader classLoader = new ClassLoader ();
	
	JavaProg (byte [] binary, String className, String classNameOrig) {
		this.binary = binary;
		this.className = className;
		this.classNameOrig = classNameOrig;
		this.klass = null;
	}
	public byte [] getBinary () {
		return binary;
	}
	public Class<?> load () {
		if (klass == null) {
			klass = classLoader.defineClass (this);
		}
		return klass;
	}
	public BigInteger run (BigInteger [] input) throws Exception {
		return (BigInteger) load ().getMethod("run", BigInteger [].class).invoke (null, new Object [] { input });
	}
}
