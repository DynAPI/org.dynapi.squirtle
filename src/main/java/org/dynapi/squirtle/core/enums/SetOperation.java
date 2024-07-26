package org.dynapi.squirtle.core.enums;

public class SetOperation extends Enumerator {
    public final static SetOperation UNION = new SetOperation("UNION");
    public final static SetOperation UNION_ALL = new SetOperation("UNION ALL");
    public final static SetOperation INTERSECT = new SetOperation("INTERSECT");
    public final static SetOperation EXCEPT_OF = new SetOperation("EXCEPT");
    public final static SetOperation MINUS = new SetOperation("MINUS");

    public SetOperation(String value) {
        super(value);
    }
}
