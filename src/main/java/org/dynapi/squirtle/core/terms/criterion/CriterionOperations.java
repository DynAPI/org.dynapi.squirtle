package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.enums.BooleanComparison;
import org.dynapi.squirtle.core.terms.Term;

import java.util.List;

public interface CriterionOperations {

    default ComplexCriterion and(Term other) {
        return new ComplexCriterion(null, BooleanComparison.AND, (Term) this, other);
    }

    default ComplexCriterion or(Term other) {
        return new ComplexCriterion(null, BooleanComparison.OR, (Term) this, other);
    }

    default ComplexCriterion xor(Term other) {
        return new ComplexCriterion(null, BooleanComparison.XOR,  (Term) this, other);
    }

    static Criterion any(Term... terms) {
        return any(List.of(terms));
    }

    static Criterion any(List<Term> terms) {
        Criterion criterion = new EmptyCriterion(null);
        for (Term term : terms) {
            criterion = criterion.or(term);
        }
        return criterion;
    }

    static Criterion all(Term... terms) {
        return all(List.of(terms));
    }

    static Criterion all(List<Term> terms) {
        Criterion criterion = new EmptyCriterion(null);
        for (Term term : terms) {
            criterion = criterion.and(term);
        }
        return criterion;
    }
}
