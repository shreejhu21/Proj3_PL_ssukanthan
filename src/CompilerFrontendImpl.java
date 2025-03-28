public class CompilerFrontendImpl extends CompilerFrontend {

    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    @Override
    protected void init_lexer() {
        lex = new LexerImpl();

        // Automaton for NUM: [0-9]*\.[0-9]+
        Automaton numAutomaton = createNumAutomaton();
        lex.add_automaton(TokenType.NUM, numAutomaton);

        // Automaton for PLUS: \+
        Automaton plusAutomaton = createSingleCharAutomaton('+', TokenType.PLUS);
        lex.add_automaton(TokenType.PLUS, plusAutomaton);

        // Automaton for MINUS: -
        Automaton minusAutomaton = createSingleCharAutomaton('-', TokenType.MINUS);
        lex.add_automaton(TokenType.MINUS, minusAutomaton);

        // Automaton for TIMES: \*
        Automaton timesAutomaton = createSingleCharAutomaton('*', TokenType.TIMES);
        lex.add_automaton(TokenType.TIMES, timesAutomaton);

        // Automaton for DIV: /
        Automaton divAutomaton = createSingleCharAutomaton('/', TokenType.DIV);
        lex.add_automaton(TokenType.DIV, divAutomaton);

        // Automaton for LPAREN: \(
        Automaton lparenAutomaton = createSingleCharAutomaton('(', TokenType.LPAREN);
        lex.add_automaton(TokenType.LPAREN, lparenAutomaton);

        // Automaton for RPAREN: \)
        Automaton rparenAutomaton = createSingleCharAutomaton(')', TokenType.RPAREN);
        lex.add_automaton(TokenType.RPAREN, rparenAutomaton);

        // Automaton for WHITE_SPACE: (' '|\n|\r|\t)*
        Automaton wsAutomaton = createWhiteSpaceAutomaton();
        lex.add_automaton(TokenType.WHITE_SPACE, wsAutomaton);
    }

    private Automaton createNumAutomaton() {
        Automaton numAutomaton = new AutomatonImpl();
        numAutomaton.addState(0, true, false);
        numAutomaton.addState(1, false, false);
        numAutomaton.addState(2, false, true);

        for (char c = '0'; c <= '9'; c++) {
            numAutomaton.addTransition(0, c, 0);
        }
        numAutomaton.addTransition(0, '.', 1);
        for (char c = '0'; c <= '9'; c++) {
            numAutomaton.addTransition(1, c, 2);
            numAutomaton.addTransition(2, c, 2);
        }

        return numAutomaton;
    }

    private Automaton createSingleCharAutomaton(char symbol, TokenType type) {
        Automaton automaton = new AutomatonImpl();
        automaton.addState(0, true, false);
        automaton.addState(1, false, true);
        automaton.addTransition(0, symbol, 1);
        return automaton;
    }

    private Automaton createWhiteSpaceAutomaton() {
        Automaton automaton = new AutomatonImpl();
        automaton.addState(0, true, true);
        char[] whitespaces = {' ', '\n', '\r', '\t'};
        for (char c : whitespaces) {
            automaton.addTransition(0, c, 0);
        }
        return automaton;
    }
}