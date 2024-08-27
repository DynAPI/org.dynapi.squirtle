package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.queries.Table;

import java.util.HashSet;
import java.util.Set;

public class EmptyCriterion extends Criterion {
    @Override
    public Boolean isAggregate() {return null; }

    public EmptyCriterion(EmptyCriterion original) {
        super(original);
    }

    public EmptyCriterion() {}

    public EmptyCriterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public Set<Table> getTables() {
        return new HashSet<>();
    }

    @Override
    public Set<Field> getFields() {
        return new HashSet<>();
    }

    public <C extends Criterion> C and(C other) {
        return other;
    }

    public <C extends Criterion> C or(C other) {
        return other;
    }

    public <C extends Criterion> C xor(C other) {
        return other;
    }
}
