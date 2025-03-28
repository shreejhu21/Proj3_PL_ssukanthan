public class ParserImpl extends Parser {

    @Override
    public Expr do_parse() throws Exception {
        Expr expr = parseT();
        if (tokens != null) {
            throw new Exception("Unexpected tokens after expression");
        }
        return expr;
    }

    private Expr parseT() throws Exception {
        Expr f = parseF();
        if (lookaheadAddOp()) {
            Token op = consumeAddOp();
            Expr t = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(f, t);
            } else {
                return new MinusExpr(f, t);
            }
        } else {
            return f;
        }
    }

    private Expr parseF() throws Exception {
        Expr lit = parseLit();
        if (lookaheadMulOp()) {
            Token op = consumeMulOp();
            Expr f = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(lit, f);
            } else {
                return new DivExpr(lit, f);
            }
        } else {
            return lit;
        }
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) { // Fixed: Added lookahead parameter 0
            Token numToken = consume(TokenType.NUM);
            float value = Float.parseFloat(numToken.lexeme);
            return new FloatExpr(value);
        } else if (peek(TokenType.LPAREN, 0)) { // Fixed: Added lookahead parameter 0
            consume(TokenType.LPAREN);
            Expr t = parseT();
            consume(TokenType.RPAREN);
            return t;
        } else {
            throw new Exception("Unexpected token in Lit: " + (tokens == null ? "end of input" : tokens.elem.ty));
        }
    }

    private boolean lookaheadAddOp() {
        return tokens != null && (tokens.elem.ty == TokenType.PLUS || tokens.elem.ty == TokenType.MINUS);
    }

    private boolean lookaheadMulOp() {
        return tokens != null && (tokens.elem.ty == TokenType.TIMES || tokens.elem.ty == TokenType.DIV);
    }

    private Token consumeAddOp() throws Exception {
        if (tokens.elem.ty == TokenType.PLUS) {
            return consume(TokenType.PLUS);
        } else if (tokens.elem.ty == TokenType.MINUS) {
            return consume(TokenType.MINUS);
        } else {
            throw new Exception("Expected AddOp but found " + tokens.elem.ty);
        }
    }

    private Token consumeMulOp() throws Exception {
        if (tokens.elem.ty == TokenType.TIMES) {
            return consume(TokenType.TIMES);
        } else if (tokens.elem.ty == TokenType.DIV) {
            return consume(TokenType.DIV);
        } else {
            throw new Exception("Expected MulOp but found " + tokens.elem.ty);
        }
    }
}