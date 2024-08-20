package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class FetchNextAndOffsetRowsQueryBuilder extends QueryBuilder {
    public FetchNextAndOffsetRowsQueryBuilder(FetchNextAndOffsetRowsQueryBuilder original) {
        super(original);
    }

    public FetchNextAndOffsetRowsQueryBuilder(Dialects dialect, Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(dialect, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }

    protected String limitSql(SqlAbleConfig ignored) {
        return String.format(" FETCH NEXT %d ROWS ONLY", limit);
    }

    protected String offsetSql(SqlAbleConfig ignored) {
        return String.format(" OFFSET %d ROWS", offset);
    }
}
