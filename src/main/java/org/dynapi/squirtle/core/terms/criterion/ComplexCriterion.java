package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.enums.Comparator;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

import java.util.Objects;

public class ComplexCriterion extends BasicCriterion {
    public ComplexCriterion(ComplexCriterion original) {
        super(original);
    }

    public ComplexCriterion(String alias, Comparator comparator, Term left, Term right) {
        super(alias, comparator, left, right);
    }

    public String getValue() {
        return comparator.value;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s %s %s",
                left.getSql(config.withSubCriterion(needsBrackets(left))),
                comparator.value,
                right.getSql(config.withSubCriterion(needsBrackets(right)))
        );

        if (config.isSubCriterion())
            return String.format("(%s)", sql);
        return sql;
    }

    private boolean needsBrackets(Term term) {
        return term instanceof ComplexCriterion && Objects.equals(((ComplexCriterion) term).comparator, comparator);
    }
}
