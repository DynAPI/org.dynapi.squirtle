package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class ContainsCriterion extends Criterion {
    protected Term term;
    protected final Term container;
    protected boolean isNegated;

    public ContainsCriterion(ContainsCriterion original) {
        super(original);
        this.term = CloneUtils.copyConstructorClone(original.term);
        this.container = CloneUtils.copyConstructorClone(original.container);
        this.isNegated = original.isNegated;
    }

    public ContainsCriterion(Term term, Term container) {
        this.term = term;
        this.container = container;
        isNegated = false;
    }

    public ContainsCriterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(term.nodes());
        nodes.addAll(container.nodes());
        return nodes;
    }

    @Override
    public Boolean isAggregate() {
        return term.isAggregate();
    }

    @Override
    public ContainsCriterion replaceTable(Table currentTable, Table newTable) {
        term = term.replaceTable(currentTable, newTable);
        return this;
    }

    public ContainsCriterion negated() {
        isNegated = true;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(
                "%s %sIN %s",
                term.getSql(config),
                isNegated ? "NOT " : "",
                container.getSql(config.withSubQuery(true))
        );
    }
}
