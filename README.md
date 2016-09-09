# whiler - compiler & interpreter for WHILE programs
This is a simple java-based application/framework for dealing with WHILE & GOTO programs. These programs are written in a minimalistic programming language mainly for theoretical purposes.

The features are:
 * Parsing strings in any context-free grammar by using a minimalistic parser which works by simulating a nondeterministic stack machine via backtracking. See the comments in [the source](src/whiler/parser/Parser.java) for further explanations.
 * Usage of grammars specified in BNF
 * Parsing & Interpreting WHILE programs
 * Compiling WHILE programs into GOTO programs
 * Compiling GOTO programs into Java Bytecode and saving those as Java ".class" files by using the [ASM](http://asm.ow2.org) library. These can be run from any Java application or directly on the console. This is a true but simple compilation to the JVM without any interpreter.

This proves that it is possible to parse arbitrarily complex context-free grammars in a few hundred lines of code, and how WHILE & GOTO programs can serve as model languages for real-world ones.

The whole application was written to be as simple as possible. The parser does not use a tokenizer, which makes it quite slow and also restricts the numbers of characters that can be parsed (typically the 128 ASCII characters, but not the whole Unicode set, because that would require a huge grammar definition). The compilers do not perform any optimization or flow analysis. WHILE and GOTO programs are also very inefficient by design.

## Usage
The requirements are:

Library name | Version known to work | JAR file
-------------|-----------------------|---------
[JUnit](http://junit.org) | 4.12 | junit-4.12.jar
[Hamcrest-Core](http://hamcrest.org/JavaHamcrest)  (JUnit requirement) | 1.3 | hamcrest-core-1.3.jar
[ASM](http://asm.ow2.org) | 5.1 | asm-5.1.jar

For compiling & running whiler, these JAR's have to be in the classpath.

The repository contains an eclipse Java project which can be imported using File->Import. To use it, the aforementioned JAR files have to be added to the classpath (Right Click on the Project -> Properties -> Java Build Path -> Libraries) or just copied into the project directory. The project contains a few demo launch configurations that can be run directly from eclipse. There is also one for running a few unit tests. Eclipse allows to compile the project into a JAR file for stand-alone usage. To create the JAR file, right-click on the "whiler.jardesc" file and select "Create JAR". The JAR file does not include the unit tests, therefore it does not need the JUnit and hamcrest libraries. The "progs" directory contains a few example WHILE/GOTO programs.

whiler can also be run from the command line to access all of its functionality. For example, the following command runs the interpreter with the multiplication program:
```shell
java -jar whiler.jar RunWhile progs/mult.while 3 4
```
This prints the result "12".

The full syntax for the command line is:

```shell
java -jar whiler.jar Args...
```
where Args is:
* ParseBNF File
  
  Check syntax of a BNF file, and print canonical form of BNF
* Parse BNF-File File
  
  Parse a file with given syntax and print syntax tree
* RunWhile File Input...
  
  Interpret WHILE program from file and print result
* While2Goto InFile OutFile
  
  Compile WHILE program to GOTO program
* While2Java InFile ClassName OutFile
  
  Compile WHILE program to Java class
* RunJavaWhile InFile Input...
  
  Compile WHILE program to Java and run it
* ShowBNF
  
  Print the BNF of the grammar used to parse BNF's
* ShowWhile
  
  Print the BNF of the grammar used to parse while progams

OutFile can also be "-" to print to the console.

## Usage examples

Accessing the BNF for WHILE programs
```sh
$ java -jar whiler.jar ShowWhile > while.bnf
$ cat while.bnf
<File> ::= <SpaceE> <Sequence> <SpaceE>
<Sequence> ::= <Statement> | <Statement> <SpaceE> ";" <SpaceE> <Sequence>
<Statement> ::= <SetZero> | <Assign> | <Increment> | <ITE> | <Loop> | <While>
<SpaceE> ::= <Space> | 
<Space> ::= " " <SpaceE> | "\t" <SpaceE> | "\n" <SpaceE> | "\r" <SpaceE>
<SetZero> ::= <Var> <SpaceE> ":=" <SpaceE> "0"
<Assign> ::= <Var> <SpaceE> ":=" <SpaceE> <Var>
<Increment> ::= <Var> <SpaceE> ":=" <SpaceE> <Var> <SpaceE> "+" <SpaceE> "1"
<Compare> ::= <SpaceE> <Var> <SpaceE> "<" <SpaceE> <Var> <SpaceE>
<ITE> ::= "IF" <Compare> "THEN" <SpaceE> <Sequence> <SpaceE> "ELSE" <SpaceE> <Sequence> <SpaceE> "FI"
<Loop> ::= "LOOP" <SpaceE> <Var> <SpaceE> "DO" <SpaceE> <Sequence> <SpaceE> "OD"
<While> ::= "WHILE" <Compare> "DO" <SpaceE> <Sequence> <SpaceE> "OD"
<Var> ::= "X" <Digits>
<Digits> ::= <Digit> | <Digit> <Digits>
<Digit> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```
Parsing a simple WHILE program using the previously extracted grammar and printing the syntax tree:
```sh
$ cat test.while
X0:=0
$ java -jar whiler.jar Parse while.bnf test.while
<File> {
	<SpaceE> {
	}
	<Sequence> {
		<Statement> {
			<SetZero> {
				<Var> {
					"X"
					<Digits> {
						<Digit> {
							"0"
						}
					}
				}
				<SpaceE> {
				}
				":="
				<SpaceE> {
				}
				"0"
			}
		}
	}
	<SpaceE> {
		<Space> {
			"\n"
			<SpaceE> {
			}
		}
	}
}
```
Running the multiplication program in the interpreter:
```sh
$ cat progs/mult.while 
X0 := 0 ;
LOOP X1 DO
	LOOP X2 DO
		X0 := X0 + 1
	OD
OD
$ java -jar whiler.jar RunWhile progs/mult.while 3 4
12
```
Compiling the multiplication program into a GOTO program:
```sh
$ java -jar whiler.jar While2Goto progs/mult.while -
000:  IF X0 = 0 THEN GOTO 3
001:  X0 := X0 -1
002:  GOTO 0
003:  X3 := X1 +0
004:  IF X3 = 0 THEN GOTO 12
005:  X3 := X3 -1
006:  X4 := X2 +0
007:  IF X4 = 0 THEN GOTO 11
008:  X4 := X4 -1
009:  X0 := X0 +1
010:  GOTO 7
011:  GOTO 4
012:  HALT$ cat test.while 
X0:=0
```
Compiling the multiplication program into a Java program, analyzing the generated class file, and running it directly in Java:
```sh
$ java -jar whiler.jar While2Java progs/mult.while Mult progs/Mult.class
$ javap progs/Mult.class
public class Mult {
  static {};
  public static java.math.BigInteger run(java.math.BigInteger[]);
  public static void main(java.lang.String[]);
}
$ java -cp progs Mult 3 4
12
```

## License
This is an open source project licensed under the terms of the BSD license. See the [LICENSE file](LICENSE) for details.
