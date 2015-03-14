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
		T__8=1, T__7=2, T__6=3, T__5=4, T__4=5, T__3=6, T__2=7, T__1=8, T__0=9, 
		ADD=10, SUB=11, MUL=12, DIV=13, POW=14, EXP=15, LOG=16, SQRT=17, SUM=18, 
		THETA=19, ABS=20, EQ=21, GT=22, LT=23, GE=24, LE=25, NE=26, NNINT=27, 
		NNFLOAT=28, VARNAME=29, WHITESPACE=30;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"')'", "','", "'('", "'&&'", "'||'", "'{'", "'=='", "'}'", "'!'", "'+'", 
		"'-'", "'*'", "'/'", "'^'", "'exp'", "'log'", "'sqrt'", "'sum'", "'theta'", 
		"'abs'", "'='", "'>'", "'<'", "'>='", "'<='", "'!='", "NNINT", "NNFLOAT", 
		"VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", 
		"ADD", "SUB", "MUL", "DIV", "POW", "EXP", "LOG", "SQRT", "SUM", "THETA", 
		"ABS", "EQ", "GT", "LT", "GE", "LE", "NE", "NNINT", "NNFLOAT", "D", "NZD", 
		"VARNAME", "WHITESPACE"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2 \u00bb\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20"+
		"\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\34"+
		"\3\34\3\34\7\34\u0090\n\34\f\34\16\34\u0093\13\34\5\34\u0095\n\34\3\35"+
		"\3\35\3\35\7\35\u009a\n\35\f\35\16\35\u009d\13\35\3\35\3\35\5\35\u00a1"+
		"\n\35\3\35\6\35\u00a4\n\35\r\35\16\35\u00a5\5\35\u00a8\n\35\3\36\3\36"+
		"\3\37\3\37\3 \3 \7 \u00b0\n \f \16 \u00b3\13 \3!\6!\u00b6\n!\r!\16!\u00b7"+
		"\3!\3!\2\2\"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\2=\2?\37A \3\2\b\4\2GGgg\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62"+
		";C\\aac|\5\2\13\f\17\17\"\"\u00c0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\29\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\3C\3\2\2\2\5E\3\2\2"+
		"\2\7G\3\2\2\2\tI\3\2\2\2\13L\3\2\2\2\rO\3\2\2\2\17Q\3\2\2\2\21T\3\2\2"+
		"\2\23V\3\2\2\2\25X\3\2\2\2\27Z\3\2\2\2\31\\\3\2\2\2\33^\3\2\2\2\35`\3"+
		"\2\2\2\37b\3\2\2\2!f\3\2\2\2#j\3\2\2\2%o\3\2\2\2\'s\3\2\2\2)y\3\2\2\2"+
		"+}\3\2\2\2-\177\3\2\2\2/\u0081\3\2\2\2\61\u0083\3\2\2\2\63\u0086\3\2\2"+
		"\2\65\u0089\3\2\2\2\67\u0094\3\2\2\29\u0096\3\2\2\2;\u00a9\3\2\2\2=\u00ab"+
		"\3\2\2\2?\u00ad\3\2\2\2A\u00b5\3\2\2\2CD\7+\2\2D\4\3\2\2\2EF\7.\2\2F\6"+
		"\3\2\2\2GH\7*\2\2H\b\3\2\2\2IJ\7(\2\2JK\7(\2\2K\n\3\2\2\2LM\7~\2\2MN\7"+
		"~\2\2N\f\3\2\2\2OP\7}\2\2P\16\3\2\2\2QR\7?\2\2RS\7?\2\2S\20\3\2\2\2TU"+
		"\7\177\2\2U\22\3\2\2\2VW\7#\2\2W\24\3\2\2\2XY\7-\2\2Y\26\3\2\2\2Z[\7/"+
		"\2\2[\30\3\2\2\2\\]\7,\2\2]\32\3\2\2\2^_\7\61\2\2_\34\3\2\2\2`a\7`\2\2"+
		"a\36\3\2\2\2bc\7g\2\2cd\7z\2\2de\7r\2\2e \3\2\2\2fg\7n\2\2gh\7q\2\2hi"+
		"\7i\2\2i\"\3\2\2\2jk\7u\2\2kl\7s\2\2lm\7t\2\2mn\7v\2\2n$\3\2\2\2op\7u"+
		"\2\2pq\7w\2\2qr\7o\2\2r&\3\2\2\2st\7v\2\2tu\7j\2\2uv\7g\2\2vw\7v\2\2w"+
		"x\7c\2\2x(\3\2\2\2yz\7c\2\2z{\7d\2\2{|\7u\2\2|*\3\2\2\2}~\7?\2\2~,\3\2"+
		"\2\2\177\u0080\7@\2\2\u0080.\3\2\2\2\u0081\u0082\7>\2\2\u0082\60\3\2\2"+
		"\2\u0083\u0084\7@\2\2\u0084\u0085\7?\2\2\u0085\62\3\2\2\2\u0086\u0087"+
		"\7>\2\2\u0087\u0088\7?\2\2\u0088\64\3\2\2\2\u0089\u008a\7#\2\2\u008a\u008b"+
		"\7?\2\2\u008b\66\3\2\2\2\u008c\u0095\7\62\2\2\u008d\u0091\5=\37\2\u008e"+
		"\u0090\5;\36\2\u008f\u008e\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f\3\2"+
		"\2\2\u0091\u0092\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0094"+
		"\u008c\3\2\2\2\u0094\u008d\3\2\2\2\u00958\3\2\2\2\u0096\u0097\5\67\34"+
		"\2\u0097\u009b\7\60\2\2\u0098\u009a\5;\36\2\u0099\u0098\3\2\2\2\u009a"+
		"\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u00a7\3\2"+
		"\2\2\u009d\u009b\3\2\2\2\u009e\u00a0\t\2\2\2\u009f\u00a1\7/\2\2\u00a0"+
		"\u009f\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a3\3\2\2\2\u00a2\u00a4\5;"+
		"\36\2\u00a3\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u009e\3\2\2\2\u00a7\u00a8\3\2"+
		"\2\2\u00a8:\3\2\2\2\u00a9\u00aa\t\3\2\2\u00aa<\3\2\2\2\u00ab\u00ac\t\4"+
		"\2\2\u00ac>\3\2\2\2\u00ad\u00b1\t\5\2\2\u00ae\u00b0\t\6\2\2\u00af\u00ae"+
		"\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2"+
		"@\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00b6\t\7\2\2\u00b5\u00b4\3\2\2\2"+
		"\u00b6\u00b7\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b9"+
		"\3\2\2\2\u00b9\u00ba\b!\2\2\u00baB\3\2\2\2\13\2\u0091\u0094\u009b\u00a0"+
		"\u00a5\u00a7\u00b1\u00b7\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}