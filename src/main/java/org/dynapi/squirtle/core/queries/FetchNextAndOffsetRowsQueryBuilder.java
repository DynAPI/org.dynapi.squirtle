package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class FetchNextAndOffsetRowsQueryBuilder extends QueryBuilder {
    public FetchNextAndOffsetRowsQueryBuilder(FetchNextAndOffsetRowsQueryBuilder original) {
        super(original);
    }

    public FetchNextAndOffsetRowsQueryBuilder(Config config) {
        super(config);
    }

    protected String limitSql(SqlAbleConfig ignored) {
        return String.format(" FETCH NEXT %d ROWS ONLY", limit);
    }

    protected String offsetSql(SqlAbleConfig ignored) {
        return String.format(" OFFSET %d ROWS", offset);
    }
}
