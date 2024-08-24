package org.dynapi.squirtle.core.dialects.clickhouse;

import org.dynapi.squirtle.core.queries.*;

public class ClickHouseQuery extends Query {
    @Override
    protected Class<? extends QueryBuilder> getQueryBuilderClass() { return ClickHouseQueryBuilder.class; }

    @Override
    public ClickHouseDropQueryBuilder dropDatabase(Database database) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropDatabase(database);
    }

    @Override
    public ClickHouseDropQueryBuilder dropTable(Table table) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropTable(table);
    }

    public ClickHouseDropQueryBuilder dropDictionary(String dictionaryName) {
        return new ClickHouseDropQueryBuilder().dropDictionary(dictionaryName);
    }

    public ClickHouseDropQueryBuilder dropQuota(String quotaName) {
        return new ClickHouseDropQueryBuilder().dropQuota(quotaName);
    }

    @Override
    public ClickHouseDropQueryBuilder dropUser(String userName) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropUser(userName);
    }

    @Override
    public ClickHouseDropQueryBuilder dropView(String viewName) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropView(viewName);
    }
}
