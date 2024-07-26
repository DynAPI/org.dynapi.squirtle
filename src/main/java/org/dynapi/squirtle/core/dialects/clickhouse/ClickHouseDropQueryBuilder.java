package org.dynapi.squirtle.core.dialects.clickhouse;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.DropQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;

public class ClickHouseDropQueryBuilder extends DropQueryBuilder {
    public static Class<? extends Query> QUERY_CLASS = ClickHouseQuery.class;

    protected String clusterName = null;

    public ClickHouseDropQueryBuilder() {
        super(Dialects.CLICKHOUSE);
    }

    public ClickHouseDropQueryBuilder dropDictionary(String dictionaryName) {
        setTarget("DICTIONARY", dictionaryName);
        return this;
    }

    public ClickHouseDropQueryBuilder dropQuota(String quotaName) {
        setTarget("QUOTA", quotaName);
        return this;
    }

    public ClickHouseDropQueryBuilder onCluster(String clusterName) {
        if (clusterName == null)
            throw new RuntimeException("'DropQuery' object already has attribute clusterName");

        this.clusterName = clusterName;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = super.getSql(config);

        if (dropTargetKind != "DICTIONARY" && clusterName != null)
            sql += String.format(" ON CLUSTER %s", Utils.formatQuotes(clusterName, config.getQuoteChar()));

        return sql;
    }
}
