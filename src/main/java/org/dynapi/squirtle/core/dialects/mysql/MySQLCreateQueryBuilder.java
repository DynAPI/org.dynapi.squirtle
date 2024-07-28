package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.queries.CreateQueryBuilder;

public class MySQLCreateQueryBuilder extends CreateQueryBuilder {
    public String sqlAbleQuoteChar() { return "`"; }
}
