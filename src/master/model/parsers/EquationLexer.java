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
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, EQ=7, GT=8, LT=9, GE=10, 
		LE=11, NE=12, ADD=13, SUB=14, MUL=15, DIV=16, POW=17, EXP=18, LOG=19, 
		SQRT=20, SUM=21, THETA=22, ABS=23, NNINT=24, NNFLOAT=25, VARNAME=26, WHITESPACE=27;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"']'", "')'", "','", "'['", "'('", "'='", "EQ", "'>'", "'<'", "'>='", 
		"'<='", "'!='", "'+'", "'-'", "'*'", "'/'", "'^'", "'exp'", "'log'", "'sqrt'", 
		"'sum'", "'theta'", "'abs'", "NNINT", "NNFLOAT", "VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "EQ", "GT", "LT", "GE", 
		"LE", "NE", "ADD", "SUB", "MUL", "DIV", "POW", "EXP", "LOG", "SQRT", "SUM", 
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\35\u00af\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\3\3"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\5\bM\n\b\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\31\3\31\3\31\7\31\u0084\n\31\f\31\16\31\u0087\13\31"+
		"\5\31\u0089\n\31\3\32\3\32\3\32\7\32\u008e\n\32\f\32\16\32\u0091\13\32"+
		"\3\32\3\32\5\32\u0095\n\32\3\32\6\32\u0098\n\32\r\32\16\32\u0099\5\32"+
		"\u009c\n\32\3\33\3\33\3\34\3\34\3\35\3\35\7\35\u00a4\n\35\f\35\16\35\u00a7"+
		"\13\35\3\36\6\36\u00aa\n\36\r\36\16\36\u00ab\3\36\3\36\2\2\37\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\2\67\29\34;\35\3\2\b\4\2"+
		"GGgg\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\u00b5"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5?\3\2\2"+
		"\2\7A\3\2\2\2\tC\3\2\2\2\13E\3\2\2\2\rG\3\2\2\2\17L\3\2\2\2\21N\3\2\2"+
		"\2\23P\3\2\2\2\25R\3\2\2\2\27U\3\2\2\2\31X\3\2\2\2\33[\3\2\2\2\35]\3\2"+
		"\2\2\37_\3\2\2\2!a\3\2\2\2#c\3\2\2\2%e\3\2\2\2\'i\3\2\2\2)m\3\2\2\2+r"+
		"\3\2\2\2-v\3\2\2\2/|\3\2\2\2\61\u0088\3\2\2\2\63\u008a\3\2\2\2\65\u009d"+
		"\3\2\2\2\67\u009f\3\2\2\29\u00a1\3\2\2\2;\u00a9\3\2\2\2=>\7_\2\2>\4\3"+
		"\2\2\2?@\7+\2\2@\6\3\2\2\2AB\7.\2\2B\b\3\2\2\2CD\7]\2\2D\n\3\2\2\2EF\7"+
		"*\2\2F\f\3\2\2\2GH\7?\2\2H\16\3\2\2\2IM\7?\2\2JK\7?\2\2KM\7?\2\2LI\3\2"+
		"\2\2LJ\3\2\2\2M\20\3\2\2\2NO\7@\2\2O\22\3\2\2\2PQ\7>\2\2Q\24\3\2\2\2R"+
		"S\7@\2\2ST\7?\2\2T\26\3\2\2\2UV\7>\2\2VW\7?\2\2W\30\3\2\2\2XY\7#\2\2Y"+
		"Z\7?\2\2Z\32\3\2\2\2[\\\7-\2\2\\\34\3\2\2\2]^\7/\2\2^\36\3\2\2\2_`\7,"+
		"\2\2` \3\2\2\2ab\7\61\2\2b\"\3\2\2\2cd\7`\2\2d$\3\2\2\2ef\7g\2\2fg\7z"+
		"\2\2gh\7r\2\2h&\3\2\2\2ij\7n\2\2jk\7q\2\2kl\7i\2\2l(\3\2\2\2mn\7u\2\2"+
		"no\7s\2\2op\7t\2\2pq\7v\2\2q*\3\2\2\2rs\7u\2\2st\7w\2\2tu\7o\2\2u,\3\2"+
		"\2\2vw\7v\2\2wx\7j\2\2xy\7g\2\2yz\7v\2\2z{\7c\2\2{.\3\2\2\2|}\7c\2\2}"+
		"~\7d\2\2~\177\7u\2\2\177\60\3\2\2\2\u0080\u0089\7\62\2\2\u0081\u0085\5"+
		"\67\34\2\u0082\u0084\5\65\33\2\u0083\u0082\3\2\2\2\u0084\u0087\3\2\2\2"+
		"\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085"+
		"\3\2\2\2\u0088\u0080\3\2\2\2\u0088\u0081\3\2\2\2\u0089\62\3\2\2\2\u008a"+
		"\u008b\5\61\31\2\u008b\u008f\7\60\2\2\u008c\u008e\5\65\33\2\u008d\u008c"+
		"\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090"+
		"\u009b\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0094\t\2\2\2\u0093\u0095\7/"+
		"\2\2\u0094\u0093\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0097\3\2\2\2\u0096"+
		"\u0098\5\65\33\2\u0097\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u0097\3"+
		"\2\2\2\u0099\u009a\3\2\2\2\u009a\u009c\3\2\2\2\u009b\u0092\3\2\2\2\u009b"+
		"\u009c\3\2\2\2\u009c\64\3\2\2\2\u009d\u009e\t\3\2\2\u009e\66\3\2\2\2\u009f"+
		"\u00a0\t\4\2\2\u00a08\3\2\2\2\u00a1\u00a5\t\5\2\2\u00a2\u00a4\t\6\2\2"+
		"\u00a3\u00a2\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6"+
		"\3\2\2\2\u00a6:\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00aa\t\7\2\2\u00a9"+
		"\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2"+
		"\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\b\36\2\2\u00ae<\3\2\2\2\f\2L\u0085"+
		"\u0088\u008f\u0094\u0099\u009b\u00a5\u00ab\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}