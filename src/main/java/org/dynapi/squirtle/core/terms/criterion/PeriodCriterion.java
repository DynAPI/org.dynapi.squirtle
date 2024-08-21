package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class PeriodCriterion extends RangeCriterion {
    public PeriodCriterion(PeriodCriterion original) {
        super(original);
    }

    public PeriodCriterion(Term term, Term start, Term end) {
        super(term, start, end);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s FROM %s TO %s",
                term.getSql(config),
                start.getSql(config),
                end.getSql(config)
        );
        return Utils.formatAliasSql(sql, alias, config);
    }
}
