package common.Mathematics;

/**
 * A Token for Open Parenthesis
 *
 * @author Adam Pere
 * @version Project6
 */
public class Parenthesis extends Token {

    /**
     * Default Constructor - sets the precedence.
     */
    public Parenthesis(String s) {
        super(TOKEN_TYPE.DELIMITER, s, 0);
    }
}
