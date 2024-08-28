package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Table;

public class MySQLQuery extends Query {
    @Override
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return MySQLQueryBuilder.class; }

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
