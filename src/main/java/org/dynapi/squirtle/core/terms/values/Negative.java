package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class Negative extends Term {
    private final Term term;

    public Negative(Term term) {
        super(null);
        this.term = term;
    }

    // todo: property isAggregate <- term.isAggregate

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format("-%s", term.getSql(config));
    }
}
