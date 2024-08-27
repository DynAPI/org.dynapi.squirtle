package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class RangeCriterion extends Criterion {
    protected Term term;
    protected final Term start;
    protected final Term end;
    protected boolean isNegated = false;

    public RangeCriterion(RangeCriterion original) {
        super(original);
        this.term = CloneUtils.copyConstructorClone(original.term);
        this.start = CloneUtils.copyConstructorClone(original.start);
        this.end = CloneUtils.copyConstructorClone(original.end);
        this.isNegated = original.isNegated;
    }

    public RangeCriterion(Term term, Term start, Term end) {
        this.term = term;
        this.start = start;
        this.end = end;
    }

    public RangeCriterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(term.nodes());
        nodes.addAll(start.nodes());
        nodes.addAll(end.nodes());
        return nodes;
    }

    public RangeCriterion negated() {
        isNegated = true;
        return this;
    }
}
