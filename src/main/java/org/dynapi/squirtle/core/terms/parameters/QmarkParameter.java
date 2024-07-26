package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class QmarkParameter extends Parameter {
    public QmarkParameter() {
        super("");
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return "?";
    }
}
