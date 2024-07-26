package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class LiteralValue extends Term {
    private final String literalValue;

    public LiteralValue(String alias, String literalValue) {
        super(alias);
        this.literalValue = literalValue;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return Utils.formatAliasSql(literalValue, alias, config);
    }
}
