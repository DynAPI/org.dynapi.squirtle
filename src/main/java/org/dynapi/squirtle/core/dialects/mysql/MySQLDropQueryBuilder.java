package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.queries.DropQueryBuilder;

public class MySQLDropQueryBuilder extends DropQueryBuilder {
    public MySQLDropQueryBuilder(MySQLDropQueryBuilder original) {
        super(original);
    }

    public MySQLDropQueryBuilder() {
        super();
    }

    public String sqlAbleQuoteChar() { return "`"; }
}
