package common.Mathematics;

/**
 * Created by gabriel on 12/23/13.
 */
public class OperatorToken extends Token {
    private boolean isUnary;

    public OperatorToken(String s) {
        this(s, false);
    }

    public OperatorToken(String s, boolean isUnary) {
        super(TOKEN_TYPE.OPERATOR, s, getOperatorPrecedence(s));
        this.isUnary = isUnary;
    }

    public static int getOperatorPrecedence(String s) {
        if (s.length() != 1) {
            //TODO error
        }

        // values from https://en.wikipedia.org/wiki/Shunting_yard_algorithm
        switch (s.charAt(0)) {
            case '+':
            case '-':
                return 2;
            case '*':
            case '/':
                return 3;
            case '^':
                return 4;
        }
        //TODO error
        return 0;
    }

    public boolean isUnary() {
        return isUnary;
    }
}
