package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.terms.criterion.Index;

import java.util.Collection;

public class Query {
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return QueryBuilder.class; }

    public final QueryBuilder newQueryBuilder() {
        return newQueryBuilder(null);
    }

    public final QueryBuilder newQueryBuilder(QueryBuilder.Config config) {
        Object[] args = config == null ? new Object[]{} : new Object[]{config};
        return Utils.newInstance(getQueryBuilderClass(), args);
    }

    //

    public QueryBuilder from(String tableName) {
        return from(tableName, null);
    }

    public QueryBuilder from(String tableName, QueryBuilder.Config config) {
        return from(new Table(tableName), config);
    }

    public QueryBuilder from(Selectable table) {
        return from(table, null);
    }

    public QueryBuilder from(Selectable table, QueryBuilder.Config config) {
        return newQueryBuilder(config).from(table);
    }

    //

    public CreateQueryBuilder createTable(String tableName) {
        return createTable(new Table(tableName));
    }

    public CreateQueryBuilder createTable(Table table) {
        return new CreateQueryBuilder().createTable(table);
    }

    //

    public CreateIndexBuilder createIndex(String indexName) {
        return createIndex(new Index(indexName));
    }

    public CreateIndexBuilder createIndex(Index index) {
        return new CreateIndexBuilder().createIndex(index);
    }

    //

    public DropQueryBuilder dropDatabase(String databaseName) {
        return dropDatabase(new Database(databaseName));
    }

    public DropQueryBuilder dropDatabase(Database database) {
        return new DropQueryBuilder().dropDatabase(database);
    }

    //

    public DropQueryBuilder dropTable(String tableName) {
        return dropTable(new Table(tableName));
    }

    public DropQueryBuilder dropTable(Table table) {
        return new DropQueryBuilder().dropTable(table);
    }

    //

    public DropQueryBuilder dropUser(String username) {
        return new DropQueryBuilder().dropUser(username);
    }

    //

    public DropQueryBuilder dropView(String view) {
        return new DropQueryBuilder().dropView(view);
    }


    public DropQueryBuilder dropIndex(String indexName) {
        return dropIndex(new Index(indexName));
    }

    public DropQueryBuilder dropIndex(Index database) {
        return new DropQueryBuilder().dropIndex(database);
    }

    //

    public QueryBuilder into(String tableName) {
        return into(tableName, null);
    }

    public QueryBuilder into(String tableName, QueryBuilder.Config config) {
        return into(new Table(tableName), config);
    }

    public QueryBuilder into(Table table) {
        return into(table, null);
    }

    public QueryBuilder into(Table table, QueryBuilder.Config config) {
        return newQueryBuilder(config).into(table);
    }

    //

    public QueryBuilder with(String tableName, String name) {
        return with(tableName, name, null);
    }

    public QueryBuilder with(String tableName, String name, QueryBuilder.Config config) {
        return with(new Table(tableName), name, config);
    }

    public QueryBuilder with(Selectable table, String name) {
        return with(table, name, null);
    }

    public QueryBuilder with(Selectable table, String name, QueryBuilder.Config config) {
        return newQueryBuilder(config).with(table, name);
    }

    //

    public QueryBuilder select(Collection<Object> terms) {
        return select(terms, null);
    }

    public QueryBuilder select(Collection<Object> terms, QueryBuilder.Config config) {
        return newQueryBuilder(config).select(terms.toArray());
    }

    //

    public QueryBuilder update(String tableName) {
        return update(tableName, null);
    }

    public QueryBuilder update(String tableName, QueryBuilder.Config config) {
        return update(new Table(tableName), config);
    }

    public QueryBuilder update(Table table) {
        return update(table, null);
    }

    public QueryBuilder update(Table table, QueryBuilder.Config config) {
        return newQueryBuilder(config).update(table);
    }
}
