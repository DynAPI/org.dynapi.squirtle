package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Table;

public class MySQLQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(MySQLQueryBuilder.clss, args);
    }

    public static MySQLLoadQueryBuilder load(String file) {
        return new MySQLLoadQueryBuilder().load(file);
    }

    public static MySQLCreateQueryBuilder createTable(Table table) {
        return new MySQLCreateQueryBuilder().createTable(table);
    }

    public static MySQLDropQueryBuilder dropTable(Table table) {
        return new MySQLDropQueryBuilder().dropTable(table);
    }
}
