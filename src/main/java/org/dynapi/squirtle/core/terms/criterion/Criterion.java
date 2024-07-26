package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.enums.BooleanComp;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Term;

import java.util.List;

public class Criterion extends Term implements SqlAble {
    public Criterion(String alias) {
        super(alias);
    }

    @Override
    public Criterion replaceTable(Table currentTable, Table newTable) {
        return (Criterion) super.replaceTable(currentTable, newTable);
    }

    public ComplexCriterion and(Term other) {
        return new ComplexCriterion(null, BooleanComp.AND, (Term) this, other);
    }

    public ComplexCriterion or(Term other) {
        return new ComplexCriterion(null, BooleanComp.OR, (Term) this, other);
    }

    public  ComplexCriterion xor(Term other) {
        return new ComplexCriterion(null, BooleanComp.XOR, (Term) this, other);
    }

    public static Criterion any(Term... terms) {
        return any(List.of(terms));
    }

    public static Criterion any(List<Term> terms) {
        Criterion criterion = new EmptyCriterion(null);
        for (Term term : terms) {
            criterion = criterion.or(term);
        }
        return criterion;
    }

    public static Criterion all(Term... terms) {
        return all(List.of(terms));
    }

    public static Criterion all(List<Term> terms) {
        Criterion criterion = new EmptyCriterion(null);
        for (Term term : terms) {
            criterion = criterion.and(term);
        }
        return criterion;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        throw new RuntimeException("Not Implemented");
    }
}
