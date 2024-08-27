package org.dynapi.squirtle.core.queries;

import lombok.Getter;
import lombok.Setter;
import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Objects;

public class AliasedQuery implements Selectable, SqlAble {
    @Getter @Setter
    private String alias;
    @Getter
    private final String name;
    private final Selectable query;

    public AliasedQuery(AliasedQuery original) {
        this.alias = original.alias;
        this.name = original.name;
        this.query = CloneUtils.copyConstructorClone(original.query);
    }

    public AliasedQuery(String name, Selectable query) {
        this.alias = name;
        this.name = name;
        this.query = query;
    }

    public AliasedQuery as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (query == null)
            return name;
        return query.getSql(config);
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
