package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class EmptyCriterion extends Criterion {
    @Override
    public Boolean isAggregate() {return null; }

    public EmptyCriterion(String alias) {
        super(alias);
    }

    @Override
    public List<Table> getTables() {
        return new ArrayList<>();
    }

    @Override
    public List<Field> getFields() {
        return new ArrayList<>();
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
