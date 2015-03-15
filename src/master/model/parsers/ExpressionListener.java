// Generated from Expression.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpressionParser}.
 */
public interface ExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#UnaryOp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(@NotNull ExpressionParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#UnaryOp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(@NotNull ExpressionParser.UnaryOpContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(@NotNull ExpressionParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(@NotNull ExpressionParser.VariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull ExpressionParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull ExpressionParser.NumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Factorial}.
	 * @param ctx the parse tree
	 */
	void enterFactorial(@NotNull ExpressionParser.FactorialContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Factorial}.
	 * @param ctx the parse tree
	 */
	void exitFactorial(@NotNull ExpressionParser.FactorialContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(@NotNull ExpressionParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(@NotNull ExpressionParser.FunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#AddSub}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(@NotNull ExpressionParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#AddSub}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(@NotNull ExpressionParser.AddSubContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Array}.
	 * @param ctx the parse tree
	 */
	void enterArray(@NotNull ExpressionParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Array}.
	 * @param ctx the parse tree
	 */
	void exitArray(@NotNull ExpressionParser.ArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#BooleanOp}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOp(@NotNull ExpressionParser.BooleanOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#BooleanOp}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOp(@NotNull ExpressionParser.BooleanOpContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#MulDiv}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(@NotNull ExpressionParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#MulDiv}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(@NotNull ExpressionParser.MulDivContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Bracketed}.
	 * @param ctx the parse tree
	 */
	void enterBracketed(@NotNull ExpressionParser.BracketedContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Bracketed}.
	 * @param ctx the parse tree
	 */
	void exitBracketed(@NotNull ExpressionParser.BracketedContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#IfThenElse}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElse(@NotNull ExpressionParser.IfThenElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#IfThenElse}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElse(@NotNull ExpressionParser.IfThenElseContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Exponentiation}.
	 * @param ctx the parse tree
	 */
	void enterExponentiation(@NotNull ExpressionParser.ExponentiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Exponentiation}.
	 * @param ctx the parse tree
	 */
	void exitExponentiation(@NotNull ExpressionParser.ExponentiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(@NotNull ExpressionParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(@NotNull ExpressionParser.NegationContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExpressionParser#Equality}.
	 * @param ctx the parse tree
	 */
	void enterEquality(@NotNull ExpressionParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#Equality}.
	 * @param ctx the parse tree
	 */
	void exitEquality(@NotNull ExpressionParser.EqualityContext ctx);
}