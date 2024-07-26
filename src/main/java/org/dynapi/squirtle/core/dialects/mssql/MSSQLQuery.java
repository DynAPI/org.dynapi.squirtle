package org.dynapi.squirtle.core.dialects.mssql;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class MSSQLQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(MSSQLQueryBuilder.clss, args);
    }
}
