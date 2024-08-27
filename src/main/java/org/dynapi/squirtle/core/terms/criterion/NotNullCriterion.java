package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class NotNullCriterion extends NullCriterion {
    public NotNullCriterion(NotNullCriterion original) {
        super(original);
    }

    public NotNullCriterion(Term term) {
        super(term);
    }

    public NotNullCriterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s IS NOT NULL",
                term.getSql(config)
        );
        return Utils.formatAliasSql(sql, alias, config);
    }
}
