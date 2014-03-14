// Generated from PFExpression.g4 by ANTLR 4.2
package master.utilities.pfe;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PFExpressionParser}.
 */
public interface PFExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#Bracketed}.
	 * @param ctx the parse tree
	 */
	void enterBracketed(@NotNull PFExpressionParser.BracketedContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#Bracketed}.
	 * @param ctx the parse tree
	 */
	void exitBracketed(@NotNull PFExpressionParser.BracketedContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#Mul}.
	 * @param ctx the parse tree
	 */
	void enterMul(@NotNull PFExpressionParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#Mul}.
	 * @param ctx the parse tree
	 */
	void exitMul(@NotNull PFExpressionParser.MulContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#MulOrDiv}.
	 * @param ctx the parse tree
	 */
	void enterMulOrDiv(@NotNull PFExpressionParser.MulOrDivContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#MulOrDiv}.
	 * @param ctx the parse tree
	 */
	void exitMulOrDiv(@NotNull PFExpressionParser.MulOrDivContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(@NotNull PFExpressionParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(@NotNull PFExpressionParser.StartContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#Add}.
	 * @param ctx the parse tree
	 */
	void enterAdd(@NotNull PFExpressionParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#Add}.
	 * @param ctx the parse tree
	 */
	void exitAdd(@NotNull PFExpressionParser.AddContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull PFExpressionParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull PFExpressionParser.NumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#pop}.
	 * @param ctx the parse tree
	 */
	void enterPop(@NotNull PFExpressionParser.PopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#pop}.
	 * @param ctx the parse tree
	 */
	void exitPop(@NotNull PFExpressionParser.PopContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#Sub}.
	 * @param ctx the parse tree
	 */
	void enterSub(@NotNull PFExpressionParser.SubContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#Sub}.
	 * @param ctx the parse tree
	 */
	void exitSub(@NotNull PFExpressionParser.SubContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#Unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(@NotNull PFExpressionParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#Unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(@NotNull PFExpressionParser.UnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 */
	void enterPopulation(@NotNull PFExpressionParser.PopulationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 */
	void exitPopulation(@NotNull PFExpressionParser.PopulationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#Div}.
	 * @param ctx the parse tree
	 */
	void enterDiv(@NotNull PFExpressionParser.DivContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#Div}.
	 * @param ctx the parse tree
	 */
	void exitDiv(@NotNull PFExpressionParser.DivContext ctx);
}