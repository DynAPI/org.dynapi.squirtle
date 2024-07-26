package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class ContainsCriterion extends Criterion {
    protected Term term;
    protected final Term container;
    protected boolean isNegated;

    public ContainsCriterion(String alias, Term term, Term container) {
        super(alias);
        this.term = term;
        this.container = container;
        isNegated = false;
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
}
