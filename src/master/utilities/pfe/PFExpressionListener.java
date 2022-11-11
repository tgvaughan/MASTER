// Generated from /Users/vaughant/code/beast_and_friends/MASTER/src/master/utilities/pfe/PFExpression.g4 by ANTLR 4.10.1
package master.utilities.pfe;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PFExpressionParser}.
 */
public interface PFExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(PFExpressionParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(PFExpressionParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Add}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAdd(PFExpressionParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAdd(PFExpressionParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Sub}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSub(PFExpressionParser.SubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Sub}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSub(PFExpressionParser.SubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulOrDiv}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMulOrDiv(PFExpressionParser.MulOrDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulOrDiv}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMulOrDiv(PFExpressionParser.MulOrDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Div}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void enterDiv(PFExpressionParser.DivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Div}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void exitDiv(PFExpressionParser.DivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void enterMul(PFExpressionParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void exitMul(PFExpressionParser.MulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void enterUnit(PFExpressionParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void exitUnit(PFExpressionParser.UnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterBracketed(PFExpressionParser.BracketedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitBracketed(PFExpressionParser.BracketedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pop}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterPop(PFExpressionParser.PopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pop}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitPop(PFExpressionParser.PopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code number}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterNumber(PFExpressionParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code number}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitNumber(PFExpressionParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 */
	void enterPopulation(PFExpressionParser.PopulationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 */
	void exitPopulation(PFExpressionParser.PopulationContext ctx);
}