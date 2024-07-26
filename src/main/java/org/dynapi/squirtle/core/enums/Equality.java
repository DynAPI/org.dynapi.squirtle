package org.dynapi.squirtle.core.enums;

public class Equality extends Comparator {
    public final static Equality EQ = new Equality("=");
    public final static Equality NE = new Equality("<>");
    public final static Equality GT = new Equality(">");
    public final static Equality GTE = new Equality(">=");
    public final static Equality LT = new Equality("<");
    public final static Equality LTE = new Equality("<=");

    public Equality(String value) {
        super(value);
    }
}
