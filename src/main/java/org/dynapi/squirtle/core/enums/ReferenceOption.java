package org.dynapi.squirtle.core.enums;

public class ReferenceOption extends Enumerator {
    public final static ReferenceOption CASCADE = new ReferenceOption("CASCADE");
    public final static ReferenceOption NO_ACTION = new ReferenceOption("NO_ACTION");
    public final static ReferenceOption RESTRICT = new ReferenceOption("RESTRICT");
    public final static ReferenceOption SET_NULL = new ReferenceOption("SET_NULL");
    public final static ReferenceOption SET_DEFAULT = new ReferenceOption("SET_DEFAULT");

    public ReferenceOption(String value) {
        super(value);
    }
}
