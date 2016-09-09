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
