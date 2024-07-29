package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Enumerator;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class BasicCriterion extends Criterion {
    protected final Enumerator comparator;
    protected Term left;
    protected Term right;

    public BasicCriterion(String alias, Enumerator comparator, Term left, Term right) {
        super(alias);
        this.comparator = comparator;
        this.left = left;
        this.right = right;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(left.nodes());
        nodes.addAll(right.nodes());
        return nodes;
    }

    @Override
    public Boolean isAggregate() {
        return Utils.resolveIsAggregate(left.isAggregate(), right.isAggregate());
    }

    @Override
    public BasicCriterion replaceTable(Table currentTable, Table newTable) {
        left = left.replaceTable(currentTable, newTable);
        right = right.replaceTable(currentTable, newTable);
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (config.getSecondaryQuoteChar() == null)
            config = config.withSecondaryQuoteChar("\"");

        String sql = String.format(
                "%s%s%s",
                left.getSql(config),
                comparator.value,
                right.getSql(config)
        );

        if (config.isWithAlias())
            return Utils.formatAliasSql(sql, alias, config);

        return sql;
    }
}
