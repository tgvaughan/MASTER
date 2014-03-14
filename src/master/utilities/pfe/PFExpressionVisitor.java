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
	 * Visit a parse tree produced by {@link PFExpressionParser#Bracketed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketed(@NotNull PFExpressionParser.BracketedContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#Mul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul(@NotNull PFExpressionParser.MulContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#MulOrDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOrDiv(@NotNull PFExpressionParser.MulOrDivContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(@NotNull PFExpressionParser.StartContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#Add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(@NotNull PFExpressionParser.AddContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull PFExpressionParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#pop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPop(@NotNull PFExpressionParser.PopContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#Sub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub(@NotNull PFExpressionParser.SubContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#Unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(@NotNull PFExpressionParser.UnitContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#population}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopulation(@NotNull PFExpressionParser.PopulationContext ctx);

	/**
	 * Visit a parse tree produced by {@link PFExpressionParser#Div}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiv(@NotNull PFExpressionParser.DivContext ctx);
}