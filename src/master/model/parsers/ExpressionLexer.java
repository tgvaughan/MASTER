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
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, ADD=9, 
		SUB=10, MUL=11, DIV=12, MOD=13, POW=14, EXP=15, LOG=16, SQRT=17, SUM=18, 
		THETA=19, ABS=20, AND=21, OR=22, EQ=23, GT=24, LT=25, GE=26, LE=27, NE=28, 
		NNINT=29, NNFLOAT=30, VARNAME=31, WHITESPACE=32;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"')'", "','", "'('", "':'", "'?'", "'{'", "'}'", "'!'", "'+'", "'-'", 
		"'*'", "'/'", "'%'", "'^'", "'exp'", "'log'", "'sqrt'", "'sum'", "'theta'", 
		"'abs'", "'&&'", "'||'", "'=='", "'>'", "'<'", "'>='", "'<='", "'!='", 
		"NNINT", "NNFLOAT", "VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "ADD", 
		"SUB", "MUL", "DIV", "MOD", "POW", "EXP", "LOG", "SQRT", "SUM", "THETA", 
		"ABS", "AND", "OR", "EQ", "GT", "LT", "GE", "LE", "NE", "NNINT", "NNFLOAT", 
		"D", "NZD", "VARNAME", "WHITESPACE"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\"\u00c3\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20"+
		"\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\7\36\u0098\n\36\f\36\16"+
		"\36\u009b\13\36\5\36\u009d\n\36\3\37\3\37\3\37\7\37\u00a2\n\37\f\37\16"+
		"\37\u00a5\13\37\3\37\3\37\5\37\u00a9\n\37\3\37\6\37\u00ac\n\37\r\37\16"+
		"\37\u00ad\5\37\u00b0\n\37\3 \3 \3!\3!\3\"\3\"\7\"\u00b8\n\"\f\"\16\"\u00bb"+
		"\13\"\3#\6#\u00be\n#\r#\16#\u00bf\3#\3#\2\2$\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26"+
		"+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?\2A\2C!E\"\3\2\b\4\2GG"+
		"gg\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\u00c8"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\3G\3\2\2\2\5I\3\2\2\2\7K\3\2\2"+
		"\2\tM\3\2\2\2\13O\3\2\2\2\rQ\3\2\2\2\17S\3\2\2\2\21U\3\2\2\2\23W\3\2\2"+
		"\2\25Y\3\2\2\2\27[\3\2\2\2\31]\3\2\2\2\33_\3\2\2\2\35a\3\2\2\2\37c\3\2"+
		"\2\2!g\3\2\2\2#k\3\2\2\2%p\3\2\2\2\'t\3\2\2\2)z\3\2\2\2+~\3\2\2\2-\u0081"+
		"\3\2\2\2/\u0084\3\2\2\2\61\u0087\3\2\2\2\63\u0089\3\2\2\2\65\u008b\3\2"+
		"\2\2\67\u008e\3\2\2\29\u0091\3\2\2\2;\u009c\3\2\2\2=\u009e\3\2\2\2?\u00b1"+
		"\3\2\2\2A\u00b3\3\2\2\2C\u00b5\3\2\2\2E\u00bd\3\2\2\2GH\7+\2\2H\4\3\2"+
		"\2\2IJ\7.\2\2J\6\3\2\2\2KL\7*\2\2L\b\3\2\2\2MN\7<\2\2N\n\3\2\2\2OP\7A"+
		"\2\2P\f\3\2\2\2QR\7}\2\2R\16\3\2\2\2ST\7\177\2\2T\20\3\2\2\2UV\7#\2\2"+
		"V\22\3\2\2\2WX\7-\2\2X\24\3\2\2\2YZ\7/\2\2Z\26\3\2\2\2[\\\7,\2\2\\\30"+
		"\3\2\2\2]^\7\61\2\2^\32\3\2\2\2_`\7\'\2\2`\34\3\2\2\2ab\7`\2\2b\36\3\2"+
		"\2\2cd\7g\2\2de\7z\2\2ef\7r\2\2f \3\2\2\2gh\7n\2\2hi\7q\2\2ij\7i\2\2j"+
		"\"\3\2\2\2kl\7u\2\2lm\7s\2\2mn\7t\2\2no\7v\2\2o$\3\2\2\2pq\7u\2\2qr\7"+
		"w\2\2rs\7o\2\2s&\3\2\2\2tu\7v\2\2uv\7j\2\2vw\7g\2\2wx\7v\2\2xy\7c\2\2"+
		"y(\3\2\2\2z{\7c\2\2{|\7d\2\2|}\7u\2\2}*\3\2\2\2~\177\7(\2\2\177\u0080"+
		"\7(\2\2\u0080,\3\2\2\2\u0081\u0082\7~\2\2\u0082\u0083\7~\2\2\u0083.\3"+
		"\2\2\2\u0084\u0085\7?\2\2\u0085\u0086\7?\2\2\u0086\60\3\2\2\2\u0087\u0088"+
		"\7@\2\2\u0088\62\3\2\2\2\u0089\u008a\7>\2\2\u008a\64\3\2\2\2\u008b\u008c"+
		"\7@\2\2\u008c\u008d\7?\2\2\u008d\66\3\2\2\2\u008e\u008f\7>\2\2\u008f\u0090"+
		"\7?\2\2\u00908\3\2\2\2\u0091\u0092\7#\2\2\u0092\u0093\7?\2\2\u0093:\3"+
		"\2\2\2\u0094\u009d\7\62\2\2\u0095\u0099\5A!\2\u0096\u0098\5? \2\u0097"+
		"\u0096\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2"+
		"\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009c\u0094\3\2\2\2\u009c"+
		"\u0095\3\2\2\2\u009d<\3\2\2\2\u009e\u009f\5;\36\2\u009f\u00a3\7\60\2\2"+
		"\u00a0\u00a2\5? \2\u00a1\u00a0\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1"+
		"\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00af\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6"+
		"\u00a8\t\2\2\2\u00a7\u00a9\7/\2\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2"+
		"\2\2\u00a9\u00ab\3\2\2\2\u00aa\u00ac\5? \2\u00ab\u00aa\3\2\2\2\u00ac\u00ad"+
		"\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af"+
		"\u00a6\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0>\3\2\2\2\u00b1\u00b2\t\3\2\2"+
		"\u00b2@\3\2\2\2\u00b3\u00b4\t\4\2\2\u00b4B\3\2\2\2\u00b5\u00b9\t\5\2\2"+
		"\u00b6\u00b8\t\6\2\2\u00b7\u00b6\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00b7"+
		"\3\2\2\2\u00b9\u00ba\3\2\2\2\u00baD\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc"+
		"\u00be\t\7\2\2\u00bd\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00bd\3\2"+
		"\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\b#\2\2\u00c2"+
		"F\3\2\2\2\13\2\u0099\u009c\u00a3\u00a8\u00ad\u00af\u00b9\u00bf\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}