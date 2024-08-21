package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class Index extends Term {
    private final String name;

    public Index(Index original) {
        super(original);
        this.name = original.name;
    }

    public Index(String name) {
        this.name = name;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return Utils.formatQuotes(name, config.getQuoteChar());
    }
}
