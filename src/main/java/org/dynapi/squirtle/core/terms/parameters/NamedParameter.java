package org.dynapi.squirtle.core.terms.parameters;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class NamedParameter extends Parameter {
    public NamedParameter(String placeholder) {
        super(placeholder);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(":%s", placeholder);
    }
}
