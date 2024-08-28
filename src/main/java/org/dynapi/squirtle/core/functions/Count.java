package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.criterion.Star;

public class Count extends DistinctOptionFunction {
    public Count(Count original) {
        super(original);
    }

    public Count(Object param) {
        super("COUNT", isStar(param) ? new Star() : param);
    }

    public Count as(String alias) {
        this.alias = alias;
        return this;
    }

    private static boolean isStar(Object param) {
        return param instanceof String && param.equals("*");
    }
}
