package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.queries.DropQueryBuilder;

public class MySQLDropQueryBuilder extends DropQueryBuilder {
    public String sqlAbleQuoteChar() { return "`"; }
}
