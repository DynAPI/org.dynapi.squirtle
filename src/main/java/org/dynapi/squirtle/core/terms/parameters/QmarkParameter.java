package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class QmarkParameter extends Parameter {
    public QmarkParameter(QmarkParameter original) {
        super(original);
    }

    public QmarkParameter() {
        super("");
    }

    public QmarkParameter as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return "?";
    }
}
