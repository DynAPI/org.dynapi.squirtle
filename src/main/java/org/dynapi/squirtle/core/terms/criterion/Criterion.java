package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.enums.BooleanComp;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

public class Criterion extends Term implements CriterionOperations, SqlAble {
    public Criterion(String alias) {
        super(alias);
    }

    @Override
    public Criterion replaceTable(Table currentTable, Table newTable) {
        return (Criterion) super.replaceTable(currentTable, newTable);
    }

    public ComplexCriterion and(Term other) {
        return new ComplexCriterion(null, BooleanComp.AND, this, other);
    }

    public ComplexCriterion or(Term other) {
        return new ComplexCriterion(null, BooleanComp.OR, this, other);
    }

    public  ComplexCriterion xor(Term other) {
        return new ComplexCriterion(null, BooleanComp.XOR, this, other);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        throw new RuntimeException("Not Implemented");
    }
}
