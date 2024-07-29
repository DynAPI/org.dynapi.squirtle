package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class NullCriterion extends Criterion {
    protected Term term;

    public NullCriterion(String alias, Term term) {
        super(alias);
        this.term = term;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(term.nodes());
        return nodes;
    }

    @Override
    public NullCriterion replaceTable(Table currentTable, Table newTable) {
        term = term.replaceTable(currentTable, newTable);
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s IS NULL",
                term.getSql(config)
        );
        return Utils.formatAliasSql(sql, alias, config);
    }
}
