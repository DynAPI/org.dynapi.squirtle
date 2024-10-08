package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class NamedParameter extends Parameter {
    public NamedParameter(NamedParameter original) {
        super(original);
    }

    public NamedParameter(String placeholder) {
        super(placeholder);
    }

    public NamedParameter as(String alias) {
        this.alias = alias;
        return this;
    }


    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(":%s", placeholder);
    }
}
