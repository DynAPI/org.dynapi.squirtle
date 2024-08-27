package org.dynapi.squirtle.core.dialects.redshift;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class RedshiftQueryBuilder extends QueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return RedshiftQuery.class; }

    public RedshiftQueryBuilder(RedshiftQueryBuilder original) {
        super(original);
    }

    public RedshiftQueryBuilder() {
        this(Config.builder().build());
    }

    public RedshiftQueryBuilder as(String alias) {
        this.alias = alias;
        return this;
    }

    public RedshiftQueryBuilder(Config config) {
        super(config.toBuilder()
                .dialect(Dialects.REDSHIFT)
                .build()
        );
    }
}
