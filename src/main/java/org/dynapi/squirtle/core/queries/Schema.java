package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Objects;

public class Schema implements SqlAble {
    private final String name;
    private final Schema parent;

    public Schema(Schema original) {
        this.name = original.name;
        this.parent = CloneUtils.copyConstructorClone(original.parent);
    }

    public Schema(String name) {
        this(name, null);
    }

    public Schema(String name, Schema parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schema schema)) return false;
        return Objects.equals(name, schema.name) && Objects.equals(parent, schema.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent);
    }

    public Table table(String name) {
        return new Table(name, this);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String schemaSql = Utils.formatQuotes(name, config.getQuoteChar());

        if (parent != null)
            return String.format("%s.%s", parent.getSql(config), schemaSql);

        return schemaSql;
    }
}
