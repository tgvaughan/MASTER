// Generated from /home/tvaughan/code/beast_and_friends/MASTER/src/master/model/parsers/MASTERGrammar.g4 by ANTLR 4.5
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MASTERGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MASTERGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#reaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReaction(@NotNull MASTERGrammarParser.ReactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#reactants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReactants(@NotNull MASTERGrammarParser.ReactantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#products}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProducts(@NotNull MASTERGrammarParser.ProductsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#popsum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopsum(@NotNull MASTERGrammarParser.PopsumContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#popel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopel(@NotNull MASTERGrammarParser.PopelContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(@NotNull MASTERGrammarParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(@NotNull MASTERGrammarParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(@NotNull MASTERGrammarParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#popname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopname(@NotNull MASTERGrammarParser.PopnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#loc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoc(@NotNull MASTERGrammarParser.LocContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#locel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocel(@NotNull MASTERGrammarParser.LocelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(@NotNull MASTERGrammarParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(@NotNull MASTERGrammarParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull MASTERGrammarParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Factorial}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactorial(@NotNull MASTERGrammarParser.FactorialContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(@NotNull MASTERGrammarParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(@NotNull MASTERGrammarParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Array}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(@NotNull MASTERGrammarParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanOp(@NotNull MASTERGrammarParser.BooleanOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(@NotNull MASTERGrammarParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketed(@NotNull MASTERGrammarParser.BracketedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfThenElse}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenElse(@NotNull MASTERGrammarParser.IfThenElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArraySubscript}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraySubscript(@NotNull MASTERGrammarParser.ArraySubscriptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Exponentiation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExponentiation(@NotNull MASTERGrammarParser.ExponentiationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(@NotNull MASTERGrammarParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(@NotNull MASTERGrammarParser.EqualityContext ctx);
}