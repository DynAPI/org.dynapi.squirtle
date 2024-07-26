package org.dynapi.squirtle.core.enums;

public class BooleanComp extends Comparator {
    public final static BooleanComp AND = new BooleanComp("AND");
    public final static BooleanComp OR = new BooleanComp("OR");
    public final static BooleanComp XOR = new BooleanComp("XOR");
    public final static BooleanComp TRUE = new BooleanComp("TRUE");
    public final static BooleanComp FALSE = new BooleanComp("FALSE");

    public BooleanComp(String value) {
        super(value);
    }
}
