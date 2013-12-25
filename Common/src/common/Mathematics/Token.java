package common.Mathematics;

/**
 * Describes the methods that must be defined in order for an object to be
 * considered a token. Every token must be able to be processed (handle) and
 * printable (toString).
 *
 * @author Chris Fernandes and Adam Pere
 * @version 10/26/08
 */
public abstract class Token {

    public static enum TOKEN_TYPE {
        OPERATOR, DELIMITER, NUMBER, STRING
    }

    private TOKEN_TYPE type;
    private String value;
    private int precedence;

    public Token(TOKEN_TYPE type, String value, int precedence) {
        this.type = type;
        this.value = value;
        this.precedence = precedence;
    }

    /**
     * Returns the token as a printable String
     *
     * @return the String version of the token. For example, ")" for a right
     * parenthesis.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * returns the precedence of the token (It doesn't make sense to have
     * precedence in every token but it was the most coherent way I could think
     * of)
     *
     * @return an int representing the precedence
     */
    public int getPrecedence() {
        return precedence;
    }

    public TOKEN_TYPE getType() {
        return type;
    }
}
