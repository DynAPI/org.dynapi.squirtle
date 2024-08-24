package org.dynapi.squirtle.core.dialects.vertica;

import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.Table;

public class VerticaQuery extends Query {
    public VerticaCopyQueryBuilder fromFile(String file) {
        return new VerticaCopyQueryBuilder().fromFile(file);
    }

    @Override
    public VerticaCreateQueryBuilder createTable(Table table) {
        return (VerticaCreateQueryBuilder) new VerticaCreateQueryBuilder().createTable(table);
    }
}
