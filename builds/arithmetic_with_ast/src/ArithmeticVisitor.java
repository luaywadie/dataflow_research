// Generated from Arithmetic.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ArithmeticParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ArithmeticVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ArithmeticParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(ArithmeticParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link ArithmeticParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ArithmeticParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ArithmeticParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(ArithmeticParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ArithmeticParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpression(ArithmeticParser.ArithmeticExpressionContext ctx);
}