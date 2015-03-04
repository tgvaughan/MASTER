// Generated from Equation.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EquationParser}.
 */
public interface EquationListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link EquationParser#UnaryOp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(@NotNull EquationParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#UnaryOp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(@NotNull EquationParser.UnaryOpContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#Bracketed}.
	 * @param ctx the parse tree
	 */
	void enterBracketed(@NotNull EquationParser.BracketedContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#Bracketed}.
	 * @param ctx the parse tree
	 */
	void exitBracketed(@NotNull EquationParser.BracketedContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#Variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(@NotNull EquationParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#Variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(@NotNull EquationParser.VariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(@NotNull EquationParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(@NotNull EquationParser.EquationContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#Number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull EquationParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#Number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull EquationParser.NumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#AddSub}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(@NotNull EquationParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#AddSub}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(@NotNull EquationParser.AddSubContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#Array}.
	 * @param ctx the parse tree
	 */
	void enterArray(@NotNull EquationParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#Array}.
	 * @param ctx the parse tree
	 */
	void exitArray(@NotNull EquationParser.ArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#Exponentiation}.
	 * @param ctx the parse tree
	 */
	void enterExponentiation(@NotNull EquationParser.ExponentiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#Exponentiation}.
	 * @param ctx the parse tree
	 */
	void exitExponentiation(@NotNull EquationParser.ExponentiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#ELSEWHERE1}.
	 * @param ctx the parse tree
	 */
	void enterELSEWHERE1(@NotNull EquationParser.ELSEWHERE1Context ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#ELSEWHERE1}.
	 * @param ctx the parse tree
	 */
	void exitELSEWHERE1(@NotNull EquationParser.ELSEWHERE1Context ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#ELSEWHERE3}.
	 * @param ctx the parse tree
	 */
	void enterELSEWHERE3(@NotNull EquationParser.ELSEWHERE3Context ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#ELSEWHERE3}.
	 * @param ctx the parse tree
	 */
	void exitELSEWHERE3(@NotNull EquationParser.ELSEWHERE3Context ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#Negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(@NotNull EquationParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#Negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(@NotNull EquationParser.NegationContext ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#ELSEWHERE2}.
	 * @param ctx the parse tree
	 */
	void enterELSEWHERE2(@NotNull EquationParser.ELSEWHERE2Context ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#ELSEWHERE2}.
	 * @param ctx the parse tree
	 */
	void exitELSEWHERE2(@NotNull EquationParser.ELSEWHERE2Context ctx);

	/**
	 * Enter a parse tree produced by {@link EquationParser#MulDiv}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(@NotNull EquationParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by {@link EquationParser#MulDiv}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(@NotNull EquationParser.MulDivContext ctx);
}