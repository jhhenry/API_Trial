package org.henry.antlr.initArray;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class TestArrayInit {

	@Test
	public void test() throws IOException {
		 // create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(System.in);
 	
 	
        // create a lexer that feeds off of input CharStream
        ArrayInitLexer lexer = new ArrayInitLexer(input);
 	
 	
        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
 	
 	
        // create a parser that feeds off the tokens buffer
        ArrayInitParser parser = new ArrayInitParser(tokens);
 	
 	
        ParseTree tree = parser.init(); // begin parsing at init rule
 	
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
	}

}
