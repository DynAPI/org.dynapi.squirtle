package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

public class BetweenCriterion extends RangeCriterion {
    public BetweenCriterion(String alias, Term term, Term start, Term end) {
        super(alias, term, start, end);
    }

    @Override
    public BetweenCriterion replaceTable(Table currentTable, Table newTable) {
        term = term.replaceTable(currentTable, newTable);
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s BETWEEN %s AND %s",
                term.getSql(config),
                start.getSql(config),
                end.getSql(config)
        );
        return Utils.formatAliasSql(sql, alias, config);
    }
}
