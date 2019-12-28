// Generated from Arithmetic.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArithmeticParser}.
 */
public interface ArithmeticListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(ArithmeticParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(ArithmeticParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#evaluation}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation(ArithmeticParser.EvaluationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#evaluation}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation(ArithmeticParser.EvaluationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(ArithmeticParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(ArithmeticParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ArithmeticParser.ExpressionContext ctx);
}