// Generated from /Users/vaughant/code/beast_and_friends/MASTER/src/master/utilities/pfe/PFExpression.g4 by ANTLR 4.7
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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, POPTYPE=7, LOC=8, NUM=9, 
		WHITESPACE=10;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "POPTYPE", "LOC", "NUM", 
		"NNINT", "D", "NZD", "WHITESPACE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'+'", "'-'", "'*'", "'/'", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, "POPTYPE", "LOC", "NUM", "WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PFExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PFExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\fb\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6"+
		"\3\7\3\7\3\b\6\b+\n\b\r\b\16\b,\3\t\3\t\3\t\3\t\7\t\63\n\t\f\t\16\t\66"+
		"\13\t\3\t\3\t\3\n\3\n\3\n\6\n=\n\n\r\n\16\n>\5\nA\n\n\3\n\3\n\5\nE\n\n"+
		"\3\n\6\nH\n\n\r\n\16\nI\5\nL\n\n\3\13\3\13\3\13\7\13Q\n\13\f\13\16\13"+
		"T\13\13\5\13V\n\13\3\f\3\f\3\r\3\r\3\16\6\16]\n\16\r\16\16\16^\3\16\3"+
		"\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\2\27\2\31\2\33"+
		"\f\3\2\7\4\2C\\c|\4\2GGgg\3\2\62;\3\2\63;\5\2\13\f\17\17\"\"\2h\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\33\3\2\2\2\3\35\3\2\2\2\5\37\3"+
		"\2\2\2\7!\3\2\2\2\t#\3\2\2\2\13%\3\2\2\2\r\'\3\2\2\2\17*\3\2\2\2\21.\3"+
		"\2\2\2\239\3\2\2\2\25U\3\2\2\2\27W\3\2\2\2\31Y\3\2\2\2\33\\\3\2\2\2\35"+
		"\36\7-\2\2\36\4\3\2\2\2\37 \7/\2\2 \6\3\2\2\2!\"\7,\2\2\"\b\3\2\2\2#$"+
		"\7\61\2\2$\n\3\2\2\2%&\7*\2\2&\f\3\2\2\2\'(\7+\2\2(\16\3\2\2\2)+\t\2\2"+
		"\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\20\3\2\2\2./\7]\2\2/\64\5"+
		"\25\13\2\60\61\7.\2\2\61\63\5\25\13\2\62\60\3\2\2\2\63\66\3\2\2\2\64\62"+
		"\3\2\2\2\64\65\3\2\2\2\65\67\3\2\2\2\66\64\3\2\2\2\678\7_\2\28\22\3\2"+
		"\2\29@\5\25\13\2:<\7\60\2\2;=\5\27\f\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>"+
		"?\3\2\2\2?A\3\2\2\2@:\3\2\2\2@A\3\2\2\2AK\3\2\2\2BD\t\3\2\2CE\7/\2\2D"+
		"C\3\2\2\2DE\3\2\2\2EG\3\2\2\2FH\5\27\f\2GF\3\2\2\2HI\3\2\2\2IG\3\2\2\2"+
		"IJ\3\2\2\2JL\3\2\2\2KB\3\2\2\2KL\3\2\2\2L\24\3\2\2\2MV\7\62\2\2NR\5\31"+
		"\r\2OQ\5\27\f\2PO\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2SV\3\2\2\2TR\3"+
		"\2\2\2UM\3\2\2\2UN\3\2\2\2V\26\3\2\2\2WX\t\4\2\2X\30\3\2\2\2YZ\t\5\2\2"+
		"Z\32\3\2\2\2[]\t\6\2\2\\[\3\2\2\2]^\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_`\3\2"+
		"\2\2`a\b\16\2\2a\34\3\2\2\2\r\2,\64>@DIKRU^\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}