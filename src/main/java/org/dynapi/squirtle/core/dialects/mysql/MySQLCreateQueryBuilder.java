package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.queries.CreateQueryBuilder;

public class MySQLCreateQueryBuilder extends CreateQueryBuilder {
    public MySQLCreateQueryBuilder(MySQLCreateQueryBuilder original) {
        super(original);
    }

    public MySQLCreateQueryBuilder() {
        super();
    }

    public String sqlAbleQuoteChar() { return "`"; }
}
