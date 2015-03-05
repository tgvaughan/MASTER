// Generated from ReactionString.g4 by ANTLR 4.2
package master.model.parsers;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ReactionStringLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__6=1, T__5=2, T__4=3, T__3=4, T__2=5, T__1=6, T__0=7, NZINT=8, NAME=9, 
		WHITESPACE=10;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'->'", "']'", "','", "'+'", "'['", "':'", "'0'", "NZINT", "NAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "NZINT", "Z", 
		"D", "NZD", "NAME", "WHITESPACE"
	};


	public ReactionStringLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ReactionString.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\fG\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\3\t\3\t\7\t/\n\t\f\t\16\t\62\13\t\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\3\r\3\r\7\r<\n\r\f\r\16\r?\13\r\3\16\6\16B\n\16\r\16\16\16"+
		"C\3\16\3\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\2\25\2\27\2\31"+
		"\13\33\f\3\2\7\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17"+
		"\"\"F\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r"+
		"\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\35\3\2"+
		"\2\2\5 \3\2\2\2\7\"\3\2\2\2\t$\3\2\2\2\13&\3\2\2\2\r(\3\2\2\2\17*\3\2"+
		"\2\2\21,\3\2\2\2\23\63\3\2\2\2\25\65\3\2\2\2\27\67\3\2\2\2\319\3\2\2\2"+
		"\33A\3\2\2\2\35\36\7/\2\2\36\37\7@\2\2\37\4\3\2\2\2 !\7_\2\2!\6\3\2\2"+
		"\2\"#\7.\2\2#\b\3\2\2\2$%\7-\2\2%\n\3\2\2\2&\'\7]\2\2\'\f\3\2\2\2()\7"+
		"<\2\2)\16\3\2\2\2*+\7\62\2\2+\20\3\2\2\2,\60\5\27\f\2-/\5\25\13\2.-\3"+
		"\2\2\2/\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\22\3\2\2\2\62\60\3\2\2"+
		"\2\63\64\7\62\2\2\64\24\3\2\2\2\65\66\t\2\2\2\66\26\3\2\2\2\678\t\3\2"+
		"\28\30\3\2\2\29=\t\4\2\2:<\t\5\2\2;:\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2"+
		"\2\2>\32\3\2\2\2?=\3\2\2\2@B\t\6\2\2A@\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3"+
		"\2\2\2DE\3\2\2\2EF\b\16\2\2F\34\3\2\2\2\6\2\60=C\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}