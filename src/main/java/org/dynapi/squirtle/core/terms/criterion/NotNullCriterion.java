package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class NotNullCriterion extends NullCriterion {
    public NotNullCriterion(String alias, Term term) {
        super(alias, term);
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
