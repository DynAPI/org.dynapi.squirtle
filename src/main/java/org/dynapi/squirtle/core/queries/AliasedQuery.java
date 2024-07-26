package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Objects;

public class AliasedQuery extends Selectable implements SqlAble {
    private final String name;
    private final Selectable query;

    public AliasedQuery(String name, Selectable query) {
        super(name);
        this.name = name;
        this.query = query;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (query == null)
            return name;
        return ((SqlAble) query).getSql(config);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AliasedQuery && Objects.equals(name, ((AliasedQuery) obj).name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
