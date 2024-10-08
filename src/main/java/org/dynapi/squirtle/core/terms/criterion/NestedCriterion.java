package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Comparator;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class NestedCriterion extends Criterion {
    protected final Comparator comparator;
    protected final ComplexCriterion nestedComparator;
    protected Term left;
    protected Term right;
    protected Term nested;

    public NestedCriterion(NestedCriterion original) {
        super(original);
        this.comparator = original.comparator;
        this.nestedComparator = CloneUtils.copyConstructorClone(original.nestedComparator);
        this.left = CloneUtils.copyConstructorClone(original.left);
        this.right = CloneUtils.copyConstructorClone(original.right);
        this.nested = CloneUtils.copyConstructorClone(original.nested);
    }

    // xxx: comparator is of type Comparator(enum)

    public NestedCriterion(Comparator comparator, ComplexCriterion nestedComparator, Term left, Term right, Term nested) {
        this.comparator = comparator;
        this.nestedComparator = nestedComparator;
        this.left = left;
        this.right = right;
        this.nested = nested;
    }

    public NestedCriterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(left.nodes());
        nodes.addAll(right.nodes());
        nodes.addAll(nested.nodes());
        return nodes;
    }

    @Override
    public Boolean isAggregate() {
        return Utils.resolveIsAggregate(left.isAggregate(), right.isAggregate(), nested.isAggregate());
    }

    @Override
    public NestedCriterion replaceTable(Table currentTable, Table newTable) {
        left = left.replaceTable(currentTable, newTable);
        right = right.replaceTable(currentTable, newTable);
        nested = nested.replaceTable(currentTable, newTable);
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s%s%s%s%s",
                left.getSql(config),
                comparator.value,
                right.getSql(config),
                nestedComparator.getValue(),
                nested.getSql(config)
        );

        if (config.isWithAlias())
            return Utils.formatAliasSql(sql, alias, config);

        return sql;
    }
}
