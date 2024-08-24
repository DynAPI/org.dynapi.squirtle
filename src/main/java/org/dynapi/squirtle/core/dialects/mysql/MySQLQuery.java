package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.*;

public class MySQLQuery extends Query {
    @Override
    protected QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(MySQLQueryBuilder.class, args);
    }

    public MySQLLoadQueryBuilder load(String file) {
        return new MySQLLoadQueryBuilder().load(file);
    }

    @Override
    public MySQLCreateQueryBuilder createTable(Table table) {
        return (MySQLCreateQueryBuilder) new MySQLCreateQueryBuilder().createTable(table);
    }

    @Override
    public MySQLDropQueryBuilder dropTable(Table table) {
        return (MySQLDropQueryBuilder) new MySQLDropQueryBuilder().dropTable(table);
    }
}
