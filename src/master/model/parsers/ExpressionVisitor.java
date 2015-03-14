// Generated from Expression.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#UnaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(@NotNull ExpressionParser.UnaryOpContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(@NotNull ExpressionParser.VariableContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull ExpressionParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Factorial}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactorial(@NotNull ExpressionParser.FactorialContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(@NotNull ExpressionParser.FunctionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#AddSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(@NotNull ExpressionParser.AddSubContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(@NotNull ExpressionParser.ArrayContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#BooleanOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanOp(@NotNull ExpressionParser.BooleanOpContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#MulDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(@NotNull ExpressionParser.MulDivContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Bracketed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketed(@NotNull ExpressionParser.BracketedContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Exponentiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExponentiation(@NotNull ExpressionParser.ExponentiationContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Negation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(@NotNull ExpressionParser.NegationContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionParser#Equality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(@NotNull ExpressionParser.EqualityContext ctx);
}