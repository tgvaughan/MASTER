// Generated from /home/tvaughan/code/beast_and_friends/MASTER/src/master/model/parsers/MASTERGrammar.g4 by ANTLR 4.5
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
public class MASTERGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, ADD=13, SUB=14, MUL=15, DIV=16, MOD=17, POW=18, 
		EXP=19, LOG=20, SQRT=21, SUM=22, THETA=23, ABS=24, AND=25, OR=26, EQ=27, 
		GT=28, LT=29, GE=30, LE=31, NE=32, ZERO=33, NZINT=34, NNFLOAT=35, IDENT=36, 
		COMMENT_SINGLELINE=37, COMMENT_MULTILINE=38, WHITESPACE=39;
	public static final int
		RULE_reaction = 0, RULE_reactants = 1, RULE_products = 2, RULE_popsum = 3, 
		RULE_popel = 4, RULE_factor = 5, RULE_id = 6, RULE_assignment = 7, RULE_popname = 8, 
		RULE_loc = 9, RULE_locel = 10, RULE_expression = 11;
	public static final String[] ruleNames = {
		"reaction", "reactants", "products", "popsum", "popel", "factor", "id", 
		"assignment", "popname", "loc", "locel", "expression"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'->'", "':'", "':='", "'['", "','", "']'", "'('", "')'", "'{'", 
		"'}'", "'!'", "'?'", "'+'", "'-'", "'*'", "'/'", "'%'", "'^'", "'exp'", 
		"'log'", "'sqrt'", "'sum'", "'theta'", "'abs'", "'&&'", "'||'", "'=='", 
		"'>'", "'<'", "'>='", "'<='", "'!='", "'0'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "ADD", "SUB", "MUL", "DIV", "MOD", "POW", "EXP", "LOG", "SQRT", 
		"SUM", "THETA", "ABS", "AND", "OR", "EQ", "GT", "LT", "GE", "LE", "NE", 
		"ZERO", "NZINT", "NNFLOAT", "IDENT", "COMMENT_SINGLELINE", "COMMENT_MULTILINE", 
		"WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	@NotNull
	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MASTERGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MASTERGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ReactionContext extends ParserRuleContext {
		public ReactantsContext reactants() {
			return getRuleContext(ReactantsContext.class,0);
		}
		public ProductsContext products() {
			return getRuleContext(ProductsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MASTERGrammarParser.EOF, 0); }
		public ReactionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reaction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterReaction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitReaction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitReaction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReactionContext reaction() throws RecognitionException {
		ReactionContext _localctx = new ReactionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_reaction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24); 
			reactants();
			setState(25); 
			match(T__0);
			setState(26); 
			products();
			setState(27); 
			match(EOF);
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

	public static class ReactantsContext extends ParserRuleContext {
		public PopsumContext popsum() {
			return getRuleContext(PopsumContext.class,0);
		}
		public ReactantsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reactants; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterReactants(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitReactants(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitReactants(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReactantsContext reactants() throws RecognitionException {
		ReactantsContext _localctx = new ReactantsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_reactants);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29); 
			popsum();
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

	public static class ProductsContext extends ParserRuleContext {
		public PopsumContext popsum() {
			return getRuleContext(PopsumContext.class,0);
		}
		public ProductsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_products; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterProducts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitProducts(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitProducts(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProductsContext products() throws RecognitionException {
		ProductsContext _localctx = new ProductsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_products);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31); 
			popsum();
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

	public static class PopsumContext extends ParserRuleContext {
		public List<PopelContext> popel() {
			return getRuleContexts(PopelContext.class);
		}
		public PopelContext popel(int i) {
			return getRuleContext(PopelContext.class,i);
		}
		public PopsumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_popsum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterPopsum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitPopsum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitPopsum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PopsumContext popsum() throws RecognitionException {
		PopsumContext _localctx = new PopsumContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_popsum);
		int _la;
		try {
			setState(42);
			switch (_input.LA(1)) {
			case NZINT:
			case IDENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(33); 
				popel();
				setState(38);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ADD) {
					{
					{
					setState(34); 
					match(ADD);
					setState(35); 
					popel();
					}
					}
					setState(40);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(41); 
				match(ZERO);
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

	public static class PopelContext extends ParserRuleContext {
		public PopnameContext popname() {
			return getRuleContext(PopnameContext.class,0);
		}
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public PopelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_popel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterPopel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitPopel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitPopel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PopelContext popel() throws RecognitionException {
		PopelContext _localctx = new PopelContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_popel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			_la = _input.LA(1);
			if (_la==NZINT) {
				{
				setState(44); 
				factor();
				}
			}

			setState(47); 
			popname();
			setState(49);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(48); 
				loc();
				}
			}

			setState(53);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(51); 
				match(T__1);
				setState(52); 
				id();
				}
			}

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

	public static class FactorContext extends ParserRuleContext {
		public TerminalNode NZINT() { return getToken(MASTERGrammarParser.NZINT, 0); }
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterFactor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitFactor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitFactor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_factor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55); 
			match(NZINT);
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

	public static class IdContext extends ParserRuleContext {
		public TerminalNode NZINT() { return getToken(MASTERGrammarParser.NZINT, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_la = _input.LA(1);
			if ( !(_la==ZERO || _la==NZINT) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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

	public static class AssignmentContext extends ParserRuleContext {
		public PopnameContext popname() {
			return getRuleContext(PopnameContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MASTERGrammarParser.EOF, 0); }
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_assignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59); 
			popname();
			setState(61);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(60); 
				loc();
				}
			}

			setState(63); 
			match(T__2);
			setState(64); 
			expression(0);
			setState(65); 
			match(EOF);
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

	public static class PopnameContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(MASTERGrammarParser.IDENT, 0); }
		public PopnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_popname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterPopname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitPopname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitPopname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PopnameContext popname() throws RecognitionException {
		PopnameContext _localctx = new PopnameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_popname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); 
			match(IDENT);
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

	public static class LocContext extends ParserRuleContext {
		public List<LocelContext> locel() {
			return getRuleContexts(LocelContext.class);
		}
		public LocelContext locel(int i) {
			return getRuleContext(LocelContext.class,i);
		}
		public LocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterLoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitLoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitLoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocContext loc() throws RecognitionException {
		LocContext _localctx = new LocContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_loc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69); 
			match(T__3);
			setState(70); 
			locel();
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(71); 
				match(T__4);
				setState(72); 
				locel();
				}
				}
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(78); 
			match(T__5);
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

	public static class LocelContext extends ParserRuleContext {
		public TerminalNode NZINT() { return getToken(MASTERGrammarParser.NZINT, 0); }
		public TerminalNode IDENT() { return getToken(MASTERGrammarParser.IDENT, 0); }
		public LocelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterLocel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitLocel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitLocel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocelContext locel() throws RecognitionException {
		LocelContext _localctx = new LocelContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_locel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << NZINT) | (1L << IDENT))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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
	public static class UnaryOpContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EXP() { return getToken(MASTERGrammarParser.EXP, 0); }
		public TerminalNode LOG() { return getToken(MASTERGrammarParser.LOG, 0); }
		public TerminalNode SQRT() { return getToken(MASTERGrammarParser.SQRT, 0); }
		public TerminalNode SUM() { return getToken(MASTERGrammarParser.SUM, 0); }
		public TerminalNode THETA() { return getToken(MASTERGrammarParser.THETA, 0); }
		public TerminalNode ABS() { return getToken(MASTERGrammarParser.ABS, 0); }
		public UnaryOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterUnaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitUnaryOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitUnaryOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableContext extends ExpressionContext {
		public TerminalNode IDENT() { return getToken(MASTERGrammarParser.IDENT, 0); }
		public VariableContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberContext extends ExpressionContext {
		public Token val;
		public TerminalNode NZINT() { return getToken(MASTERGrammarParser.NZINT, 0); }
		public TerminalNode NNFLOAT() { return getToken(MASTERGrammarParser.NNFLOAT, 0); }
		public NumberContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FactorialContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FactorialContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterFactorial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitFactorial(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitFactorial(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionContext extends ExpressionContext {
		public TerminalNode IDENT() { return getToken(MASTERGrammarParser.IDENT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public FunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddSubContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AddSubContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterAddSub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitAddSub(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitAddSub(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanOpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BooleanOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterBooleanOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitBooleanOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitBooleanOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MulDivContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MulDivContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterMulDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitMulDiv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitMulDiv(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BracketedContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracketedContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterBracketed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitBracketed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitBracketed(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfThenElseContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IfThenElseContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterIfThenElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitIfThenElse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitIfThenElse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExponentiationContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExponentiationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterExponentiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitExponentiation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitExponentiation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArraySubscriptContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArraySubscriptContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterArraySubscript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitArraySubscript(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitArraySubscript(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegationContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NegationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitNegation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitNegation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqualityContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EqualityContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).enterEquality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MASTERGrammarListener ) ((MASTERGrammarListener)listener).exitEquality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MASTERGrammarVisitor ) return ((MASTERGrammarVisitor<? extends T>)visitor).visitEquality(this);
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
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				_localctx = new NegationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(83); 
				match(SUB);
				setState(84); 
				expression(10);
				}
				break;
			case 2:
				{
				_localctx = new BracketedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(85); 
				match(T__6);
				setState(86); 
				expression(0);
				setState(87); 
				match(T__7);
				}
				break;
			case 3:
				{
				_localctx = new ArrayContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(89); 
				match(T__8);
				setState(90); 
				expression(0);
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(91); 
					match(T__4);
					setState(92); 
					expression(0);
					}
					}
					setState(97);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(98); 
				match(T__9);
				}
				break;
			case 4:
				{
				_localctx = new FunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100); 
				match(IDENT);
				setState(101); 
				match(T__6);
				setState(102); 
				expression(0);
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(103); 
					match(T__4);
					setState(104); 
					expression(0);
					}
					}
					setState(109);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(110); 
				match(T__7);
				}
				break;
			case 5:
				{
				_localctx = new UnaryOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(112);
				((UnaryOpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EXP) | (1L << LOG) | (1L << SQRT) | (1L << SUM) | (1L << THETA) | (1L << ABS))) != 0)) ) {
					((UnaryOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				consume();
				setState(113); 
				match(T__6);
				setState(114); 
				expression(0);
				setState(115); 
				match(T__7);
				}
				break;
			case 6:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(117); 
				match(IDENT);
				}
				break;
			case 7:
				{
				_localctx = new NumberContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(118);
				((NumberContext)_localctx).val = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << NZINT) | (1L << NNFLOAT))) != 0)) ) {
					((NumberContext)_localctx).val = (Token)_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(151);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(149);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						_localctx = new ExponentiationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(121);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(122); 
						match(POW);
						setState(123); 
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new MulDivContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(124);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(125);
						((MulDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((MulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(126); 
						expression(8);
						}
						break;
					case 3:
						{
						_localctx = new AddSubContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(127);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(128);
						((AddSubContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((AddSubContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(129); 
						expression(7);
						}
						break;
					case 4:
						{
						_localctx = new EqualityContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(130);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(131);
						((EqualityContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE) | (1L << NE))) != 0)) ) {
							((EqualityContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(132); 
						expression(6);
						}
						break;
					case 5:
						{
						_localctx = new BooleanOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(133);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(134);
						((BooleanOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
							((BooleanOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(135); 
						expression(5);
						}
						break;
					case 6:
						{
						_localctx = new IfThenElseContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(136);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(137); 
						match(T__11);
						setState(138); 
						expression(0);
						setState(139); 
						match(T__1);
						setState(140); 
						expression(3);
						}
						break;
					case 7:
						{
						_localctx = new ArraySubscriptContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(142);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(143); 
						match(T__3);
						setState(144); 
						expression(0);
						setState(145); 
						match(T__5);
						}
						break;
					case 8:
						{
						_localctx = new FactorialContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(147);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(148); 
						match(T__10);
						}
						break;
					}
					} 
				}
				setState(153);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11: 
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: 
			return precpred(_ctx, 8);
		case 1: 
			return precpred(_ctx, 7);
		case 2: 
			return precpred(_ctx, 6);
		case 3: 
			return precpred(_ctx, 5);
		case 4: 
			return precpred(_ctx, 4);
		case 5: 
			return precpred(_ctx, 3);
		case 6: 
			return precpred(_ctx, 13);
		case 7: 
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3)\u009d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\7"+
		"\5\'\n\5\f\5\16\5*\13\5\3\5\5\5-\n\5\3\6\5\6\60\n\6\3\6\3\6\5\6\64\n\6"+
		"\3\6\3\6\5\68\n\6\3\7\3\7\3\b\3\b\3\t\3\t\5\t@\n\t\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\13\3\13\3\13\3\13\7\13L\n\13\f\13\16\13O\13\13\3\13\3\13\3\f\3"+
		"\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r`\n\r\f\r\16\rc\13\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\rl\n\r\f\r\16\ro\13\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\5\rz\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\7\r\u0098\n\r\f\r\16\r\u009b\13\r\3\r\2\3\30\16\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\2\n\3\2#$\4\2#$&&\3\2\25\32\3\2#%\3\2\21\23\3\2\17\20\3\2\35"+
		"\"\3\2\33\34\u00a7\2\32\3\2\2\2\4\37\3\2\2\2\6!\3\2\2\2\b,\3\2\2\2\n/"+
		"\3\2\2\2\f9\3\2\2\2\16;\3\2\2\2\20=\3\2\2\2\22E\3\2\2\2\24G\3\2\2\2\26"+
		"R\3\2\2\2\30y\3\2\2\2\32\33\5\4\3\2\33\34\7\3\2\2\34\35\5\6\4\2\35\36"+
		"\7\2\2\3\36\3\3\2\2\2\37 \5\b\5\2 \5\3\2\2\2!\"\5\b\5\2\"\7\3\2\2\2#("+
		"\5\n\6\2$%\7\17\2\2%\'\5\n\6\2&$\3\2\2\2\'*\3\2\2\2(&\3\2\2\2()\3\2\2"+
		"\2)-\3\2\2\2*(\3\2\2\2+-\7#\2\2,#\3\2\2\2,+\3\2\2\2-\t\3\2\2\2.\60\5\f"+
		"\7\2/.\3\2\2\2/\60\3\2\2\2\60\61\3\2\2\2\61\63\5\22\n\2\62\64\5\24\13"+
		"\2\63\62\3\2\2\2\63\64\3\2\2\2\64\67\3\2\2\2\65\66\7\4\2\2\668\5\16\b"+
		"\2\67\65\3\2\2\2\678\3\2\2\28\13\3\2\2\29:\7$\2\2:\r\3\2\2\2;<\t\2\2\2"+
		"<\17\3\2\2\2=?\5\22\n\2>@\5\24\13\2?>\3\2\2\2?@\3\2\2\2@A\3\2\2\2AB\7"+
		"\5\2\2BC\5\30\r\2CD\7\2\2\3D\21\3\2\2\2EF\7&\2\2F\23\3\2\2\2GH\7\6\2\2"+
		"HM\5\26\f\2IJ\7\7\2\2JL\5\26\f\2KI\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2"+
		"\2NP\3\2\2\2OM\3\2\2\2PQ\7\b\2\2Q\25\3\2\2\2RS\t\3\2\2S\27\3\2\2\2TU\b"+
		"\r\1\2UV\7\20\2\2Vz\5\30\r\fWX\7\t\2\2XY\5\30\r\2YZ\7\n\2\2Zz\3\2\2\2"+
		"[\\\7\13\2\2\\a\5\30\r\2]^\7\7\2\2^`\5\30\r\2_]\3\2\2\2`c\3\2\2\2a_\3"+
		"\2\2\2ab\3\2\2\2bd\3\2\2\2ca\3\2\2\2de\7\f\2\2ez\3\2\2\2fg\7&\2\2gh\7"+
		"\t\2\2hm\5\30\r\2ij\7\7\2\2jl\5\30\r\2ki\3\2\2\2lo\3\2\2\2mk\3\2\2\2m"+
		"n\3\2\2\2np\3\2\2\2om\3\2\2\2pq\7\n\2\2qz\3\2\2\2rs\t\4\2\2st\7\t\2\2"+
		"tu\5\30\r\2uv\7\n\2\2vz\3\2\2\2wz\7&\2\2xz\t\5\2\2yT\3\2\2\2yW\3\2\2\2"+
		"y[\3\2\2\2yf\3\2\2\2yr\3\2\2\2yw\3\2\2\2yx\3\2\2\2z\u0099\3\2\2\2{|\f"+
		"\n\2\2|}\7\24\2\2}\u0098\5\30\r\n~\177\f\t\2\2\177\u0080\t\6\2\2\u0080"+
		"\u0098\5\30\r\n\u0081\u0082\f\b\2\2\u0082\u0083\t\7\2\2\u0083\u0098\5"+
		"\30\r\t\u0084\u0085\f\7\2\2\u0085\u0086\t\b\2\2\u0086\u0098\5\30\r\b\u0087"+
		"\u0088\f\6\2\2\u0088\u0089\t\t\2\2\u0089\u0098\5\30\r\7\u008a\u008b\f"+
		"\5\2\2\u008b\u008c\7\16\2\2\u008c\u008d\5\30\r\2\u008d\u008e\7\4\2\2\u008e"+
		"\u008f\5\30\r\5\u008f\u0098\3\2\2\2\u0090\u0091\f\17\2\2\u0091\u0092\7"+
		"\6\2\2\u0092\u0093\5\30\r\2\u0093\u0094\7\b\2\2\u0094\u0098\3\2\2\2\u0095"+
		"\u0096\f\13\2\2\u0096\u0098\7\r\2\2\u0097{\3\2\2\2\u0097~\3\2\2\2\u0097"+
		"\u0081\3\2\2\2\u0097\u0084\3\2\2\2\u0097\u0087\3\2\2\2\u0097\u008a\3\2"+
		"\2\2\u0097\u0090\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u009b\3\2\2\2\u0099"+
		"\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\31\3\2\2\2\u009b\u0099\3\2\2"+
		"\2\16(,/\63\67?Mamy\u0097\u0099";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}