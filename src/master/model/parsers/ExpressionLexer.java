// Generated from Expression.g4 by ANTLR 4.2
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
public class ExpressionLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__4=1, T__3=2, T__2=3, T__1=4, T__0=5, ADD=6, SUB=7, MUL=8, DIV=9, POW=10, 
		EXP=11, LOG=12, SQRT=13, SUM=14, THETA=15, ABS=16, NNINT=17, NNFLOAT=18, 
		VARNAME=19, WHITESPACE=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"']'", "')'", "','", "'['", "'('", "'+'", "'-'", "'*'", "'/'", "'^'", 
		"'exp'", "'log'", "'sqrt'", "'sum'", "'theta'", "'abs'", "NNINT", "NNFLOAT", 
		"VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__4", "T__3", "T__2", "T__1", "T__0", "ADD", "SUB", "MUL", "DIV", "POW", 
		"EXP", "LOG", "SQRT", "SUM", "THETA", "ABS", "NNINT", "NNFLOAT", "D", 
		"NZD", "VARNAME", "WHITESPACE"
	};


	public ExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26\u008d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\3\3"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22"+
		"\7\22b\n\22\f\22\16\22e\13\22\5\22g\n\22\3\23\3\23\3\23\7\23l\n\23\f\23"+
		"\16\23o\13\23\3\23\3\23\5\23s\n\23\3\23\6\23v\n\23\r\23\16\23w\5\23z\n"+
		"\23\3\24\3\24\3\25\3\25\3\26\3\26\7\26\u0082\n\26\f\26\16\26\u0085\13"+
		"\26\3\27\6\27\u0088\n\27\r\27\16\27\u0089\3\27\3\27\2\2\30\3\3\5\4\7\5"+
		"\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\2)\2+\25-\26\3\2\b\4\2GGgg\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62;C"+
		"\\aac|\5\2\13\f\17\17\"\"\u0092\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\3"+
		"/\3\2\2\2\5\61\3\2\2\2\7\63\3\2\2\2\t\65\3\2\2\2\13\67\3\2\2\2\r9\3\2"+
		"\2\2\17;\3\2\2\2\21=\3\2\2\2\23?\3\2\2\2\25A\3\2\2\2\27C\3\2\2\2\31G\3"+
		"\2\2\2\33K\3\2\2\2\35P\3\2\2\2\37T\3\2\2\2!Z\3\2\2\2#f\3\2\2\2%h\3\2\2"+
		"\2\'{\3\2\2\2)}\3\2\2\2+\177\3\2\2\2-\u0087\3\2\2\2/\60\7_\2\2\60\4\3"+
		"\2\2\2\61\62\7+\2\2\62\6\3\2\2\2\63\64\7.\2\2\64\b\3\2\2\2\65\66\7]\2"+
		"\2\66\n\3\2\2\2\678\7*\2\28\f\3\2\2\29:\7-\2\2:\16\3\2\2\2;<\7/\2\2<\20"+
		"\3\2\2\2=>\7,\2\2>\22\3\2\2\2?@\7\61\2\2@\24\3\2\2\2AB\7`\2\2B\26\3\2"+
		"\2\2CD\7g\2\2DE\7z\2\2EF\7r\2\2F\30\3\2\2\2GH\7n\2\2HI\7q\2\2IJ\7i\2\2"+
		"J\32\3\2\2\2KL\7u\2\2LM\7s\2\2MN\7t\2\2NO\7v\2\2O\34\3\2\2\2PQ\7u\2\2"+
		"QR\7w\2\2RS\7o\2\2S\36\3\2\2\2TU\7v\2\2UV\7j\2\2VW\7g\2\2WX\7v\2\2XY\7"+
		"c\2\2Y \3\2\2\2Z[\7c\2\2[\\\7d\2\2\\]\7u\2\2]\"\3\2\2\2^g\7\62\2\2_c\5"+
		")\25\2`b\5\'\24\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2dg\3\2\2\2ec"+
		"\3\2\2\2f^\3\2\2\2f_\3\2\2\2g$\3\2\2\2hi\5#\22\2im\7\60\2\2jl\5\'\24\2"+
		"kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2ny\3\2\2\2om\3\2\2\2pr\t\2\2\2"+
		"qs\7/\2\2rq\3\2\2\2rs\3\2\2\2su\3\2\2\2tv\5\'\24\2ut\3\2\2\2vw\3\2\2\2"+
		"wu\3\2\2\2wx\3\2\2\2xz\3\2\2\2yp\3\2\2\2yz\3\2\2\2z&\3\2\2\2{|\t\3\2\2"+
		"|(\3\2\2\2}~\t\4\2\2~*\3\2\2\2\177\u0083\t\5\2\2\u0080\u0082\t\6\2\2\u0081"+
		"\u0080\3\2\2\2\u0082\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2"+
		"\2\2\u0084,\3\2\2\2\u0085\u0083\3\2\2\2\u0086\u0088\t\7\2\2\u0087\u0086"+
		"\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"\u008b\3\2\2\2\u008b\u008c\b\27\2\2\u008c.\3\2\2\2\13\2cfmrwy\u0083\u0089"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}