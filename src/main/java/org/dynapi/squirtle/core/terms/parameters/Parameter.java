package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;

public class Parameter extends Term implements SqlAble {
    public final Boolean isAggregate = null;

    protected final String placeholder;

    public Parameter(Parameter original) {
        super(original);
        this.placeholder = original.placeholder;
    }

    public Parameter(Integer placeholder) {
        this.placeholder = placeholder.toString();
    }

    public Parameter(String placeholder) {
        this.placeholder = placeholder;
    }

    public Parameter as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return placeholder;
    }
}
