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
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, ADD=11, SUB=12, MUL=13, DIV=14, MOD=15, POW=16, EXP=17, LOG=18, 
		SQRT=19, SUM=20, THETA=21, ABS=22, AND=23, OR=24, EQ=25, GT=26, LT=27, 
		GE=28, LE=29, NE=30, NNINT=31, NNFLOAT=32, IDENT=33, COMMENT_SINGLELINE=34, 
		COMMENT_MULTILINE=35, WHITESPACE=36;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"']'", "')'", "','", "'['", "'('", "':'", "'?'", "'{'", "'}'", "'!'", 
		"'+'", "'-'", "'*'", "'/'", "'%'", "'^'", "'exp'", "'log'", "'sqrt'", 
		"'sum'", "'theta'", "'abs'", "'&&'", "'||'", "'=='", "'>'", "'<'", "'>='", 
		"'<='", "'!='", "NNINT", "NNFLOAT", "IDENT", "COMMENT_SINGLELINE", "COMMENT_MULTILINE", 
		"WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "ADD", "SUB", "MUL", "DIV", "MOD", "POW", "EXP", "LOG", "SQRT", 
		"SUM", "THETA", "ABS", "AND", "OR", "EQ", "GT", "LT", "GE", "LE", "NE", 
		"NNINT", "NNFLOAT", "D", "NZD", "IDENT", "COMMENT_SINGLELINE", "COMMENT_MULTILINE", 
		"WHITESPACE"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2&\u00ea\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\3\3\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3"+
		"\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3"+
		"\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3"+
		"\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3"+
		"\37\3\37\3 \3 \3 \7 \u00a4\n \f \16 \u00a7\13 \5 \u00a9\n \3!\3!\3!\7"+
		"!\u00ae\n!\f!\16!\u00b1\13!\3!\3!\5!\u00b5\n!\3!\6!\u00b8\n!\r!\16!\u00b9"+
		"\5!\u00bc\n!\3\"\3\"\3#\3#\3$\3$\7$\u00c4\n$\f$\16$\u00c7\13$\3%\3%\3"+
		"%\3%\7%\u00cd\n%\f%\16%\u00d0\13%\3%\3%\3%\3%\3&\3&\3&\3&\7&\u00da\n&"+
		"\f&\16&\u00dd\13&\3&\3&\3&\3&\3&\3\'\6\'\u00e5\n\'\r\'\16\'\u00e6\3\'"+
		"\3\'\4\u00ce\u00db\2(\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C\2E\2G#I$K%M&\3\2\b\4\2GGgg\3\2\62;\3\2\63"+
		";\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\u00f1\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63"+
		"\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2"+
		"?\3\2\2\2\2A\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\3O\3"+
		"\2\2\2\5Q\3\2\2\2\7S\3\2\2\2\tU\3\2\2\2\13W\3\2\2\2\rY\3\2\2\2\17[\3\2"+
		"\2\2\21]\3\2\2\2\23_\3\2\2\2\25a\3\2\2\2\27c\3\2\2\2\31e\3\2\2\2\33g\3"+
		"\2\2\2\35i\3\2\2\2\37k\3\2\2\2!m\3\2\2\2#o\3\2\2\2%s\3\2\2\2\'w\3\2\2"+
		"\2)|\3\2\2\2+\u0080\3\2\2\2-\u0086\3\2\2\2/\u008a\3\2\2\2\61\u008d\3\2"+
		"\2\2\63\u0090\3\2\2\2\65\u0093\3\2\2\2\67\u0095\3\2\2\29\u0097\3\2\2\2"+
		";\u009a\3\2\2\2=\u009d\3\2\2\2?\u00a8\3\2\2\2A\u00aa\3\2\2\2C\u00bd\3"+
		"\2\2\2E\u00bf\3\2\2\2G\u00c1\3\2\2\2I\u00c8\3\2\2\2K\u00d5\3\2\2\2M\u00e4"+
		"\3\2\2\2OP\7_\2\2P\4\3\2\2\2QR\7+\2\2R\6\3\2\2\2ST\7.\2\2T\b\3\2\2\2U"+
		"V\7]\2\2V\n\3\2\2\2WX\7*\2\2X\f\3\2\2\2YZ\7<\2\2Z\16\3\2\2\2[\\\7A\2\2"+
		"\\\20\3\2\2\2]^\7}\2\2^\22\3\2\2\2_`\7\177\2\2`\24\3\2\2\2ab\7#\2\2b\26"+
		"\3\2\2\2cd\7-\2\2d\30\3\2\2\2ef\7/\2\2f\32\3\2\2\2gh\7,\2\2h\34\3\2\2"+
		"\2ij\7\61\2\2j\36\3\2\2\2kl\7\'\2\2l \3\2\2\2mn\7`\2\2n\"\3\2\2\2op\7"+
		"g\2\2pq\7z\2\2qr\7r\2\2r$\3\2\2\2st\7n\2\2tu\7q\2\2uv\7i\2\2v&\3\2\2\2"+
		"wx\7u\2\2xy\7s\2\2yz\7t\2\2z{\7v\2\2{(\3\2\2\2|}\7u\2\2}~\7w\2\2~\177"+
		"\7o\2\2\177*\3\2\2\2\u0080\u0081\7v\2\2\u0081\u0082\7j\2\2\u0082\u0083"+
		"\7g\2\2\u0083\u0084\7v\2\2\u0084\u0085\7c\2\2\u0085,\3\2\2\2\u0086\u0087"+
		"\7c\2\2\u0087\u0088\7d\2\2\u0088\u0089\7u\2\2\u0089.\3\2\2\2\u008a\u008b"+
		"\7(\2\2\u008b\u008c\7(\2\2\u008c\60\3\2\2\2\u008d\u008e\7~\2\2\u008e\u008f"+
		"\7~\2\2\u008f\62\3\2\2\2\u0090\u0091\7?\2\2\u0091\u0092\7?\2\2\u0092\64"+
		"\3\2\2\2\u0093\u0094\7@\2\2\u0094\66\3\2\2\2\u0095\u0096\7>\2\2\u0096"+
		"8\3\2\2\2\u0097\u0098\7@\2\2\u0098\u0099\7?\2\2\u0099:\3\2\2\2\u009a\u009b"+
		"\7>\2\2\u009b\u009c\7?\2\2\u009c<\3\2\2\2\u009d\u009e\7#\2\2\u009e\u009f"+
		"\7?\2\2\u009f>\3\2\2\2\u00a0\u00a9\7\62\2\2\u00a1\u00a5\5E#\2\u00a2\u00a4"+
		"\5C\"\2\u00a3\u00a2\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00a0\3\2"+
		"\2\2\u00a8\u00a1\3\2\2\2\u00a9@\3\2\2\2\u00aa\u00ab\5? \2\u00ab\u00af"+
		"\7\60\2\2\u00ac\u00ae\5C\"\2\u00ad\u00ac\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00bb\3\2\2\2\u00b1\u00af\3\2"+
		"\2\2\u00b2\u00b4\t\2\2\2\u00b3\u00b5\7/\2\2\u00b4\u00b3\3\2\2\2\u00b4"+
		"\u00b5\3\2\2\2\u00b5\u00b7\3\2\2\2\u00b6\u00b8\5C\"\2\u00b7\u00b6\3\2"+
		"\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba"+
		"\u00bc\3\2\2\2\u00bb\u00b2\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bcB\3\2\2\2"+
		"\u00bd\u00be\t\3\2\2\u00beD\3\2\2\2\u00bf\u00c0\t\4\2\2\u00c0F\3\2\2\2"+
		"\u00c1\u00c5\t\5\2\2\u00c2\u00c4\t\6\2\2\u00c3\u00c2\3\2\2\2\u00c4\u00c7"+
		"\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6H\3\2\2\2\u00c7"+
		"\u00c5\3\2\2\2\u00c8\u00c9\7\61\2\2\u00c9\u00ca\7\61\2\2\u00ca\u00ce\3"+
		"\2\2\2\u00cb\u00cd\13\2\2\2\u00cc\u00cb\3\2\2\2\u00cd\u00d0\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00d1\3\2\2\2\u00d0\u00ce\3\2"+
		"\2\2\u00d1\u00d2\7\f\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d4\b%\2\2\u00d4"+
		"J\3\2\2\2\u00d5\u00d6\7\61\2\2\u00d6\u00d7\7,\2\2\u00d7\u00db\3\2\2\2"+
		"\u00d8\u00da\13\2\2\2\u00d9\u00d8\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00dc"+
		"\3\2\2\2\u00db\u00d9\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de"+
		"\u00df\7,\2\2\u00df\u00e0\7\61\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e2\b&"+
		"\2\2\u00e2L\3\2\2\2\u00e3\u00e5\t\7\2\2\u00e4\u00e3\3\2\2\2\u00e5\u00e6"+
		"\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8"+
		"\u00e9\b\'\2\2\u00e9N\3\2\2\2\r\2\u00a5\u00a8\u00af\u00b4\u00b9\u00bb"+
		"\u00c5\u00ce\u00db\u00e6\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}