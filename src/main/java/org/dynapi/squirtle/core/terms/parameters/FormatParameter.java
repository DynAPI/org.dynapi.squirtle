package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class FormatParameter extends Parameter {
    public FormatParameter(FormatParameter original) {
        super(original);
    }

    public FormatParameter() {
        super("");
    }

    public FormatParameter as(String alias) {
        this.alias = alias;
        return this;
    }


    @Override
    public String getSql(SqlAbleConfig config) {
        return "%s";
    }
}
