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
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, ADD=7, SUB=8, MUL=9, DIV=10, 
		POW=11, EXP=12, LOG=13, SQRT=14, SUM=15, THETA=16, ABS=17, AND=18, OR=19, 
		EQ=20, GT=21, LT=22, GE=23, LE=24, NE=25, NNINT=26, NNFLOAT=27, VARNAME=28, 
		WHITESPACE=29;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"')'", "','", "'('", "'{'", "'}'", "'!'", "'+'", "'-'", "'*'", "'/'", 
		"'^'", "'exp'", "'log'", "'sqrt'", "'sum'", "'theta'", "'abs'", "'&&'", 
		"'||'", "'=='", "'>'", "'<'", "'>='", "'<='", "'!='", "NNINT", "NNFLOAT", 
		"VARNAME", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "ADD", "SUB", "MUL", "DIV", 
		"POW", "EXP", "LOG", "SQRT", "SUM", "THETA", "ABS", "AND", "OR", "EQ", 
		"GT", "LT", "GE", "LE", "NE", "NNINT", "NNFLOAT", "D", "NZD", "VARNAME", 
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\37\u00b7\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n"+
		"\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3"+
		"\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\7"+
		"\33\u008c\n\33\f\33\16\33\u008f\13\33\5\33\u0091\n\33\3\34\3\34\3\34\7"+
		"\34\u0096\n\34\f\34\16\34\u0099\13\34\3\34\3\34\5\34\u009d\n\34\3\34\6"+
		"\34\u00a0\n\34\r\34\16\34\u00a1\5\34\u00a4\n\34\3\35\3\35\3\36\3\36\3"+
		"\37\3\37\7\37\u00ac\n\37\f\37\16\37\u00af\13\37\3 \6 \u00b2\n \r \16 "+
		"\u00b3\3 \3 \2\2!\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\2;\2=\36?\37\3\2\b\4\2GGgg\3\2\62;\3\2\63;\5\2C\\aac|\6\2\62"+
		";C\\aac|\5\2\13\f\17\17\"\"\u00bc\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\3A\3\2\2\2\5C\3\2\2\2\7E\3\2\2"+
		"\2\tG\3\2\2\2\13I\3\2\2\2\rK\3\2\2\2\17M\3\2\2\2\21O\3\2\2\2\23Q\3\2\2"+
		"\2\25S\3\2\2\2\27U\3\2\2\2\31W\3\2\2\2\33[\3\2\2\2\35_\3\2\2\2\37d\3\2"+
		"\2\2!h\3\2\2\2#n\3\2\2\2%r\3\2\2\2\'u\3\2\2\2)x\3\2\2\2+{\3\2\2\2-}\3"+
		"\2\2\2/\177\3\2\2\2\61\u0082\3\2\2\2\63\u0085\3\2\2\2\65\u0090\3\2\2\2"+
		"\67\u0092\3\2\2\29\u00a5\3\2\2\2;\u00a7\3\2\2\2=\u00a9\3\2\2\2?\u00b1"+
		"\3\2\2\2AB\7+\2\2B\4\3\2\2\2CD\7.\2\2D\6\3\2\2\2EF\7*\2\2F\b\3\2\2\2G"+
		"H\7}\2\2H\n\3\2\2\2IJ\7\177\2\2J\f\3\2\2\2KL\7#\2\2L\16\3\2\2\2MN\7-\2"+
		"\2N\20\3\2\2\2OP\7/\2\2P\22\3\2\2\2QR\7,\2\2R\24\3\2\2\2ST\7\61\2\2T\26"+
		"\3\2\2\2UV\7`\2\2V\30\3\2\2\2WX\7g\2\2XY\7z\2\2YZ\7r\2\2Z\32\3\2\2\2["+
		"\\\7n\2\2\\]\7q\2\2]^\7i\2\2^\34\3\2\2\2_`\7u\2\2`a\7s\2\2ab\7t\2\2bc"+
		"\7v\2\2c\36\3\2\2\2de\7u\2\2ef\7w\2\2fg\7o\2\2g \3\2\2\2hi\7v\2\2ij\7"+
		"j\2\2jk\7g\2\2kl\7v\2\2lm\7c\2\2m\"\3\2\2\2no\7c\2\2op\7d\2\2pq\7u\2\2"+
		"q$\3\2\2\2rs\7(\2\2st\7(\2\2t&\3\2\2\2uv\7~\2\2vw\7~\2\2w(\3\2\2\2xy\7"+
		"?\2\2yz\7?\2\2z*\3\2\2\2{|\7@\2\2|,\3\2\2\2}~\7>\2\2~.\3\2\2\2\177\u0080"+
		"\7@\2\2\u0080\u0081\7?\2\2\u0081\60\3\2\2\2\u0082\u0083\7>\2\2\u0083\u0084"+
		"\7?\2\2\u0084\62\3\2\2\2\u0085\u0086\7#\2\2\u0086\u0087\7?\2\2\u0087\64"+
		"\3\2\2\2\u0088\u0091\7\62\2\2\u0089\u008d\5;\36\2\u008a\u008c\59\35\2"+
		"\u008b\u008a\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e"+
		"\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0088\3\2\2\2\u0090"+
		"\u0089\3\2\2\2\u0091\66\3\2\2\2\u0092\u0093\5\65\33\2\u0093\u0097\7\60"+
		"\2\2\u0094\u0096\59\35\2\u0095\u0094\3\2\2\2\u0096\u0099\3\2\2\2\u0097"+
		"\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u00a3\3\2\2\2\u0099\u0097\3\2"+
		"\2\2\u009a\u009c\t\2\2\2\u009b\u009d\7/\2\2\u009c\u009b\3\2\2\2\u009c"+
		"\u009d\3\2\2\2\u009d\u009f\3\2\2\2\u009e\u00a0\59\35\2\u009f\u009e\3\2"+
		"\2\2\u00a0\u00a1\3\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2"+
		"\u00a4\3\2\2\2\u00a3\u009a\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a48\3\2\2\2"+
		"\u00a5\u00a6\t\3\2\2\u00a6:\3\2\2\2\u00a7\u00a8\t\4\2\2\u00a8<\3\2\2\2"+
		"\u00a9\u00ad\t\5\2\2\u00aa\u00ac\t\6\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00af"+
		"\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae>\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00b0\u00b2\t\7\2\2\u00b1\u00b0\3\2\2\2\u00b2\u00b3\3\2"+
		"\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5"+
		"\u00b6\b \2\2\u00b6@\3\2\2\2\13\2\u008d\u0090\u0097\u009c\u00a1\u00a3"+
		"\u00ad\u00b3\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}