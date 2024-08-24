package org.dynapi.squirtle.core.dialects.clickhouse;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.*;

public class ClickHouseQuery extends Query {
    protected QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(ClickHouseQueryBuilder.class, args);
    }

    public ClickHouseDropQueryBuilder dropDatabase(Database database) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropDatabase(database);
    }

    public ClickHouseDropQueryBuilder dropTable(Table table) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropTable(table);
    }

    public ClickHouseDropQueryBuilder dropDictionary(String dictionaryName) {
        return new ClickHouseDropQueryBuilder().dropDictionary(dictionaryName);
    }

    public ClickHouseDropQueryBuilder dropQuota(String quotaName) {
        return new ClickHouseDropQueryBuilder().dropQuota(quotaName);
    }

    public ClickHouseDropQueryBuilder dropUser(String userName) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropUser(userName);
    }

    public ClickHouseDropQueryBuilder dropView(String viewName) {
        return (ClickHouseDropQueryBuilder) new ClickHouseDropQueryBuilder().dropView(viewName);
    }
}
