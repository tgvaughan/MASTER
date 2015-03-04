// Generated from Expression.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExpressionParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__4=1, T__3=2, T__2=3, T__1=4, T__0=5, ADD=6, SUB=7, MUL=8, DIV=9, POW=10, 
		EXP=11, LOG=12, SQRT=13, SUM=14, THETA=15, ABS=16, NNINT=17, NNFLOAT=18, 
		VARNAME=19, WHITESPACE=20;
	public static final String[] tokenNames = {
		"<INVALID>", "']'", "')'", "','", "'['", "'('", "'+'", "'-'", "'*'", "'/'", 
		"'^'", "'exp'", "'log'", "'sqrt'", "'sum'", "'theta'", "'abs'", "NNINT", 
		"NNFLOAT", "VARNAME", "WHITESPACE"
	};
	public static final int
		RULE_expression = 0, RULE_factor = 1, RULE_molecule = 2, RULE_atom = 3;
	public static final String[] ruleNames = {
		"expression", "factor", "molecule", "atom"
	};

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AddSubContext extends ExpressionContext {
		public Token op;
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AddSubContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterAddSub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitAddSub(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitAddSub(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ELSEWHERE1Context extends ExpressionContext {
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public ELSEWHERE1Context(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterELSEWHERE1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitELSEWHERE1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitELSEWHERE1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ELSEWHERE1Context(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(9); factor(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(16);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new AddSubContext(new ExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(11);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(12);
					((AddSubContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==ADD || _la==SUB) ) {
						((AddSubContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(13); factor(0);
					}
					} 
				}
				setState(18);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FactorContext extends ParserRuleContext {
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
	 
		public FactorContext() { }
		public void copyFrom(FactorContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ELSEWHERE2Context extends FactorContext {
		public MoleculeContext molecule() {
			return getRuleContext(MoleculeContext.class,0);
		}
		public ELSEWHERE2Context(FactorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterELSEWHERE2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitELSEWHERE2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitELSEWHERE2(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MulDivContext extends FactorContext {
		public Token op;
		public MoleculeContext molecule() {
			return getRuleContext(MoleculeContext.class,0);
		}
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public MulDivContext(FactorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterMulDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitMulDiv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitMulDiv(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactorContext factor() throws RecognitionException {
		return factor(0);
	}

	private FactorContext factor(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FactorContext _localctx = new FactorContext(_ctx, _parentState);
		FactorContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_factor, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ELSEWHERE2Context(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(20); molecule();
			}
			_ctx.stop = _input.LT(-1);
			setState(27);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new MulDivContext(new FactorContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_factor);
					setState(22);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(23);
					((MulDivContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==MUL || _la==DIV) ) {
						((MulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(24); molecule();
					}
					} 
				}
				setState(29);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class MoleculeContext extends ParserRuleContext {
		public MoleculeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_molecule; }
	 
		public MoleculeContext() { }
		public void copyFrom(MoleculeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExponentiationContext extends MoleculeContext {
		public MoleculeContext molecule() {
			return getRuleContext(MoleculeContext.class,0);
		}
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public ExponentiationContext(MoleculeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterExponentiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitExponentiation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitExponentiation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegationContext extends MoleculeContext {
		public MoleculeContext molecule() {
			return getRuleContext(MoleculeContext.class,0);
		}
		public NegationContext(MoleculeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitNegation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitNegation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ELSEWHERE3Context extends MoleculeContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public ELSEWHERE3Context(MoleculeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterELSEWHERE3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitELSEWHERE3(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitELSEWHERE3(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MoleculeContext molecule() throws RecognitionException {
		MoleculeContext _localctx = new MoleculeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_molecule);
		try {
			setState(37);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new NegationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(30); match(SUB);
				setState(31); molecule();
				}
				break;

			case 2:
				_localctx = new ExponentiationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(32); atom();
				setState(33); match(POW);
				setState(34); molecule();
				}
				break;

			case 3:
				_localctx = new ELSEWHERE3Context(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(36); atom();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
	 
		public AtomContext() { }
		public void copyFrom(AtomContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BracketedContext extends AtomContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracketedContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterBracketed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitBracketed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitBracketed(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryOpContext extends AtomContext {
		public Token op;
		public TerminalNode SUM() { return getToken(ExpressionParser.SUM, 0); }
		public TerminalNode THETA() { return getToken(ExpressionParser.THETA, 0); }
		public TerminalNode LOG() { return getToken(ExpressionParser.LOG, 0); }
		public TerminalNode EXP() { return getToken(ExpressionParser.EXP, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ABS() { return getToken(ExpressionParser.ABS, 0); }
		public TerminalNode SQRT() { return getToken(ExpressionParser.SQRT, 0); }
		public UnaryOpContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterUnaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitUnaryOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitUnaryOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableContext extends AtomContext {
		public Token i;
		public TerminalNode NNINT() { return getToken(ExpressionParser.NNINT, 0); }
		public TerminalNode VARNAME() { return getToken(ExpressionParser.VARNAME, 0); }
		public VariableContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberContext extends AtomContext {
		public Token val;
		public TerminalNode NNFLOAT() { return getToken(ExpressionParser.NNFLOAT, 0); }
		public TerminalNode NNINT() { return getToken(ExpressionParser.NNINT, 0); }
		public NumberContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayContext extends AtomContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ArrayContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionVisitor ) return ((ExpressionVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atom);
		int _la;
		try {
			setState(66);
			switch (_input.LA(1)) {
			case 5:
				_localctx = new BracketedContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(39); match(5);
				setState(40); expression(0);
				setState(41); match(2);
				}
				break;
			case 4:
				_localctx = new ArrayContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(43); match(4);
				setState(44); expression(0);
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==3) {
					{
					{
					setState(45); match(3);
					setState(46); expression(0);
					}
					}
					setState(51);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(52); match(1);
				}
				break;
			case EXP:
			case LOG:
			case SQRT:
			case SUM:
			case THETA:
			case ABS:
				_localctx = new UnaryOpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(54);
				((UnaryOpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EXP) | (1L << LOG) | (1L << SQRT) | (1L << SUM) | (1L << THETA) | (1L << ABS))) != 0)) ) {
					((UnaryOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				consume();
				setState(55); match(5);
				setState(56); expression(0);
				setState(57); match(2);
				}
				break;
			case VARNAME:
				_localctx = new VariableContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(59); match(VARNAME);
				setState(63);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(60); match(4);
					setState(61); ((VariableContext)_localctx).i = match(NNINT);
					setState(62); match(1);
					}
					break;
				}
				}
				break;
			case NNINT:
			case NNFLOAT:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(65);
				((NumberContext)_localctx).val = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==NNINT || _la==NNFLOAT) ) {
					((NumberContext)_localctx).val = (Token)_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0: return expression_sempred((ExpressionContext)_localctx, predIndex);

		case 1: return factor_sempred((FactorContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean factor_sempred(FactorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1: return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26G\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\3\2\3\2\7\2\21\n\2\f\2\16\2\24\13\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\7\3\34\n\3\f\3\16\3\37\13\3\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\5\4(\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5\62\n\5\f\5\16"+
		"\5\65\13\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5B\n\5\3\5\5"+
		"\5E\n\5\3\5\2\4\2\4\6\2\4\6\b\2\6\3\2\b\t\3\2\n\13\3\2\r\22\3\2\23\24"+
		"L\2\n\3\2\2\2\4\25\3\2\2\2\6\'\3\2\2\2\bD\3\2\2\2\n\13\b\2\1\2\13\f\5"+
		"\4\3\2\f\22\3\2\2\2\r\16\f\4\2\2\16\17\t\2\2\2\17\21\5\4\3\2\20\r\3\2"+
		"\2\2\21\24\3\2\2\2\22\20\3\2\2\2\22\23\3\2\2\2\23\3\3\2\2\2\24\22\3\2"+
		"\2\2\25\26\b\3\1\2\26\27\5\6\4\2\27\35\3\2\2\2\30\31\f\4\2\2\31\32\t\3"+
		"\2\2\32\34\5\6\4\2\33\30\3\2\2\2\34\37\3\2\2\2\35\33\3\2\2\2\35\36\3\2"+
		"\2\2\36\5\3\2\2\2\37\35\3\2\2\2 !\7\t\2\2!(\5\6\4\2\"#\5\b\5\2#$\7\f\2"+
		"\2$%\5\6\4\2%(\3\2\2\2&(\5\b\5\2\' \3\2\2\2\'\"\3\2\2\2\'&\3\2\2\2(\7"+
		"\3\2\2\2)*\7\7\2\2*+\5\2\2\2+,\7\4\2\2,E\3\2\2\2-.\7\6\2\2.\63\5\2\2\2"+
		"/\60\7\5\2\2\60\62\5\2\2\2\61/\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63"+
		"\64\3\2\2\2\64\66\3\2\2\2\65\63\3\2\2\2\66\67\7\3\2\2\67E\3\2\2\289\t"+
		"\4\2\29:\7\7\2\2:;\5\2\2\2;<\7\4\2\2<E\3\2\2\2=A\7\25\2\2>?\7\6\2\2?@"+
		"\7\23\2\2@B\7\3\2\2A>\3\2\2\2AB\3\2\2\2BE\3\2\2\2CE\t\5\2\2D)\3\2\2\2"+
		"D-\3\2\2\2D8\3\2\2\2D=\3\2\2\2DC\3\2\2\2E\t\3\2\2\2\b\22\35\'\63AD";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}