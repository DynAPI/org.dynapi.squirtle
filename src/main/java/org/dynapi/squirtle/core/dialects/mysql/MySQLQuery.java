package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.*;

public class MySQLQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(MySQLQueryBuilder.class, args);
    }

    public static MySQLLoadQueryBuilder load(String file) {
        return new MySQLLoadQueryBuilder().load(file);
    }

    public static MySQLCreateQueryBuilder createTable(Table table) {
        return (MySQLCreateQueryBuilder) new MySQLCreateQueryBuilder().createTable(table);
    }

    public static MySQLDropQueryBuilder dropTable(Table table) {
        return (MySQLDropQueryBuilder) new MySQLDropQueryBuilder().dropTable(table);
    }
}
