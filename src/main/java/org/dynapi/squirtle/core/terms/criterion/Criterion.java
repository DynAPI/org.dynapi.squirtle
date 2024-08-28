package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.enums.BooleanComparison;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

public class Criterion extends Term implements CriterionOperations, SqlAble {
    public Criterion(Criterion original) {
        super(original);
    }

    public Criterion() {}

    public Criterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public Criterion replaceTable(Table currentTable, Table newTable) {
        return (Criterion) super.replaceTable(currentTable, newTable);
    }

    public ComplexCriterion and(Term other) {
        return new ComplexCriterion(BooleanComparison.AND, this, other);
    }

    public ComplexCriterion or(Term other) {
        return new ComplexCriterion(BooleanComparison.OR, this, other);
    }

    public  ComplexCriterion xor(Term other) {
        return new ComplexCriterion(BooleanComparison.XOR, this, other);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        throw new RuntimeException("Not Implemented");
    }
}
