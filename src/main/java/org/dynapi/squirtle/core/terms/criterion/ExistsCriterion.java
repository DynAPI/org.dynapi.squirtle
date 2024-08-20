package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class ExistsCriterion extends Criterion {
    protected Term container;
    protected boolean isNegated;

    public ExistsCriterion(ExistsCriterion original) {
        super(original);
        this.container = CloneUtils.copyConstructorClone(original.container);
        this.isNegated = original.isNegated;
    }

    public ExistsCriterion(String alias, Term container) {
        super(alias);
        this.container = container;
        this.isNegated = false;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(
                "%sEXISTS %s",
                isNegated ? "NOT " : "",
                container.getSql(config)
        );
    }

    public ExistsCriterion negated() {
        isNegated = true;
        return this;
    }
}
