package org.dynapi.squirtle.core.dialects.sqlite;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.List;

public class SQLiteQueryBuilder extends QueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return SQLiteQuery.class; }

    protected boolean insertOrReplace = false;

    public SQLiteQueryBuilder(SQLiteQueryBuilder original) {
        super(original);
        this.insertOrReplace = original.insertOrReplace;
    }

    public SQLiteQueryBuilder() {
        this(Config.builder().build());
    }

    public SQLiteQueryBuilder(Config config) {
        super(config.toBuilder()
                .dialect(Dialects.SQLITE)
                .wrapperClass(config.getWrapperClass() == ValueWrapper.class ? SQLiteValueWrapper.class : config.getWrapperClass())
                .build()
        );
    }

    public SQLiteQueryBuilder as(String alias) {
        this.alias = alias;
        return this;
    }

    public SQLiteQueryBuilder insertOrReplace(Object... terms) {
        applyTerm(List.of(terms));
        this.replace = true;
        this.insertOrReplace = true;
        return this;
    }

    protected String replaceSql(SqlAbleConfig config) {
        String prefix = insertOrReplace ? "INSERT OR " : "";
        return prefix + super.replaceSql(config);
    }
}
