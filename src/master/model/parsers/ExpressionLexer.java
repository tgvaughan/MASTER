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
		GE=28, LE=29, NE=30, NNINT=31, NNFLOAT=32, VARNAME=33, WHITESPACE=34;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"']'", "')'", "','", "'['", "'('", "':'", "'?'", "'{'", "'}'", "'!'", 
		"'+'", "'-'", "'*'", "'/'", "'%'", "'^'", "'exp'", "'log'", "'sqrt'", 
		"'sum'", "'theta'", "'abs'", "'&&'", "'||'", "'=='", "'>'", "'<'", "'>='", 
		"'<='", "'!='", "NNINT", "NNFLOAT", "VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "ADD", "SUB", "MUL", "DIV", "MOD", "POW", "EXP", "LOG", "SQRT", 
		"SUM", "THETA", "ABS", "AND", "OR", "EQ", "GT", "LT", "GE", "LE", "NE", 
		"NNINT", "NNFLOAT", "D", "NZD", "VARNAME", "WHITESPACE"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2$\u00cb\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6"+
		"\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3"+
		"\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3"+
		"\33\3\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 "+
		"\3 \7 \u00a0\n \f \16 \u00a3\13 \5 \u00a5\n \3!\3!\3!\7!\u00aa\n!\f!\16"+
		"!\u00ad\13!\3!\3!\5!\u00b1\n!\3!\6!\u00b4\n!\r!\16!\u00b5\5!\u00b8\n!"+
		"\3\"\3\"\3#\3#\3$\3$\7$\u00c0\n$\f$\16$\u00c3\13$\3%\6%\u00c6\n%\r%\16"+
		"%\u00c7\3%\3%\2\2&\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r"+
		"\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C\2E\2G#I$\3\2\b\4\2GGgg\3\2\62;\3\2\63;\5"+
		"\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\u00d0\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\3K\3\2\2\2\5M\3\2\2\2\7O\3\2\2"+
		"\2\tQ\3\2\2\2\13S\3\2\2\2\rU\3\2\2\2\17W\3\2\2\2\21Y\3\2\2\2\23[\3\2\2"+
		"\2\25]\3\2\2\2\27_\3\2\2\2\31a\3\2\2\2\33c\3\2\2\2\35e\3\2\2\2\37g\3\2"+
		"\2\2!i\3\2\2\2#k\3\2\2\2%o\3\2\2\2\'s\3\2\2\2)x\3\2\2\2+|\3\2\2\2-\u0082"+
		"\3\2\2\2/\u0086\3\2\2\2\61\u0089\3\2\2\2\63\u008c\3\2\2\2\65\u008f\3\2"+
		"\2\2\67\u0091\3\2\2\29\u0093\3\2\2\2;\u0096\3\2\2\2=\u0099\3\2\2\2?\u00a4"+
		"\3\2\2\2A\u00a6\3\2\2\2C\u00b9\3\2\2\2E\u00bb\3\2\2\2G\u00bd\3\2\2\2I"+
		"\u00c5\3\2\2\2KL\7_\2\2L\4\3\2\2\2MN\7+\2\2N\6\3\2\2\2OP\7.\2\2P\b\3\2"+
		"\2\2QR\7]\2\2R\n\3\2\2\2ST\7*\2\2T\f\3\2\2\2UV\7<\2\2V\16\3\2\2\2WX\7"+
		"A\2\2X\20\3\2\2\2YZ\7}\2\2Z\22\3\2\2\2[\\\7\177\2\2\\\24\3\2\2\2]^\7#"+
		"\2\2^\26\3\2\2\2_`\7-\2\2`\30\3\2\2\2ab\7/\2\2b\32\3\2\2\2cd\7,\2\2d\34"+
		"\3\2\2\2ef\7\61\2\2f\36\3\2\2\2gh\7\'\2\2h \3\2\2\2ij\7`\2\2j\"\3\2\2"+
		"\2kl\7g\2\2lm\7z\2\2mn\7r\2\2n$\3\2\2\2op\7n\2\2pq\7q\2\2qr\7i\2\2r&\3"+
		"\2\2\2st\7u\2\2tu\7s\2\2uv\7t\2\2vw\7v\2\2w(\3\2\2\2xy\7u\2\2yz\7w\2\2"+
		"z{\7o\2\2{*\3\2\2\2|}\7v\2\2}~\7j\2\2~\177\7g\2\2\177\u0080\7v\2\2\u0080"+
		"\u0081\7c\2\2\u0081,\3\2\2\2\u0082\u0083\7c\2\2\u0083\u0084\7d\2\2\u0084"+
		"\u0085\7u\2\2\u0085.\3\2\2\2\u0086\u0087\7(\2\2\u0087\u0088\7(\2\2\u0088"+
		"\60\3\2\2\2\u0089\u008a\7~\2\2\u008a\u008b\7~\2\2\u008b\62\3\2\2\2\u008c"+
		"\u008d\7?\2\2\u008d\u008e\7?\2\2\u008e\64\3\2\2\2\u008f\u0090\7@\2\2\u0090"+
		"\66\3\2\2\2\u0091\u0092\7>\2\2\u00928\3\2\2\2\u0093\u0094\7@\2\2\u0094"+
		"\u0095\7?\2\2\u0095:\3\2\2\2\u0096\u0097\7>\2\2\u0097\u0098\7?\2\2\u0098"+
		"<\3\2\2\2\u0099\u009a\7#\2\2\u009a\u009b\7?\2\2\u009b>\3\2\2\2\u009c\u00a5"+
		"\7\62\2\2\u009d\u00a1\5E#\2\u009e\u00a0\5C\"\2\u009f\u009e\3\2\2\2\u00a0"+
		"\u00a3\3\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a5\3\2"+
		"\2\2\u00a3\u00a1\3\2\2\2\u00a4\u009c\3\2\2\2\u00a4\u009d\3\2\2\2\u00a5"+
		"@\3\2\2\2\u00a6\u00a7\5? \2\u00a7\u00ab\7\60\2\2\u00a8\u00aa\5C\"\2\u00a9"+
		"\u00a8\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2"+
		"\2\2\u00ac\u00b7\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ae\u00b0\t\2\2\2\u00af"+
		"\u00b1\7/\2\2\u00b0\u00af\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b3\3\2"+
		"\2\2\u00b2\u00b4\5C\"\2\u00b3\u00b2\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5"+
		"\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00ae\3\2"+
		"\2\2\u00b7\u00b8\3\2\2\2\u00b8B\3\2\2\2\u00b9\u00ba\t\3\2\2\u00baD\3\2"+
		"\2\2\u00bb\u00bc\t\4\2\2\u00bcF\3\2\2\2\u00bd\u00c1\t\5\2\2\u00be\u00c0"+
		"\t\6\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c1"+
		"\u00c2\3\2\2\2\u00c2H\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c4\u00c6\t\7\2\2"+
		"\u00c5\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8"+
		"\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00ca\b%\2\2\u00caJ\3\2\2\2\13\2\u00a1"+
		"\u00a4\u00ab\u00b0\u00b5\u00b7\u00c1\u00c7\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}