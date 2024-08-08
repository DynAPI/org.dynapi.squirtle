package org.dynapi.squirtle.core.enums;

public class BooleanComparison extends Comparator {
    public final static BooleanComparison AND = new BooleanComparison("AND");
    public final static BooleanComparison OR = new BooleanComparison("OR");
    public final static BooleanComparison XOR = new BooleanComparison("XOR");
    public final static BooleanComparison TRUE = new BooleanComparison("TRUE");
    public final static BooleanComparison FALSE = new BooleanComparison("FALSE");

    public BooleanComparison(String value) {
        super(value);
    }
}
