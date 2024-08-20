package org.dynapi.squirtle.core.terms.values;

public class SystemTimeValue extends LiteralValue {
    public SystemTimeValue(SystemTimeValue original) {
        super(original);
    }

    public SystemTimeValue(String alias) {
        super(alias, "SYSTEM_TIME");
    }
}
