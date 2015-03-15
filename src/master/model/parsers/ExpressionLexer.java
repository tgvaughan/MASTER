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
		SUB=10, MUL=11, DIV=12, POW=13, EXP=14, LOG=15, SQRT=16, SUM=17, THETA=18, 
		ABS=19, AND=20, OR=21, EQ=22, GT=23, LT=24, GE=25, LE=26, NE=27, NNINT=28, 
		NNFLOAT=29, VARNAME=30, WHITESPACE=31;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"')'", "','", "'('", "':'", "'?'", "'{'", "'}'", "'!'", "'+'", "'-'", 
		"'*'", "'/'", "'^'", "'exp'", "'log'", "'sqrt'", "'sum'", "'theta'", "'abs'", 
		"'&&'", "'||'", "'=='", "'>'", "'<'", "'>='", "'<='", "'!='", "NNINT", 
		"NNFLOAT", "VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "ADD", 
		"SUB", "MUL", "DIV", "POW", "EXP", "LOG", "SQRT", "SUM", "THETA", "ABS", 
		"AND", "OR", "EQ", "GT", "LT", "GE", "LE", "NE", "NNINT", "NNFLOAT", "D", 
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2!\u00bf\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\7\35\u0094\n\35\f\35\16\35\u0097\13\35"+
		"\5\35\u0099\n\35\3\36\3\36\3\36\7\36\u009e\n\36\f\36\16\36\u00a1\13\36"+
		"\3\36\3\36\5\36\u00a5\n\36\3\36\6\36\u00a8\n\36\r\36\16\36\u00a9\5\36"+
		"\u00ac\n\36\3\37\3\37\3 \3 \3!\3!\7!\u00b4\n!\f!\16!\u00b7\13!\3\"\6\""+
		"\u00ba\n\"\r\"\16\"\u00bb\3\"\3\"\2\2#\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37=\2?\2A C!\3\2\b\4\2GGgg\3\2\62;\3"+
		"\2\63;\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\u00c4\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"+
		"\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2A\3\2"+
		"\2\2\2C\3\2\2\2\3E\3\2\2\2\5G\3\2\2\2\7I\3\2\2\2\tK\3\2\2\2\13M\3\2\2"+
		"\2\rO\3\2\2\2\17Q\3\2\2\2\21S\3\2\2\2\23U\3\2\2\2\25W\3\2\2\2\27Y\3\2"+
		"\2\2\31[\3\2\2\2\33]\3\2\2\2\35_\3\2\2\2\37c\3\2\2\2!g\3\2\2\2#l\3\2\2"+
		"\2%p\3\2\2\2\'v\3\2\2\2)z\3\2\2\2+}\3\2\2\2-\u0080\3\2\2\2/\u0083\3\2"+
		"\2\2\61\u0085\3\2\2\2\63\u0087\3\2\2\2\65\u008a\3\2\2\2\67\u008d\3\2\2"+
		"\29\u0098\3\2\2\2;\u009a\3\2\2\2=\u00ad\3\2\2\2?\u00af\3\2\2\2A\u00b1"+
		"\3\2\2\2C\u00b9\3\2\2\2EF\7+\2\2F\4\3\2\2\2GH\7.\2\2H\6\3\2\2\2IJ\7*\2"+
		"\2J\b\3\2\2\2KL\7<\2\2L\n\3\2\2\2MN\7A\2\2N\f\3\2\2\2OP\7}\2\2P\16\3\2"+
		"\2\2QR\7\177\2\2R\20\3\2\2\2ST\7#\2\2T\22\3\2\2\2UV\7-\2\2V\24\3\2\2\2"+
		"WX\7/\2\2X\26\3\2\2\2YZ\7,\2\2Z\30\3\2\2\2[\\\7\61\2\2\\\32\3\2\2\2]^"+
		"\7`\2\2^\34\3\2\2\2_`\7g\2\2`a\7z\2\2ab\7r\2\2b\36\3\2\2\2cd\7n\2\2de"+
		"\7q\2\2ef\7i\2\2f \3\2\2\2gh\7u\2\2hi\7s\2\2ij\7t\2\2jk\7v\2\2k\"\3\2"+
		"\2\2lm\7u\2\2mn\7w\2\2no\7o\2\2o$\3\2\2\2pq\7v\2\2qr\7j\2\2rs\7g\2\2s"+
		"t\7v\2\2tu\7c\2\2u&\3\2\2\2vw\7c\2\2wx\7d\2\2xy\7u\2\2y(\3\2\2\2z{\7("+
		"\2\2{|\7(\2\2|*\3\2\2\2}~\7~\2\2~\177\7~\2\2\177,\3\2\2\2\u0080\u0081"+
		"\7?\2\2\u0081\u0082\7?\2\2\u0082.\3\2\2\2\u0083\u0084\7@\2\2\u0084\60"+
		"\3\2\2\2\u0085\u0086\7>\2\2\u0086\62\3\2\2\2\u0087\u0088\7@\2\2\u0088"+
		"\u0089\7?\2\2\u0089\64\3\2\2\2\u008a\u008b\7>\2\2\u008b\u008c\7?\2\2\u008c"+
		"\66\3\2\2\2\u008d\u008e\7#\2\2\u008e\u008f\7?\2\2\u008f8\3\2\2\2\u0090"+
		"\u0099\7\62\2\2\u0091\u0095\5? \2\u0092\u0094\5=\37\2\u0093\u0092\3\2"+
		"\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096"+
		"\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u0090\3\2\2\2\u0098\u0091\3\2"+
		"\2\2\u0099:\3\2\2\2\u009a\u009b\59\35\2\u009b\u009f\7\60\2\2\u009c\u009e"+
		"\5=\37\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u009d\3\2\2\2\u009f"+
		"\u00a0\3\2\2\2\u00a0\u00ab\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2\u00a4\t\2"+
		"\2\2\u00a3\u00a5\7/\2\2\u00a4\u00a3\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5"+
		"\u00a7\3\2\2\2\u00a6\u00a8\5=\37\2\u00a7\u00a6\3\2\2\2\u00a8\u00a9\3\2"+
		"\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ac\3\2\2\2\u00ab"+
		"\u00a2\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac<\3\2\2\2\u00ad\u00ae\t\3\2\2"+
		"\u00ae>\3\2\2\2\u00af\u00b0\t\4\2\2\u00b0@\3\2\2\2\u00b1\u00b5\t\5\2\2"+
		"\u00b2\u00b4\t\6\2\2\u00b3\u00b2\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3"+
		"\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6B\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8"+
		"\u00ba\t\7\2\2\u00b9\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00b9\3\2"+
		"\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00be\b\"\2\2\u00be"+
		"D\3\2\2\2\13\2\u0095\u0098\u009f\u00a4\u00a9\u00ab\u00b5\u00bb\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}