package org.dynapi.squirtle.core.dialects.vertica;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class VerticaQueryBuilder extends QueryBuilder {
    public static Class<? extends Query> QUERY_CLASS = VerticaQuery.class;

    protected String hint = null;

    public VerticaQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.VERTICA, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }

    public VerticaQueryBuilder hint(String label) {
        this.hint = label;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = super.getSql(config);

        if (hint != null)
            sql = String.format("%s%s%s",
                    sql.substring(0, 7),
                    String.format("/*+label(%s)*/", hint),
                    sql.substring(6)
            );

        return sql;
    }
}
