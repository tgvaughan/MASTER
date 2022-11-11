// Generated from /Users/vaughant/code/beast_and_friends/MASTER/src/master/utilities/pfe/PFExpression.g4 by ANTLR 4.10.1
package master.utilities.pfe;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PFExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PFExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(PFExpressionParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(PFExpressionParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Sub}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub(PFExpressionParser.SubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulOrDiv}
	 * labeled alternative in {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOrDiv(PFExpressionParser.MulOrDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Div}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiv(PFExpressionParser.DivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul(PFExpressionParser.MulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit}
	 * labeled alternative in {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(PFExpressionParser.UnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketed(PFExpressionParser.BracketedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pop}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPop(PFExpressionParser.PopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number}
	 * labeled alternative in {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(PFExpressionParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopulation(PFExpressionParser.PopulationContext ctx);
}