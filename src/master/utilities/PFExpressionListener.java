// Generated from PFExpression.g4 by ANTLR 4.2
package master.utilities;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PFExpressionParser}.
 */
public interface PFExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull PFExpressionParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull PFExpressionParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(@NotNull PFExpressionParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(@NotNull PFExpressionParser.TermContext ctx);

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
	 * Enter a parse tree produced by {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(@NotNull PFExpressionParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(@NotNull PFExpressionParser.FactorContext ctx);

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
}