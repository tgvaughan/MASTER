// Generated from /Users/vaughant/code/beast_and_friends/MASTER/src/master/utilities/pfe/PFExpression.g4 by ANTLR 4.10.1
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
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

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

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "POPTYPE", "LOC", "NUM", 
			"NNINT", "D", "NZD", "WHITESPACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'/'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, "POPTYPE", "LOC", "NUM", "WHITESPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
		"\u0004\u0000\n`\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0004\u0006)\b\u0006\u000b"+
		"\u0006\f\u0006*\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005"+
		"\u00071\b\u0007\n\u0007\f\u00074\t\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0004\b;\b\b\u000b\b\f\b<\u0003\b?\b\b\u0001\b\u0001"+
		"\b\u0003\bC\b\b\u0001\b\u0004\bF\b\b\u000b\b\f\bG\u0003\bJ\b\b\u0001\t"+
		"\u0001\t\u0001\t\u0005\tO\b\t\n\t\f\tR\t\t\u0003\tT\b\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0004\f[\b\f\u000b\f\f\f\\\u0001\f"+
		"\u0001\f\u0000\u0000\r\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004"+
		"\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\u0000\u0015\u0000\u0017"+
		"\u0000\u0019\n\u0001\u0000\u0005\u0002\u0000AZaz\u0002\u0000EEee\u0001"+
		"\u000009\u0001\u000019\u0003\u0000\t\n\r\r  f\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0019\u0001\u0000\u0000\u0000\u0001\u001b\u0001\u0000\u0000\u0000"+
		"\u0003\u001d\u0001\u0000\u0000\u0000\u0005\u001f\u0001\u0000\u0000\u0000"+
		"\u0007!\u0001\u0000\u0000\u0000\t#\u0001\u0000\u0000\u0000\u000b%\u0001"+
		"\u0000\u0000\u0000\r(\u0001\u0000\u0000\u0000\u000f,\u0001\u0000\u0000"+
		"\u0000\u00117\u0001\u0000\u0000\u0000\u0013S\u0001\u0000\u0000\u0000\u0015"+
		"U\u0001\u0000\u0000\u0000\u0017W\u0001\u0000\u0000\u0000\u0019Z\u0001"+
		"\u0000\u0000\u0000\u001b\u001c\u0005+\u0000\u0000\u001c\u0002\u0001\u0000"+
		"\u0000\u0000\u001d\u001e\u0005-\u0000\u0000\u001e\u0004\u0001\u0000\u0000"+
		"\u0000\u001f \u0005*\u0000\u0000 \u0006\u0001\u0000\u0000\u0000!\"\u0005"+
		"/\u0000\u0000\"\b\u0001\u0000\u0000\u0000#$\u0005(\u0000\u0000$\n\u0001"+
		"\u0000\u0000\u0000%&\u0005)\u0000\u0000&\f\u0001\u0000\u0000\u0000\')"+
		"\u0007\u0000\u0000\u0000(\'\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000"+
		"\u0000*(\u0001\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+\u000e\u0001"+
		"\u0000\u0000\u0000,-\u0005[\u0000\u0000-2\u0003\u0013\t\u0000./\u0005"+
		",\u0000\u0000/1\u0003\u0013\t\u00000.\u0001\u0000\u0000\u000014\u0001"+
		"\u0000\u0000\u000020\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u0000"+
		"35\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u000056\u0005]\u0000\u0000"+
		"6\u0010\u0001\u0000\u0000\u00007>\u0003\u0013\t\u00008:\u0005.\u0000\u0000"+
		"9;\u0003\u0015\n\u0000:9\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000"+
		"<:\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=?\u0001\u0000\u0000"+
		"\u0000>8\u0001\u0000\u0000\u0000>?\u0001\u0000\u0000\u0000?I\u0001\u0000"+
		"\u0000\u0000@B\u0007\u0001\u0000\u0000AC\u0005-\u0000\u0000BA\u0001\u0000"+
		"\u0000\u0000BC\u0001\u0000\u0000\u0000CE\u0001\u0000\u0000\u0000DF\u0003"+
		"\u0015\n\u0000ED\u0001\u0000\u0000\u0000FG\u0001\u0000\u0000\u0000GE\u0001"+
		"\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HJ\u0001\u0000\u0000\u0000"+
		"I@\u0001\u0000\u0000\u0000IJ\u0001\u0000\u0000\u0000J\u0012\u0001\u0000"+
		"\u0000\u0000KT\u00050\u0000\u0000LP\u0003\u0017\u000b\u0000MO\u0003\u0015"+
		"\n\u0000NM\u0001\u0000\u0000\u0000OR\u0001\u0000\u0000\u0000PN\u0001\u0000"+
		"\u0000\u0000PQ\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000RP\u0001"+
		"\u0000\u0000\u0000SK\u0001\u0000\u0000\u0000SL\u0001\u0000\u0000\u0000"+
		"T\u0014\u0001\u0000\u0000\u0000UV\u0007\u0002\u0000\u0000V\u0016\u0001"+
		"\u0000\u0000\u0000WX\u0007\u0003\u0000\u0000X\u0018\u0001\u0000\u0000"+
		"\u0000Y[\u0007\u0004\u0000\u0000ZY\u0001\u0000\u0000\u0000[\\\u0001\u0000"+
		"\u0000\u0000\\Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]^\u0001"+
		"\u0000\u0000\u0000^_\u0006\f\u0000\u0000_\u001a\u0001\u0000\u0000\u0000"+
		"\u000b\u0000*2<>BGIPS\\\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}