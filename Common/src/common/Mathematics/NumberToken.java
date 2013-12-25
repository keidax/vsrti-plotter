package common.Mathematics;

/**
 * Created by gabriel on 12/20/13.
 */
public class NumberToken extends Token {

    public NumberToken() {
        this(0.0);
    }

    public NumberToken(Double d) {
        super(TOKEN_TYPE.NUMBER, d.toString(), 0);
    }

    public NumberToken(String s) {
        this(Double.parseDouble(s));

    }
}
