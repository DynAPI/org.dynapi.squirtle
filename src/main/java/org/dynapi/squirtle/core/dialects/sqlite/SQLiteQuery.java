package org.dynapi.squirtle.core.dialects.sqlite;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class SQLiteQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(SQLiteQueryBuilder.class, args);
    }
}
