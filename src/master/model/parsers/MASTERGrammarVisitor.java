// Generated from /Users/vaughant/code/beast_and_friends/MASTER/src/master/model/parsers/MASTERGrammar.g4 by ANTLR 4.10.1
package master.model.parsers;
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
	T visitReaction(MASTERGrammarParser.ReactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#reactants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReactants(MASTERGrammarParser.ReactantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#products}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProducts(MASTERGrammarParser.ProductsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#popsum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopsum(MASTERGrammarParser.PopsumContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#popel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopel(MASTERGrammarParser.PopelContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(MASTERGrammarParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(MASTERGrammarParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(MASTERGrammarParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#popname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopname(MASTERGrammarParser.PopnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#loc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoc(MASTERGrammarParser.LocContext ctx);
	/**
	 * Visit a parse tree produced by {@link MASTERGrammarParser#locel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocel(MASTERGrammarParser.LocelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(MASTERGrammarParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(MASTERGrammarParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(MASTERGrammarParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(MASTERGrammarParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(MASTERGrammarParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanOp(MASTERGrammarParser.BooleanOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Exponentiation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExponentiation(MASTERGrammarParser.ExponentiationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketed(MASTERGrammarParser.BracketedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Array}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(MASTERGrammarParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Factorial}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactorial(MASTERGrammarParser.FactorialContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(MASTERGrammarParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(MASTERGrammarParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArraySubscript}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraySubscript(MASTERGrammarParser.ArraySubscriptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(MASTERGrammarParser.EqualityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfThenElse}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenElse(MASTERGrammarParser.IfThenElseContext ctx);
}