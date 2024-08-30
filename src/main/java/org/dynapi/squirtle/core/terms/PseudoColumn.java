package org.dynapi.squirtle.core.terms;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

/**
 * @see org.dynapi.squirtle.core.PseudoColumns
 */
public class PseudoColumn extends Term implements SqlAble {
    private final String name;

    public PseudoColumn(PseudoColumn original) {
        super(original);
        this.name = original.name;
    }

    public PseudoColumn(String name) {
        this.name = name;
    }

    public PseudoColumn as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return name;
    }
}
