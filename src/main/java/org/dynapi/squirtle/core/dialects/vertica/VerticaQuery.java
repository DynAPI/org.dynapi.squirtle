package org.dynapi.squirtle.core.dialects.vertica;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Table;

public class VerticaQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(QueryBuilder.class, args);
    }

    public static VerticaCopyQueryBuilder fromFile(String file) {
        return new VerticaCopyQueryBuilder().fromFile(file);
    }

    public static VerticaCreateQueryBuilder createTable(Table table) {
        return (VerticaCreateQueryBuilder) new VerticaCreateQueryBuilder().createTable(table);
    }
}
