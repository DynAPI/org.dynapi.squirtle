package org.dynapi.squirtle.core.dialects.clickhouse;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.queries.Database;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Table;

public class ClickHouseQuery extends Query {
    protected static QueryBuilder newBuilder(Object... args) {
        return Utils.newInstance(ClickHouseQueryBuilder.class, args);
    }

    public static ClickHouseDropQueryBuilder dropDatabase(Database database) {
        return new ClickHouseDropQueryBuilder().dropDatabase(database);
    }

    public static ClickHouseDropQueryBuilder dropTable(Table table) {
        return new ClickHouseDropQueryBuilder().dropTable(table);
    }

    public static ClickHouseDropQueryBuilder dropDictionary(String dictionaryName) {
        return new ClickHouseDropQueryBuilder().dropDictionary(dictionaryName);
    }

    public static ClickHouseDropQueryBuilder dropQuota(String quotaName) {
        return new ClickHouseDropQueryBuilder().dropQuota(quotaName);
    }

    public static ClickHouseDropQueryBuilder dropUser(String userName) {
        return new ClickHouseDropQueryBuilder().dropUser(user);
    }

    public static ClickHouseDropQueryBuilder dropView(String viewName) {
        return new ClickHouseDropQueryBuilder().dropView(viewName);
    }
}
