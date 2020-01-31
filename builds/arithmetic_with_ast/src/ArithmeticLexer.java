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
		T__0=1, T__1=2, T__2=3, WHITESPACE=4, COMMENT=5, EQ=6, OPERATOR=7, INT=8, 
		ID=9;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "WHITESPACE", "COMMENT", "EQ", "OPERATOR", "PLUS", 
			"MINUS", "MULT", "DIV", "MOD", "EXP", "INT", "ID"
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
			null, null, null, null, "WHITESPACE", "COMMENT", "EQ", "OPERATOR", "INT", 
			"ID"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\13\u008a\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\7\68\n\6\f\6\16\6;\13\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\5\7J\n\7\3\b\3\b\3\b\3\b\3\b\3\b\5\bR\n\b\3\t\3\t\3\t\3\t\3\t"+
		"\5\tY\n\t\3\n\3\n\3\n\3\n\3\n\3\n\5\na\n\n\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\5\13i\n\13\3\f\3\f\3\f\3\f\3\f\3\f\5\fq\n\f\3\r\3\r\3\r\3\r\5\rw\n"+
		"\r\3\16\3\16\3\16\3\16\5\16}\n\16\3\17\6\17\u0080\n\17\r\17\16\17\u0081"+
		"\3\20\3\20\7\20\u0086\n\20\f\20\16\20\u0089\13\20\39\2\21\3\3\5\4\7\5"+
		"\t\6\13\7\r\b\17\t\21\2\23\2\25\2\27\2\31\2\33\2\35\n\37\13\3\2\6\5\2"+
		"\13\f\17\17\"\"\3\2\62;\4\2C\\c|\5\2\62;C\\c|\2\u0093\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\3!\3\2\2\2\5(\3\2\2\2\7-\3\2\2\2\t/\3\2\2\2"+
		"\13\63\3\2\2\2\rI\3\2\2\2\17Q\3\2\2\2\21X\3\2\2\2\23`\3\2\2\2\25h\3\2"+
		"\2\2\27p\3\2\2\2\31v\3\2\2\2\33|\3\2\2\2\35\177\3\2\2\2\37\u0083\3\2\2"+
		"\2!\"\7>\2\2\"#\7D\2\2#$\7G\2\2$%\7I\2\2%&\7K\2\2&\'\7P\2\2\'\4\3\2\2"+
		"\2()\7G\2\2)*\7P\2\2*+\7F\2\2+,\7@\2\2,\6\3\2\2\2-.\7=\2\2.\b\3\2\2\2"+
		"/\60\t\2\2\2\60\61\3\2\2\2\61\62\b\5\2\2\62\n\3\2\2\2\63\64\7\61\2\2\64"+
		"\65\7\61\2\2\659\3\2\2\2\668\13\2\2\2\67\66\3\2\2\28;\3\2\2\29:\3\2\2"+
		"\29\67\3\2\2\2:<\3\2\2\2;9\3\2\2\2<=\7\f\2\2=>\3\2\2\2>?\b\6\2\2?\f\3"+
		"\2\2\2@A\7g\2\2AB\7s\2\2BC\7w\2\2CD\7c\2\2DE\7n\2\2EJ\7u\2\2FJ\7?\2\2"+
		"GH\7/\2\2HJ\7@\2\2I@\3\2\2\2IF\3\2\2\2IG\3\2\2\2J\16\3\2\2\2KR\5\21\t"+
		"\2LR\5\23\n\2MR\5\25\13\2NR\5\27\f\2OR\5\31\r\2PR\5\33\16\2QK\3\2\2\2"+
		"QL\3\2\2\2QM\3\2\2\2QN\3\2\2\2QO\3\2\2\2QP\3\2\2\2R\20\3\2\2\2ST\7r\2"+
		"\2TU\7n\2\2UV\7w\2\2VY\7u\2\2WY\7-\2\2XS\3\2\2\2XW\3\2\2\2Y\22\3\2\2\2"+
		"Z[\7o\2\2[\\\7k\2\2\\]\7p\2\2]^\7w\2\2^a\7u\2\2_a\7/\2\2`Z\3\2\2\2`_\3"+
		"\2\2\2a\24\3\2\2\2bc\7v\2\2cd\7k\2\2de\7o\2\2ef\7g\2\2fi\7u\2\2gi\7,\2"+
		"\2hb\3\2\2\2hg\3\2\2\2i\26\3\2\2\2jk\7f\2\2kl\7k\2\2lm\7x\2\2mn\7d\2\2"+
		"nq\7{\2\2oq\7\61\2\2pj\3\2\2\2po\3\2\2\2q\30\3\2\2\2rs\7o\2\2st\7q\2\2"+
		"tw\7f\2\2uw\7\'\2\2vr\3\2\2\2vu\3\2\2\2w\32\3\2\2\2xy\7r\2\2yz\7q\2\2"+
		"z}\7y\2\2{}\7`\2\2|x\3\2\2\2|{\3\2\2\2}\34\3\2\2\2~\u0080\t\3\2\2\177"+
		"~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\36\3\2\2\2\u0083\u0087\t\4\2\2\u0084\u0086\t\5\2\2\u0085\u0084\3\2\2"+
		"\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088 "+
		"\3\2\2\2\u0089\u0087\3\2\2\2\17\29IQX`hpv|\u0081\u0085\u0087\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}