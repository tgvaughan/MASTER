// Generated from /Users/vaughant/code/beast_and_friends/MASTER/src/master/model/parsers/MASTERGrammar.g4 by ANTLR 4.7
package master.model.parsers;
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
	void enterReaction(MASTERGrammarParser.ReactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#reaction}.
	 * @param ctx the parse tree
	 */
	void exitReaction(MASTERGrammarParser.ReactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#reactants}.
	 * @param ctx the parse tree
	 */
	void enterReactants(MASTERGrammarParser.ReactantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#reactants}.
	 * @param ctx the parse tree
	 */
	void exitReactants(MASTERGrammarParser.ReactantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#products}.
	 * @param ctx the parse tree
	 */
	void enterProducts(MASTERGrammarParser.ProductsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#products}.
	 * @param ctx the parse tree
	 */
	void exitProducts(MASTERGrammarParser.ProductsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#popsum}.
	 * @param ctx the parse tree
	 */
	void enterPopsum(MASTERGrammarParser.PopsumContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#popsum}.
	 * @param ctx the parse tree
	 */
	void exitPopsum(MASTERGrammarParser.PopsumContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#popel}.
	 * @param ctx the parse tree
	 */
	void enterPopel(MASTERGrammarParser.PopelContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#popel}.
	 * @param ctx the parse tree
	 */
	void exitPopel(MASTERGrammarParser.PopelContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(MASTERGrammarParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(MASTERGrammarParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(MASTERGrammarParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(MASTERGrammarParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(MASTERGrammarParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(MASTERGrammarParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#popname}.
	 * @param ctx the parse tree
	 */
	void enterPopname(MASTERGrammarParser.PopnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#popname}.
	 * @param ctx the parse tree
	 */
	void exitPopname(MASTERGrammarParser.PopnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#loc}.
	 * @param ctx the parse tree
	 */
	void enterLoc(MASTERGrammarParser.LocContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#loc}.
	 * @param ctx the parse tree
	 */
	void exitLoc(MASTERGrammarParser.LocContext ctx);
	/**
	 * Enter a parse tree produced by {@link MASTERGrammarParser#locel}.
	 * @param ctx the parse tree
	 */
	void enterLocel(MASTERGrammarParser.LocelContext ctx);
	/**
	 * Exit a parse tree produced by {@link MASTERGrammarParser#locel}.
	 * @param ctx the parse tree
	 */
	void exitLocel(MASTERGrammarParser.LocelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(MASTERGrammarParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(MASTERGrammarParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVariable(MASTERGrammarParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVariable(MASTERGrammarParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegation(MASTERGrammarParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegation(MASTERGrammarParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(MASTERGrammarParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(MASTERGrammarParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(MASTERGrammarParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(MASTERGrammarParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOp(MASTERGrammarParser.BooleanOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanOp}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOp(MASTERGrammarParser.BooleanOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Exponentiation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExponentiation(MASTERGrammarParser.ExponentiationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Exponentiation}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExponentiation(MASTERGrammarParser.ExponentiationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBracketed(MASTERGrammarParser.BracketedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bracketed}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBracketed(MASTERGrammarParser.BracketedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Array}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArray(MASTERGrammarParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Array}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArray(MASTERGrammarParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Factorial}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFactorial(MASTERGrammarParser.FactorialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Factorial}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFactorial(MASTERGrammarParser.FactorialContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Function}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunction(MASTERGrammarParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunction(MASTERGrammarParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNumber(MASTERGrammarParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNumber(MASTERGrammarParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArraySubscript}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArraySubscript(MASTERGrammarParser.ArraySubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArraySubscript}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArraySubscript(MASTERGrammarParser.ArraySubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEquality(MASTERGrammarParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEquality(MASTERGrammarParser.EqualityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfThenElse}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElse(MASTERGrammarParser.IfThenElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfThenElse}
	 * labeled alternative in {@link MASTERGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElse(MASTERGrammarParser.IfThenElseContext ctx);
}