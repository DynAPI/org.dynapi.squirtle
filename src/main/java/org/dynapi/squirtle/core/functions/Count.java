package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.criterion.Star;

public class Count extends DistinctOptionFunction {
    public Count(Count original) {
        super(original);
    }

    public Count(Object param, String alias) {
        super(alias, "COUNT", isStar(param) ? new Star() : param);
    }

    private static boolean isStar(Object param) {
        return param instanceof String && param.equals("*");
    }
}
