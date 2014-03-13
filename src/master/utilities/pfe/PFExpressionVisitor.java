// Generated from PFExpression.g4 by ANTLR 4.2
package master.utilities.pfe;
import org.antlr.v4.runtime.misc.NotNull;
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
	 * Visit a parse tree produced by {@link PFExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull PFExpressionParser.ExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(@NotNull PFExpressionParser.TermContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(@NotNull PFExpressionParser.StartContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(@NotNull PFExpressionParser.FactorContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopulation(@NotNull PFExpressionParser.PopulationContext ctx);
}