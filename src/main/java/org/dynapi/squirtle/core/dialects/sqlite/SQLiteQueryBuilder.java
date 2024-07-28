package org.dynapi.squirtle.core.dialects.sqlite;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class SQLiteQueryBuilder extends QueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return SQLiteQuery.class; }

    protected boolean insertOrReplace = false;

    public SQLiteQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.SQLITE, wrapSetOperationQueries, wrapperClass == null ? SQLiteValueWrapper.class : wrapperClass, immutable, asKeyword);
    }

    public SQLiteQueryBuilder insertOrReplace(Term... terms) {
        applyTerms(terms);
        this.replace = true;
        this.insertOrReplace = true;
        return this;
    }

    protected String replaceSql(SqlAbleConfig config) {
        String prefix = insertOrReplace ? "INSERT OR " : "";
        return prefix + super.replaceSql(config);
    }
}
