package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class Negative extends Term {
    private final Term term;

    public Negative(Negative original) {
        super(original);
        this.term = CloneUtils.copyConstructorClone(original.term);
    }

    public Negative(Term term) {
        super((String) null);
        this.term = term;
    }

    // todo: property isAggregate <- term.isAggregate

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format("-%s", term.getSql(config));
    }
}
