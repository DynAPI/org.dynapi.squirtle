package org.dynapi.squirtle.core.enums;

public class ArithmeticOperation extends Enumerator {
    public final static ArithmeticOperation ADD = new ArithmeticOperation("+");
    public final static ArithmeticOperation SUB = new ArithmeticOperation("-");
    public final static ArithmeticOperation MUL = new ArithmeticOperation("*");
    public final static ArithmeticOperation DIV = new ArithmeticOperation("/");
    public final static ArithmeticOperation LSHIFT = new ArithmeticOperation("<<");
    public final static ArithmeticOperation RSHIFT = new ArithmeticOperation(">>");

    public ArithmeticOperation(String value) {
        super(value);
    }
}
