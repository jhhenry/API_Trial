package org.henry.antlr.tour;

import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Calc {

	public static void main(String[] args) throws Exception {
		String inputFile = null;
		if (args.length > 0) inputFile= args[0];
		InputStream is = System.in;
		if (inputFile != null) is = new FileInputStream(inputFile);
		ANTLRInputStream input = new ANTLRInputStream(is);
		LabeledExprLexer lexer = new LabeledExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LabeledExprParser parser = new LabeledExprParser(tokens);
		ParseTree prog = parser.prog();
		
		EvalVisitor v = new EvalVisitor();
		v.visit(prog);
	}

}
