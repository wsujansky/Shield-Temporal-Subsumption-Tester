package gov.hhs.fda.shield.temporalreasoning;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import gov.hhs.fda.shield.temporalreasoning.utils.Printers;

public class TemporalExpressionParser {

	public static RtipDisjunction builtTemporalStructure;	
	
	private ParseTreeWalker walker = new ParseTreeWalker();
	private BuildGrtrStructureListener listener = new BuildGrtrStructureListener();

	public TemporalExpressionParser() {
		// TODO Auto-generated constructor stub
	}

	// Main class for debugging only
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String temporalRelationshipExpr = "{ BB[0sc,+INF) & EE(-INF,-0sc] } | { BE(+0sc, +INF) }  ";
		String temporalRelationshipExprSuper = "{ BB(-INF,+3.887mn) }";
		String temporalRelationshipExprSub = "{ EE{(-INF,-0sc] }";

		
		TemporalExpressionParser parser = new TemporalExpressionParser();
		
		System.out.println("SUPER TEMPORAL RELATIONSHIP");
		try {
			RtipDisjunction superRtipStructure = parser.parse(temporalRelationshipExprSuper);
			Printers.print(superRtipStructure, 0);
			System.out.println();
		} catch (IllegalStateException e) {
			System.out.println("**** ERROR when parsing temporal expression: \'"  + temporalRelationshipExprSuper + "\' => " + e.getMessage());
			System.out.println();
		}

		System.out.println("SUB TEMPORAL RELATIONSHIP");
		try {
			RtipDisjunction subRtipStructure = parser.parse(temporalRelationshipExprSub);
			Printers.print(subRtipStructure, 0);
			System.out.println();
		} catch (IllegalStateException e) {
			System.out.println("**** ERROR when parsing temporal expression: \'"  + temporalRelationshipExprSuper + "\' => " + e.getMessage());
			System.out.println();
		}
		
/*** FOR DEBUGGING ONLY
		TemporalRelationshipGrammarLexer lexer = new TemporalRelationshipGrammarLexer(CharStreams.fromString(temporalRelationshipExpr));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TemporalRelationshipGrammarParser parser = new TemporalRelationshipGrammarParser(tokens);
		
		try {
	
		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, RecognitionException e) {
							System.out.println("failed to parse a temporal expression due to " + msg);
							throw new IllegalStateException("failed to parse a temporal expression due to " + msg, e);
					}
			});

		//Specify our entry point
//		ParseTree exprContext = parser.rtip();  // This creates the parse tree


		// Specify our entry point for walking the tree
		ParserRuleContext rtipContext = parser.expression();
		
		// Attach our listener and walk the tree
		ParseTreeWalker walker = new ParseTreeWalker();
//		MyCodeExprListener listener = new MyCodeExprListener();
		BuildGrtrStructureListener listener = new BuildGrtrStructureListener();
//		TemporalRelationshipGrammarBaseListener listener = new TemporalRelationshipGrammarBaseListener();
		walker.walk(listener, rtipContext);

		System.out.println("Completed parsing of: " + temporalRelationshipExpr);
		} catch(Exception e){
			System.out.println("**** Parse error for code expression: " + temporalRelationshipExpr);
			System.out.println("ERROR message: " + e.getMessage());
			System.out.println();
		}
**/
	}
	
	public BuildGrtrStructureListener getListener() {
		return this.listener;
	}
	
	// Parser takes an input string consisting of Generalized Relative Temporal Relationship (GRTR), as defined by the grammar
	// TemporalRelationshipGrammar.g4, and generates a parse tree consisting of a single instance of the class RtipDisjunction.
	public RtipDisjunction parse(String input) throws IllegalStateException {
		TemporalRelationshipGrammarLexer lexer = new TemporalRelationshipGrammarLexer(CharStreams.fromString(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TemporalRelationshipGrammarParser parser = new TemporalRelationshipGrammarParser(tokens);
			parser.addErrorListener(new BaseErrorListener() {
				@Override
				public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
						int charPositionInLine, String msg, RecognitionException e) {
// DEBUG System.out.println("failed to parse a temporal expression due to " + msg);
					throw new IllegalStateException(msg, e);
				}
			});

			// Specify our entry point for walking the tree
			ParserRuleContext ruleContext = parser.expression();

			// Attach our listener and walk the tree
			walker.walk(this.listener, ruleContext);
			
		return builtTemporalStructure;   // This static (global) class member contains the parse tree that was populated by the listener 
		                                 //  (see BuildGrtrStructureListener.java for the logic that builds the parse tree from the input expression)
	}	

}
