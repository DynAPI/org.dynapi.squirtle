package org.dynapi.squirtle.core.enums;

public class Arithmetic extends Enumerator {
    public final static Arithmetic ADD = new Arithmetic("+");
    public final static Arithmetic SUB = new Arithmetic("-");
    public final static Arithmetic MUL = new Arithmetic("*");
    public final static Arithmetic DIV = new Arithmetic("/");
    public final static Arithmetic LSHIFT = new Arithmetic("<<");
    public final static Arithmetic RSHIFT = new Arithmetic(">>");

    public Arithmetic(String value) {
        super(value);
    }
}
