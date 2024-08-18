package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

import java.util.HashSet;
import java.util.Set;

public class EmptyCriterion extends Criterion {
    @Override
    public Boolean isAggregate() {return null; }

    public EmptyCriterion(String alias) {
        super(alias);
    }

    @Override
    public Set<Table> getTables() {
        return new HashSet<>();
    }

    @Override
    public Set<Field> getFields() {
        return new HashSet<>();
    }

    @Override
    public ComplexCriterion and(Term other) {
        return (ComplexCriterion) other;
    }

    @Override
    public ComplexCriterion or(Term other) {
        return (ComplexCriterion) other;
    }

    @Override
    public ComplexCriterion xor(Term other) {
        return (ComplexCriterion) other;
    }
}
