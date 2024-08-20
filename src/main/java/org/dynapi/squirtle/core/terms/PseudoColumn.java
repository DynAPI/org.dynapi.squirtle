package org.dynapi.squirtle.core.terms;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class PseudoColumn extends Term implements SqlAble {
    private final String name;

    public PseudoColumn(PseudoColumn original) {
        super(original);
        this.name = original.name;
    }

    public PseudoColumn(String name) {
        super((String) null);
        this.name = name;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return name;
    }
}
