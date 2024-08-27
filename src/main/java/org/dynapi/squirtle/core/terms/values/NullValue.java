package org.dynapi.squirtle.core.terms.values;

public class NullValue extends LiteralValue {
    public NullValue(NullValue original) {
        super(original);
    }

    public NullValue() {
        super("NULL");
    }

    public NullValue as(String alias) {
        this.alias = alias;
        return this;
    }
}
