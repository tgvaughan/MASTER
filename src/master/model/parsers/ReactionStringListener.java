// Generated from ReactionString.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ReactionStringParser}.
 */
public interface ReactionStringListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(@NotNull ReactionStringParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(@NotNull ReactionStringParser.IdContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#reaction}.
	 * @param ctx the parse tree
	 */
	void enterReaction(@NotNull ReactionStringParser.ReactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#reaction}.
	 * @param ctx the parse tree
	 */
	void exitReaction(@NotNull ReactionStringParser.ReactionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#popsum}.
	 * @param ctx the parse tree
	 */
	void enterPopsum(@NotNull ReactionStringParser.PopsumContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#popsum}.
	 * @param ctx the parse tree
	 */
	void exitPopsum(@NotNull ReactionStringParser.PopsumContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#popname}.
	 * @param ctx the parse tree
	 */
	void enterPopname(@NotNull ReactionStringParser.PopnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#popname}.
	 * @param ctx the parse tree
	 */
	void exitPopname(@NotNull ReactionStringParser.PopnameContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#loc}.
	 * @param ctx the parse tree
	 */
	void enterLoc(@NotNull ReactionStringParser.LocContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#loc}.
	 * @param ctx the parse tree
	 */
	void exitLoc(@NotNull ReactionStringParser.LocContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(@NotNull ReactionStringParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(@NotNull ReactionStringParser.FactorContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#reactants}.
	 * @param ctx the parse tree
	 */
	void enterReactants(@NotNull ReactionStringParser.ReactantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#reactants}.
	 * @param ctx the parse tree
	 */
	void exitReactants(@NotNull ReactionStringParser.ReactantsContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#products}.
	 * @param ctx the parse tree
	 */
	void enterProducts(@NotNull ReactionStringParser.ProductsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#products}.
	 * @param ctx the parse tree
	 */
	void exitProducts(@NotNull ReactionStringParser.ProductsContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#locel}.
	 * @param ctx the parse tree
	 */
	void enterLocel(@NotNull ReactionStringParser.LocelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#locel}.
	 * @param ctx the parse tree
	 */
	void exitLocel(@NotNull ReactionStringParser.LocelContext ctx);

	/**
	 * Enter a parse tree produced by {@link ReactionStringParser#popel}.
	 * @param ctx the parse tree
	 */
	void enterPopel(@NotNull ReactionStringParser.PopelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReactionStringParser#popel}.
	 * @param ctx the parse tree
	 */
	void exitPopel(@NotNull ReactionStringParser.PopelContext ctx);
}