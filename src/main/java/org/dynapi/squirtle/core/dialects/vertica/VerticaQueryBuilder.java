package org.dynapi.squirtle.core.dialects.vertica;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;

public class VerticaQueryBuilder extends QueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return VerticaQuery.class; }

    protected String hint = null;

    public VerticaQueryBuilder(VerticaQueryBuilder original) {
        super(original);
        this.hint = original.hint;
    }

    public VerticaQueryBuilder() {
        this(Config.builder().build());
    }

    public VerticaQueryBuilder(Config config) {
        super(config.toBuilder()
                .dialect(Dialects.VERTICA)
                .build()
        );
    }

    public VerticaQueryBuilder as(String alias) {
        this.alias = alias;
        return this;
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
