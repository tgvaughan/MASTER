// Generated from Equation.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link EquationParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface EquationVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link EquationParser#UnaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(@NotNull EquationParser.UnaryOpContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#Bracketed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketed(@NotNull EquationParser.BracketedContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#Variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(@NotNull EquationParser.VariableContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#equation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquation(@NotNull EquationParser.EquationContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#Number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull EquationParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#AddSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(@NotNull EquationParser.AddSubContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#Array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(@NotNull EquationParser.ArrayContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#Exponentiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExponentiation(@NotNull EquationParser.ExponentiationContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#ELSEWHERE1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitELSEWHERE1(@NotNull EquationParser.ELSEWHERE1Context ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#ELSEWHERE3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitELSEWHERE3(@NotNull EquationParser.ELSEWHERE3Context ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#Negation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(@NotNull EquationParser.NegationContext ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#ELSEWHERE2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitELSEWHERE2(@NotNull EquationParser.ELSEWHERE2Context ctx);

	/**
	 * Visit a parse tree produced by {@link EquationParser#MulDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(@NotNull EquationParser.MulDivContext ctx);
}