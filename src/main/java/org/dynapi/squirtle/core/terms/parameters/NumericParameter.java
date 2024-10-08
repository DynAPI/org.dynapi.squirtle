package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class NumericParameter extends Parameter {
    public NumericParameter(NumericParameter original) {
        super(original);
    }

    public NumericParameter(Integer placeholder) {
        super(placeholder);
    }

    public NumericParameter as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(":%s", placeholder);
    }
}
