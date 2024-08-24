package org.dynapi.squirtle.core.dialects.oracle;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.FetchNextAndOffsetRowsQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class OracleQueryBuilder extends FetchNextAndOffsetRowsQueryBuilder {
    public String sqlAbleQuoteChar() { return null; }
    public Class<? extends Query> sqlAbleQueryClass() { return OracleQuery.class; }

    public OracleQueryBuilder(OracleQueryBuilder original) {
        super(original);
    }

    public OracleQueryBuilder(Config config) {
        super(config.toBuilder()
                .dialect(Dialects.ORACLE)
                .build()
        );
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        config = config.withGroupByAlias(false);
        return super.getSql(config);
    }

    protected String applyPagination(String queryString) {
        SqlAbleConfig emptyConfig = SqlAbleConfig.builder().build();

        if (offset > 0)
            queryString += offsetSql(emptyConfig);

        if (limit > 0)
            queryString += limitSql(emptyConfig);

        return queryString;
    }
}
