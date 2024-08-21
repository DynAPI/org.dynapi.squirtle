package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class LiteralValue extends Term {
    private final String literalValue;

    public LiteralValue(LiteralValue original) {
        super(original);
        this.literalValue = original.literalValue;
    }

    public LiteralValue(String literalValue) {
        this.literalValue = literalValue;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return Utils.formatAliasSql(literalValue, alias, config);
    }
}
