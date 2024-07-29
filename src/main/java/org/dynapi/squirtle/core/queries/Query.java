package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Index;

import java.util.Collection;

public class Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(QueryBuilder.class, args);
    }

    public static QueryBuilder from(String tableName, Object... args) {
        return from(new Table(null, tableName), args);
    }

    public static QueryBuilder from(Selectable table, Object... args) {
        return newBuilder(args).from(table);
    }

    public static CreateQueryBuilder createTable(String tableName) {
        return createTable(new Table(null, tableName));
    }

    public static CreateQueryBuilder createTable(Table table) {
        return new CreateQueryBuilder().createTable(table);
    }

    public static CreateIndexBuilder createIndex(String index) {
        return createIndex(new Index(null, index));
    }

    public static CreateIndexBuilder createIndex(Index index) {
        return new CreateIndexBuilder().createIndex(index);
    }

    public static DropQueryBuilder dropDatabase(String databaseName) {
        return dropDatabase(new Database(databaseName));
    }

    public static DropQueryBuilder dropDatabase(Database database) {
        return new DropQueryBuilder().dropDatabase(database);
    }

    public static DropQueryBuilder dropTable(String tableName) {
        return dropTable(new Table(null, tableName));
    }

    public static DropQueryBuilder dropTable(Table table) {
        return new DropQueryBuilder().dropTable(table);
    }

    public static DropQueryBuilder dropUser(String username) {
        return new DropQueryBuilder().dropUser(username);
    }

    public static DropQueryBuilder dropView(String view) {
        return new DropQueryBuilder().dropView(view);
    }

    public static DropQueryBuilder dropIndex(String indexName) {
        return dropIndex(new Index(null, indexName));
    }

    public static DropQueryBuilder dropIndex(Index database) {
        return new DropQueryBuilder().dropIndex(database);
    }

    public static QueryBuilder into(String tableName, Object... args) {
        return into(new Table(null, tableName), args);
    }

    public static QueryBuilder into(Table table, Object... args) {
        return newBuilder(args).into(table);
    }

    public static QueryBuilder with(String tableName, String name, Object... args) {
        return with(new Table(null, tableName), name, args);
    }

    public static QueryBuilder with(Selectable table, String name, Object... args) {
        return newBuilder(args).with(table, name);
    }

    public static QueryBuilder select(Collection<Term> terms, Object... args) {
        return newBuilder(args).select(terms.toArray());
    }

    public static QueryBuilder update(String tableName, Object... args) {
        return update(new Table(null, tableName), args);
    }

    public static QueryBuilder update(Table table, Object... args) {
        return newBuilder(args).update(table);
    }
}
