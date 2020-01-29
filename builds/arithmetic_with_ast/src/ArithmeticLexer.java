// Generated from Arithmetic.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ArithmeticLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, WHITESPACE=4, EQ=5, OPERATOR=6, INT=7, ID=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "WHITESPACE", "EQ", "OPERATOR", "PLUS", "MINUS", 
			"MULT", "DIV", "MOD", "EXP", "INT", "ID"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'<BEGIN'", "'END>'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "WHITESPACE", "EQ", "OPERATOR", "INT", "ID"
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


	public ArithmeticLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Arithmetic.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\n{\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\5\6;\n\6\3\7\3\7\3\7\3\7\3\7\3\7\5\7C\n\7\3\b\3\b\3\b\3\b\3\b"+
		"\5\bJ\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5\tR\n\t\3\n\3\n\3\n\3\n\3\n\3\n\5\n"+
		"Z\n\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13b\n\13\3\f\3\f\3\f\3\f\5\fh\n"+
		"\f\3\r\3\r\3\r\3\r\5\rn\n\r\3\16\6\16q\n\16\r\16\16\16r\3\17\3\17\7\17"+
		"w\n\17\f\17\16\17z\13\17\2\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\2\21\2\23"+
		"\2\25\2\27\2\31\2\33\t\35\n\3\2\6\5\2\13\f\17\17\"\"\3\2\62;\4\2C\\c|"+
		"\5\2\62;C\\c|\2\u0083\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5&\3"+
		"\2\2\2\7+\3\2\2\2\t-\3\2\2\2\13:\3\2\2\2\rB\3\2\2\2\17I\3\2\2\2\21Q\3"+
		"\2\2\2\23Y\3\2\2\2\25a\3\2\2\2\27g\3\2\2\2\31m\3\2\2\2\33p\3\2\2\2\35"+
		"t\3\2\2\2\37 \7>\2\2 !\7D\2\2!\"\7G\2\2\"#\7I\2\2#$\7K\2\2$%\7P\2\2%\4"+
		"\3\2\2\2&\'\7G\2\2\'(\7P\2\2()\7F\2\2)*\7@\2\2*\6\3\2\2\2+,\7=\2\2,\b"+
		"\3\2\2\2-.\t\2\2\2./\3\2\2\2/\60\b\5\2\2\60\n\3\2\2\2\61\62\7g\2\2\62"+
		"\63\7s\2\2\63\64\7w\2\2\64\65\7c\2\2\65\66\7n\2\2\66;\7u\2\2\67;\7?\2"+
		"\289\7/\2\29;\7@\2\2:\61\3\2\2\2:\67\3\2\2\2:8\3\2\2\2;\f\3\2\2\2<C\5"+
		"\17\b\2=C\5\21\t\2>C\5\23\n\2?C\5\25\13\2@C\5\27\f\2AC\5\31\r\2B<\3\2"+
		"\2\2B=\3\2\2\2B>\3\2\2\2B?\3\2\2\2B@\3\2\2\2BA\3\2\2\2C\16\3\2\2\2DE\7"+
		"r\2\2EF\7n\2\2FG\7w\2\2GJ\7u\2\2HJ\7-\2\2ID\3\2\2\2IH\3\2\2\2J\20\3\2"+
		"\2\2KL\7o\2\2LM\7k\2\2MN\7p\2\2NO\7w\2\2OR\7u\2\2PR\7/\2\2QK\3\2\2\2Q"+
		"P\3\2\2\2R\22\3\2\2\2ST\7v\2\2TU\7k\2\2UV\7o\2\2VW\7g\2\2WZ\7u\2\2XZ\7"+
		",\2\2YS\3\2\2\2YX\3\2\2\2Z\24\3\2\2\2[\\\7f\2\2\\]\7k\2\2]^\7x\2\2^_\7"+
		"d\2\2_b\7{\2\2`b\7\61\2\2a[\3\2\2\2a`\3\2\2\2b\26\3\2\2\2cd\7o\2\2de\7"+
		"q\2\2eh\7f\2\2fh\7\'\2\2gc\3\2\2\2gf\3\2\2\2h\30\3\2\2\2ij\7r\2\2jk\7"+
		"q\2\2kn\7y\2\2ln\7`\2\2mi\3\2\2\2ml\3\2\2\2n\32\3\2\2\2oq\t\3\2\2po\3"+
		"\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2s\34\3\2\2\2tx\t\4\2\2uw\t\5\2\2v"+
		"u\3\2\2\2wz\3\2\2\2xv\3\2\2\2xy\3\2\2\2y\36\3\2\2\2zx\3\2\2\2\16\2:BI"+
		"QYagmrvx\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}