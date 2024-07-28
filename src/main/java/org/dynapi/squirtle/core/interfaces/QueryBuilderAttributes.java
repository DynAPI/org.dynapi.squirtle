package org.dynapi.squirtle.core.interfaces;

import org.dynapi.squirtle.core.queries.Query;

public interface QueryBuilderAttributes {
    default String sqlAbleQuoteChar() { return null; }
    default String sqlAbleSecondaryQuoteChar() { return null; }
    default String sqlAbleAliasQuoteChar() { return null; }
    default String sqlAbleQueryAliasQuoteChar() { return null; }
    default Class<? extends Query> sqlAbleQueryClass() { return Query.class; }

    default SqlAbleConfig getSqlAbleConfigWithDefaults() {
        return getSqlAbleConfigWithDefaults(null);
    }

    default SqlAbleConfig getSqlAbleConfigWithDefaults(SqlAbleConfig base) {
        if (base == null) base = SqlAbleConfig.builder().build();
        SqlAbleConfig.SqlAbleConfigBuilder builder = base.toBuilder();

        if (base.getQuoteChar() == null)
            builder.quoteChar(this.sqlAbleQuoteChar());
        if (base.getSecondaryQuoteChar() == null)
            builder.secondaryQuoteChar(this.sqlAbleSecondaryQuoteChar());
        if (base.getAliasQuoteChar() == null)
            builder.aliasQuoteChar(this.sqlAbleAliasQuoteChar());
        if (base.getQueryAliasQuoteChar() == null)
            builder.queryAliasQuoteChar(this.sqlAbleQueryAliasQuoteChar());
        if (base.getQueryClass() == null)
            builder.queryClass(this.sqlAbleQueryClass());

        return builder.build();
    }
}
