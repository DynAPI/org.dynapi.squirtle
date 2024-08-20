package org.dynapi.squirtle.core.dialects.mssql;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.FetchNextAndOffsetRowsQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;
import org.dynapi.squirtle.errors.QueryException;

public class MSSQLQueryBuilder extends FetchNextAndOffsetRowsQueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return MSSQLQuery.class; }

    protected Integer top = null;
    protected boolean topWithTies = false;
    protected boolean topPercent = false;

    public MSSQLQueryBuilder(MSSQLQueryBuilder original) {
        super(original);
        this.top = original.top;
        this.topWithTies = original.topWithTies;
        this.topPercent = original.topPercent;
    }

    public MSSQLQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.MSSQL, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }

    public MSSQLQueryBuilder top(int value, boolean percent, boolean withTies) {
        if (percent && !(0 <= value && value <= 100))
            throw new QueryException("TOP value must be between 0 and 100 when `pecent` is specified");

        this.topPercent = percent;
        this.topWithTies = withTies;
        return this;
    }

    protected String applyPagination(String queryString) {
        SqlAbleConfig emptyConfig = SqlAbleConfig.builder().build();

        if (limit != null || offset != null)
            queryString += offsetSql(emptyConfig);

        if (limit != null)
            queryString += limitSql(emptyConfig);

        return queryString;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return super.getSql(config.withGroupByAlias(false));
    }

    protected String topSql(SqlAbleConfig config) {
        String topStatement = "";

        if (top != null) {
            topStatement += String.format("TOP (%d) ", top);
            if (topPercent)
                topStatement = String.format("%sPERCENT", topStatement);
            if (topWithTies)
                topStatement = String.format("%sWITH TIES", topStatement);
        }

        return topStatement;
    }

    protected String selectSql(SqlAbleConfig config) {
        SqlAbleConfig termConfig = config.withWithAlias(true).withSubQuery(true);
        return String.format(
                "SELECT %s%s%s",
                topSql(config),
                distinct ? "DISTINCT " : "",
                String.join(",", selects.stream().map(term -> term.getSql(termConfig)).toList())
        );
    }
}
