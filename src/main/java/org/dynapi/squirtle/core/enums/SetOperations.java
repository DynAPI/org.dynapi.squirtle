package org.dynapi.squirtle.core.enums;

public class SetOperations extends Enumerator {
    public final static SetOperations UNION = new SetOperations("UNION");
    public final static SetOperations UNION_ALL = new SetOperations("UNION ALL");
    public final static SetOperations INTERSECT = new SetOperations("INTERSECT");
    public final static SetOperations EXCEPT_OF = new SetOperations("EXCEPT");
    public final static SetOperations MINUS = new SetOperations("MINUS");

    public SetOperations(String value) {
        super(value);
    }
}
