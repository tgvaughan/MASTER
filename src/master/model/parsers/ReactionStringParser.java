// Generated from ReactionString.g4 by ANTLR 4.2
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
public class ReactionStringParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__6=1, T__5=2, T__4=3, T__3=4, T__2=5, T__1=6, T__0=7, NZINT=8, NAME=9, 
		WHITESPACE=10;
	public static final String[] tokenNames = {
		"<INVALID>", "'->'", "']'", "','", "'+'", "'['", "':'", "'0'", "NZINT", 
		"NAME", "WHITESPACE"
	};
	public static final int
		RULE_reaction = 0, RULE_reactants = 1, RULE_products = 2, RULE_popsum = 3, 
		RULE_popel = 4, RULE_factor = 5, RULE_popname = 6, RULE_loc = 7, RULE_locel = 8, 
		RULE_id = 9;
	public static final String[] ruleNames = {
		"reaction", "reactants", "products", "popsum", "popel", "factor", "popname", 
		"loc", "locel", "id"
	};

	@Override
	public String getGrammarFileName() { return "ReactionString.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ReactionStringParser(TokenStream input) {
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
		public ReactionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reaction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterReaction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitReaction(this);
		}
	}

	public final ReactionContext reaction() throws RecognitionException {
		ReactionContext _localctx = new ReactionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_reaction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20); reactants();
			setState(21); match(1);
			setState(22); products();
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
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterReactants(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitReactants(this);
		}
	}

	public final ReactantsContext reactants() throws RecognitionException {
		ReactantsContext _localctx = new ReactantsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_reactants);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24); popsum();
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
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterProducts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitProducts(this);
		}
	}

	public final ProductsContext products() throws RecognitionException {
		ProductsContext _localctx = new ProductsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_products);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26); popsum();
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
		public PopelContext popel(int i) {
			return getRuleContext(PopelContext.class,i);
		}
		public List<PopelContext> popel() {
			return getRuleContexts(PopelContext.class);
		}
		public PopsumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_popsum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterPopsum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitPopsum(this);
		}
	}

	public final PopsumContext popsum() throws RecognitionException {
		PopsumContext _localctx = new PopsumContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_popsum);
		int _la;
		try {
			setState(37);
			switch (_input.LA(1)) {
			case NZINT:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(28); popel();
				setState(33);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==4) {
					{
					{
					setState(29); match(4);
					setState(30); popel();
					}
					}
					setState(35);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 2);
				{
				setState(36); match(7);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public PopnameContext popname() {
			return getRuleContext(PopnameContext.class,0);
		}
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public PopelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_popel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterPopel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitPopel(this);
		}
	}

	public final PopelContext popel() throws RecognitionException {
		PopelContext _localctx = new PopelContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_popel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			_la = _input.LA(1);
			if (_la==NZINT) {
				{
				setState(39); factor();
				}
			}

			setState(42); popname();
			setState(44);
			_la = _input.LA(1);
			if (_la==5) {
				{
				setState(43); loc();
				}
			}

			setState(48);
			_la = _input.LA(1);
			if (_la==6) {
				{
				setState(46); match(6);
				setState(47); id();
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
		public TerminalNode NZINT() { return getToken(ReactionStringParser.NZINT, 0); }
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterFactor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitFactor(this);
		}
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_factor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50); match(NZINT);
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
		public TerminalNode NAME() { return getToken(ReactionStringParser.NAME, 0); }
		public PopnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_popname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterPopname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitPopname(this);
		}
	}

	public final PopnameContext popname() throws RecognitionException {
		PopnameContext _localctx = new PopnameContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_popname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52); match(NAME);
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
		public LocelContext locel(int i) {
			return getRuleContext(LocelContext.class,i);
		}
		public List<LocelContext> locel() {
			return getRuleContexts(LocelContext.class);
		}
		public LocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterLoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitLoc(this);
		}
	}

	public final LocContext loc() throws RecognitionException {
		LocContext _localctx = new LocContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_loc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54); match(5);
			setState(55); locel();
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==3) {
				{
				{
				setState(56); match(3);
				setState(57); locel();
				}
				}
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(63); match(2);
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
		public TerminalNode NAME() { return getToken(ReactionStringParser.NAME, 0); }
		public TerminalNode NZINT() { return getToken(ReactionStringParser.NZINT, 0); }
		public LocelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterLocel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitLocel(this);
		}
	}

	public final LocelContext locel() throws RecognitionException {
		LocelContext _localctx = new LocelContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_locel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_la = _input.LA(1);
			if ( !(_la==NZINT || _la==NAME) ) {
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

	public static class IdContext extends ParserRuleContext {
		public TerminalNode NZINT() { return getToken(ReactionStringParser.NZINT, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReactionStringListener ) ((ReactionStringListener)listener).exitId(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); match(NZINT);
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\fH\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\7\5\"\n\5\f\5\16\5%\13\5\3"+
		"\5\5\5(\n\5\3\6\5\6+\n\6\3\6\3\6\5\6/\n\6\3\6\3\6\5\6\63\n\6\3\7\3\7\3"+
		"\b\3\b\3\t\3\t\3\t\3\t\7\t=\n\t\f\t\16\t@\13\t\3\t\3\t\3\n\3\n\3\13\3"+
		"\13\3\13\2\2\f\2\4\6\b\n\f\16\20\22\24\2\3\3\2\n\13C\2\26\3\2\2\2\4\32"+
		"\3\2\2\2\6\34\3\2\2\2\b\'\3\2\2\2\n*\3\2\2\2\f\64\3\2\2\2\16\66\3\2\2"+
		"\2\208\3\2\2\2\22C\3\2\2\2\24E\3\2\2\2\26\27\5\4\3\2\27\30\7\3\2\2\30"+
		"\31\5\6\4\2\31\3\3\2\2\2\32\33\5\b\5\2\33\5\3\2\2\2\34\35\5\b\5\2\35\7"+
		"\3\2\2\2\36#\5\n\6\2\37 \7\6\2\2 \"\5\n\6\2!\37\3\2\2\2\"%\3\2\2\2#!\3"+
		"\2\2\2#$\3\2\2\2$(\3\2\2\2%#\3\2\2\2&(\7\t\2\2\'\36\3\2\2\2\'&\3\2\2\2"+
		"(\t\3\2\2\2)+\5\f\7\2*)\3\2\2\2*+\3\2\2\2+,\3\2\2\2,.\5\16\b\2-/\5\20"+
		"\t\2.-\3\2\2\2./\3\2\2\2/\62\3\2\2\2\60\61\7\b\2\2\61\63\5\24\13\2\62"+
		"\60\3\2\2\2\62\63\3\2\2\2\63\13\3\2\2\2\64\65\7\n\2\2\65\r\3\2\2\2\66"+
		"\67\7\13\2\2\67\17\3\2\2\289\7\7\2\29>\5\22\n\2:;\7\5\2\2;=\5\22\n\2<"+
		":\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3\2\2\2@>\3\2\2\2AB\7\4\2\2"+
		"B\21\3\2\2\2CD\t\2\2\2D\23\3\2\2\2EF\7\n\2\2F\25\3\2\2\2\b#\'*.\62>";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}