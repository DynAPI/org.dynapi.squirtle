package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

public class EmptyCriterion extends Criterion {
    @Override
    public Boolean isAggregate() {return null; }

    public EmptyCriterion(String alias) {
        super(alias);
    }

    @Override
    public Table[] getTables() {
        return new Table[0];
    }

    @Override
    public Field[] getFields() {
        return new Field[0];
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
