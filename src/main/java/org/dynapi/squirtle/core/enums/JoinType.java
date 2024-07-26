package org.dynapi.squirtle.core.enums;

public class JoinType extends Enumerator {
    public final static JoinType INNER = new JoinType("");
    public final static JoinType LEFT = new JoinType("LEFT");
    public final static JoinType RIGHT = new JoinType("RIGHT");
    public final static JoinType OUTER = new JoinType("FULL OUTER");
    public final static JoinType LEFT_OUTER = new JoinType("LEFT OUTER");
    public final static JoinType RIGHT_OUTER = new JoinType("RIGHT OUTER");
    public final static JoinType FULL_OUTER = new JoinType("FULL OUTER");
    public final static JoinType CROSS = new JoinType("CROSS");
    public final static JoinType HASH = new JoinType("HASH");

    public JoinType(String value) {
        super(value);
    }
}
