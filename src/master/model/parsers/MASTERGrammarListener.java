// Generated from /home/tvaughan/code/beast_and_friends/MASTER/src/master/model/parsers/MASTERGrammar.g4 by ANTLR 4.5
package master.model.parsers;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MASTERGrammarParser}.
 */
public interface MASTERGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#reaction}.
	 * @param ctx the parse tree
	 */
	void enterReaction(@NotNull MASTERGrammarParser.ReactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#reaction}.
	 * @param ctx the parse tree
	 */
	void exitReaction(@NotNull MASTERGrammarParser.ReactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#reactants}.
	 * @param ctx the parse tree
	 */
	void enterReactants(@NotNull MASTERGrammarParser.ReactantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#reactants}.
	 * @param ctx the parse tree
	 */
	void exitReactants(@NotNull MASTERGrammarParser.ReactantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#products}.
	 * @param ctx the parse tree
	 */
	void enterProducts(@NotNull MASTERGrammarParser.ProductsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#products}.
	 * @param ctx the parse tree
	 */
	void exitProducts(@NotNull MASTERGrammarParser.ProductsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#popsum}.
	 * @param ctx the parse tree
	 */
	void enterPopsum(@NotNull MASTERGrammarParser.PopsumContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#popsum}.
	 * @param ctx the parse tree
	 */
	void exitPopsum(@NotNull MASTERGrammarParser.PopsumContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#popel}.
	 * @param ctx the parse tree
	 */
	void enterPopel(@NotNull MASTERGrammarParser.PopelContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#popel}.
	 * @param ctx the parse tree
	 */
	void exitPopel(@NotNull MASTERGrammarParser.PopelContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(@NotNull MASTERGrammarParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(@NotNull MASTERGrammarParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(@NotNull MASTERGrammarParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(@NotNull MASTERGrammarParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(@NotNull MASTERGrammarParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(@NotNull MASTERGrammarParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#popname}.
	 * @param ctx the parse tree
	 */
	void enterPopname(@NotNull MASTERGrammarParser.PopnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#popname}.
	 * @param ctx the parse tree
	 */
	void exitPopname(@NotNull MASTERGrammarParser.PopnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#loc}.
	 * @param ctx the parse tree
	 */
	void enterLoc(@NotNull MASTERGrammarParser.LocContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#loc}.
	 * @param ctx the parse tree
	 */
	void exitLoc(@NotNull MASTERGrammarParser.LocContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#locel}.
	 * @param ctx the parse tree
	 */
	void enterLocel(@NotNull MASTERGrammarParser.LocelContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#locel}.
	 * @param ctx the parse tree
	 */
	void exitLocel(@NotNull MASTERGrammarParser.LocelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(@NotNull MASTERGrammarParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(@NotNull MASTERGrammarParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVariable(@NotNull MASTERGrammarParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVariable(@NotNull MASTERGrammarParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull MASTERGrammarParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull MASTERGrammarParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Factorial}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFactorial(@NotNull MASTERGrammarParser.FactorialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Factorial}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFactorial(@NotNull MASTERGrammarParser.FactorialContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Function}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunction(@NotNull MASTERGrammarParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunction(@NotNull MASTERGrammarParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(@NotNull MASTERGrammarParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(@NotNull MASTERGrammarParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Array}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArray(@NotNull MASTERGrammarParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Array}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArray(@NotNull MASTERGrammarParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOp(@NotNull MASTERGrammarParser.BooleanOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOp(@NotNull MASTERGrammarParser.BooleanOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(@NotNull MASTERGrammarParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(@NotNull MASTERGrammarParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBracketed(@NotNull MASTERGrammarParser.BracketedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBracketed(@NotNull MASTERGrammarParser.BracketedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfThenElse}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElse(@NotNull MASTERGrammarParser.IfThenElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfThenElse}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElse(@NotNull MASTERGrammarParser.IfThenElseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArraySubscript}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArraySubscript(@NotNull MASTERGrammarParser.ArraySubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArraySubscript}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArraySubscript(@NotNull MASTERGrammarParser.ArraySubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Exponentiation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExponentiation(@NotNull MASTERGrammarParser.ExponentiationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Exponentiation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExponentiation(@NotNull MASTERGrammarParser.ExponentiationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegation(@NotNull MASTERGrammarParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegation(@NotNull MASTERGrammarParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEquality(@NotNull MASTERGrammarParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEquality(@NotNull MASTERGrammarParser.EqualityContext ctx);
}