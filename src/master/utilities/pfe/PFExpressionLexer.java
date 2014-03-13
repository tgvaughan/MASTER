// Generated from PFExpression.g4 by ANTLR 4.2
package master.utilities.pfe;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PFExpressionLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, POPTYPE=7, LOC=8, NUM=9, 
		WHITESPACE=10;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"')'", "'+'", "'-'", "'*'", "'('", "'/'", "POPTYPE", "LOC", "NUM", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "POPTYPE", "LOC", "NUM", 
		"NNINT", "D", "NZD", "WHITESPACE"
	};


	public PFExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PFExpression.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\fe\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6"+
		"\3\7\3\7\3\b\6\b+\n\b\r\b\16\b,\3\t\3\t\3\t\3\t\7\t\63\n\t\f\t\16\t\66"+
		"\13\t\3\t\3\t\3\n\5\n;\n\n\3\n\3\n\3\n\6\n@\n\n\r\n\16\nA\5\nD\n\n\3\n"+
		"\3\n\5\nH\n\n\3\n\6\nK\n\n\r\n\16\nL\5\nO\n\n\3\13\3\13\3\13\7\13T\n\13"+
		"\f\13\16\13W\13\13\5\13Y\n\13\3\f\3\f\3\r\3\r\3\16\6\16`\n\16\r\16\16"+
		"\16a\3\16\3\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\2\27"+
		"\2\31\2\33\f\3\2\7\4\2C\\c|\4\2GGgg\3\2\62;\3\2\63;\5\2\13\f\17\17\"\""+
		"l\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\33\3\2\2\2\3\35\3\2\2\2"+
		"\5\37\3\2\2\2\7!\3\2\2\2\t#\3\2\2\2\13%\3\2\2\2\r\'\3\2\2\2\17*\3\2\2"+
		"\2\21.\3\2\2\2\23:\3\2\2\2\25X\3\2\2\2\27Z\3\2\2\2\31\\\3\2\2\2\33_\3"+
		"\2\2\2\35\36\7+\2\2\36\4\3\2\2\2\37 \7-\2\2 \6\3\2\2\2!\"\7/\2\2\"\b\3"+
		"\2\2\2#$\7,\2\2$\n\3\2\2\2%&\7*\2\2&\f\3\2\2\2\'(\7\61\2\2(\16\3\2\2\2"+
		")+\t\2\2\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\20\3\2\2\2./\7]\2"+
		"\2/\64\5\25\13\2\60\61\7.\2\2\61\63\5\25\13\2\62\60\3\2\2\2\63\66\3\2"+
		"\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\67\3\2\2\2\66\64\3\2\2\2\678\7_\2"+
		"\28\22\3\2\2\29;\7/\2\2:9\3\2\2\2:;\3\2\2\2;<\3\2\2\2<C\5\25\13\2=?\7"+
		"\60\2\2>@\5\27\f\2?>\3\2\2\2@A\3\2\2\2A?\3\2\2\2AB\3\2\2\2BD\3\2\2\2C"+
		"=\3\2\2\2CD\3\2\2\2DN\3\2\2\2EG\t\3\2\2FH\7/\2\2GF\3\2\2\2GH\3\2\2\2H"+
		"J\3\2\2\2IK\5\27\f\2JI\3\2\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2\2MO\3\2\2\2"+
		"NE\3\2\2\2NO\3\2\2\2O\24\3\2\2\2PY\7\62\2\2QU\5\31\r\2RT\5\27\f\2SR\3"+
		"\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2VY\3\2\2\2WU\3\2\2\2XP\3\2\2\2XQ\3"+
		"\2\2\2Y\26\3\2\2\2Z[\t\4\2\2[\30\3\2\2\2\\]\t\5\2\2]\32\3\2\2\2^`\t\6"+
		"\2\2_^\3\2\2\2`a\3\2\2\2a_\3\2\2\2ab\3\2\2\2bc\3\2\2\2cd\b\16\2\2d\34"+
		"\3\2\2\2\16\2,\64:ACGLNUXa\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}