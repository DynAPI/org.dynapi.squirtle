package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class FetchNextAndOffsetRowsQueryBuilder extends QueryBuilder {
    public FetchNextAndOffsetRowsQueryBuilder(FetchNextAndOffsetRowsQueryBuilder original) {
        super(original);
    }

    public FetchNextAndOffsetRowsQueryBuilder() {
        this(Config.builder().build());
    }

    public FetchNextAndOffsetRowsQueryBuilder(Config config) {
        super(config);
    }

    public FetchNextAndOffsetRowsQueryBuilder as(String alias) {
        this.alias = alias;
        return this;
    }


    protected String limitSql(SqlAbleConfig ignored) {
        return String.format(" FETCH NEXT %d ROWS ONLY", limit);
    }

    protected String offsetSql(SqlAbleConfig ignored) {
        return String.format(" OFFSET %d ROWS", offset);
    }
}
