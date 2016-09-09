package whiler.gotop;

import java.math.BigInteger;

/**
 * Since thee defineClass method of the Java ClassLoader is protected, we define our own ClassLoader class that loads a
 * class compiled by JavaProg.
 */
class ClassLoader extends java.lang.ClassLoader {
	/**
	 * Load the JavaProg's class.
	 * @param prog	The compiled program (class) to load
	 * @return		The loaded class's "Class" instance
	 */
	public Class<?> defineClass (JavaProg prog) {
		return defineClass (prog.classNameOrig, prog.binary, 0, prog.binary.length);
	}
}

/**
 * Represents a compiled Goto Program. Contains a binary representation of the compiled .class file.
 */
public class JavaProg implements whiler.ProgRunner {
	/**
	 * Binary .class file contents
	 */
	byte [] binary;
	/**
	 * The internal class name using slashes
	 */
	String className;
	/**
	 * The external class name using dots
	 */
	String classNameOrig;
	/**
	 * After loading the class, contains its Class instance
	 */
	Class<?> klass;
	/**
	 * Only one ClassLoader is needed
	 */
	static final ClassLoader classLoader = new ClassLoader ();
	
	JavaProg (byte [] binary, String className, String classNameOrig) {
		this.binary = binary;
		this.className = className;
		this.classNameOrig = classNameOrig;
		this.klass = null;
	}
	/**
	 * @return	The binary contents of the class file
	 */
	public byte [] getBinary () {
		return binary;
	}
	/**
	 * If not already loaded, loads the class file into the current JVM.
	 * @return	The Class instance of the loaded class
	 */
	public Class<?> load () {
		if (klass == null) {
			klass = classLoader.defineClass (this);
		}
		return klass;
	}
	
	public BigInteger run (BigInteger [] input) throws Exception {
		return (BigInteger) load ().getMethod ("run", BigInteger [].class).invoke (null, new Object [] { input });
	}
}
