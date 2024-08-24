package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.terms.criterion.Index;

import java.util.Collection;

public class Query {
    protected QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(QueryBuilder.class, args);
    }

    public QueryBuilder from(String tableName, Object... args) {
        return from(new Table(tableName), args);
    }

    public QueryBuilder from(Selectable table, Object... args) {
        return newBuilder(args).from(table);
    }

    public CreateQueryBuilder createTable(String tableName) {
        return createTable(new Table(tableName));
    }

    public CreateQueryBuilder createTable(Table table) {
        return new CreateQueryBuilder().createTable(table);
    }

    public CreateIndexBuilder createIndex(String indexName) {
        return createIndex(new Index(indexName));
    }

    public CreateIndexBuilder createIndex(Index index) {
        return new CreateIndexBuilder().createIndex(index);
    }

    public DropQueryBuilder dropDatabase(String databaseName) {
        return dropDatabase(new Database(databaseName));
    }

    public DropQueryBuilder dropDatabase(Database database) {
        return new DropQueryBuilder().dropDatabase(database);
    }

    public DropQueryBuilder dropTable(String tableName) {
        return dropTable(new Table(tableName));
    }

    public DropQueryBuilder dropTable(Table table) {
        return new DropQueryBuilder().dropTable(table);
    }

    public DropQueryBuilder dropUser(String username) {
        return new DropQueryBuilder().dropUser(username);
    }

    public DropQueryBuilder dropView(String view) {
        return new DropQueryBuilder().dropView(view);
    }

    public DropQueryBuilder dropIndex(String indexName) {
        return dropIndex(new Index(indexName));
    }

    public DropQueryBuilder dropIndex(Index database) {
        return new DropQueryBuilder().dropIndex(database);
    }

    public QueryBuilder into(String tableName, Object... args) {
        return into(new Table(tableName), args);
    }

    public QueryBuilder into(Table table, Object... args) {
        return newBuilder(args).into(table);
    }

    public QueryBuilder with(String tableName, String name, Object... args) {
        return with(new Table(tableName), name, args);
    }

    public QueryBuilder with(Selectable table, String name, Object... args) {
        return newBuilder(args).with(table, name);
    }

    public QueryBuilder select(Collection<Object> terms, Object... args) {
        return newBuilder(args).select(terms.toArray());
    }

    public QueryBuilder update(String tableName, Object... args) {
        return update(new Table(tableName), args);
    }

    public QueryBuilder update(Table table, Object... args) {
        return newBuilder(args).update(table);
    }
}
