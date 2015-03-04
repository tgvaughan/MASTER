// Generated from Equation.g4 by ANTLR 4.2
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
public class EquationLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__4=1, T__3=2, T__2=3, T__1=4, T__0=5, EQ=6, GT=7, LT=8, GE=9, LE=10, 
		NE=11, ADD=12, SUB=13, MUL=14, DIV=15, POW=16, EXP=17, LOG=18, SQRT=19, 
		SUM=20, THETA=21, ABS=22, NNINT=23, NNFLOAT=24, VARNAME=25, WHITESPACE=26;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"']'", "')'", "','", "'['", "'('", "'='", "'>'", "'<'", "'>='", "'<='", 
		"'!='", "'+'", "'-'", "'*'", "'/'", "'^'", "'exp'", "'log'", "'sqrt'", 
		"'sum'", "'theta'", "'abs'", "NNINT", "NNFLOAT", "VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__4", "T__3", "T__2", "T__1", "T__0", "EQ", "GT", "LT", "GE", "LE", 
		"NE", "ADD", "SUB", "MUL", "DIV", "POW", "EXP", "LOG", "SQRT", "SUM", 
		"THETA", "ABS", "NNINT", "NNFLOAT", "D", "NZD", "VARNAME", "WHITESPACE"
	};


	public EquationLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Equation.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\34\u00a8\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3"+
		"\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3"+
		"\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\7"+
		"\30}\n\30\f\30\16\30\u0080\13\30\5\30\u0082\n\30\3\31\3\31\3\31\7\31\u0087"+
		"\n\31\f\31\16\31\u008a\13\31\3\31\3\31\5\31\u008e\n\31\3\31\6\31\u0091"+
		"\n\31\r\31\16\31\u0092\5\31\u0095\n\31\3\32\3\32\3\33\3\33\3\34\3\34\7"+
		"\34\u009d\n\34\f\34\16\34\u00a0\13\34\3\35\6\35\u00a3\n\35\r\35\16\35"+
		"\u00a4\3\35\3\35\2\2\36\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63"+
		"\2\65\2\67\339\34\3\2\b\4\2GGgg\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62;C\\"+
		"aac|\5\2\13\f\17\17\"\"\u00ad\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\3"+
		";\3\2\2\2\5=\3\2\2\2\7?\3\2\2\2\tA\3\2\2\2\13C\3\2\2\2\rE\3\2\2\2\17G"+
		"\3\2\2\2\21I\3\2\2\2\23K\3\2\2\2\25N\3\2\2\2\27Q\3\2\2\2\31T\3\2\2\2\33"+
		"V\3\2\2\2\35X\3\2\2\2\37Z\3\2\2\2!\\\3\2\2\2#^\3\2\2\2%b\3\2\2\2\'f\3"+
		"\2\2\2)k\3\2\2\2+o\3\2\2\2-u\3\2\2\2/\u0081\3\2\2\2\61\u0083\3\2\2\2\63"+
		"\u0096\3\2\2\2\65\u0098\3\2\2\2\67\u009a\3\2\2\29\u00a2\3\2\2\2;<\7_\2"+
		"\2<\4\3\2\2\2=>\7+\2\2>\6\3\2\2\2?@\7.\2\2@\b\3\2\2\2AB\7]\2\2B\n\3\2"+
		"\2\2CD\7*\2\2D\f\3\2\2\2EF\7?\2\2F\16\3\2\2\2GH\7@\2\2H\20\3\2\2\2IJ\7"+
		">\2\2J\22\3\2\2\2KL\7@\2\2LM\7?\2\2M\24\3\2\2\2NO\7>\2\2OP\7?\2\2P\26"+
		"\3\2\2\2QR\7#\2\2RS\7?\2\2S\30\3\2\2\2TU\7-\2\2U\32\3\2\2\2VW\7/\2\2W"+
		"\34\3\2\2\2XY\7,\2\2Y\36\3\2\2\2Z[\7\61\2\2[ \3\2\2\2\\]\7`\2\2]\"\3\2"+
		"\2\2^_\7g\2\2_`\7z\2\2`a\7r\2\2a$\3\2\2\2bc\7n\2\2cd\7q\2\2de\7i\2\2e"+
		"&\3\2\2\2fg\7u\2\2gh\7s\2\2hi\7t\2\2ij\7v\2\2j(\3\2\2\2kl\7u\2\2lm\7w"+
		"\2\2mn\7o\2\2n*\3\2\2\2op\7v\2\2pq\7j\2\2qr\7g\2\2rs\7v\2\2st\7c\2\2t"+
		",\3\2\2\2uv\7c\2\2vw\7d\2\2wx\7u\2\2x.\3\2\2\2y\u0082\7\62\2\2z~\5\65"+
		"\33\2{}\5\63\32\2|{\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177"+
		"\u0082\3\2\2\2\u0080~\3\2\2\2\u0081y\3\2\2\2\u0081z\3\2\2\2\u0082\60\3"+
		"\2\2\2\u0083\u0084\5/\30\2\u0084\u0088\7\60\2\2\u0085\u0087\5\63\32\2"+
		"\u0086\u0085\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089"+
		"\3\2\2\2\u0089\u0094\3\2\2\2\u008a\u0088\3\2\2\2\u008b\u008d\t\2\2\2\u008c"+
		"\u008e\7/\2\2\u008d\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0090\3\2"+
		"\2\2\u008f\u0091\5\63\32\2\u0090\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0095\3\2\2\2\u0094\u008b\3\2"+
		"\2\2\u0094\u0095\3\2\2\2\u0095\62\3\2\2\2\u0096\u0097\t\3\2\2\u0097\64"+
		"\3\2\2\2\u0098\u0099\t\4\2\2\u0099\66\3\2\2\2\u009a\u009e\t\5\2\2\u009b"+
		"\u009d\t\6\2\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3\2"+
		"\2\2\u009e\u009f\3\2\2\2\u009f8\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\u00a3"+
		"\t\7\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4"+
		"\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\b\35\2\2\u00a7:\3\2\2\2"+
		"\13\2~\u0081\u0088\u008d\u0092\u0094\u009e\u00a4\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}