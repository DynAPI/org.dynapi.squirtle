package org.dynapi.squirtle.core.dialects.vertica;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.CreateQueryBuilder;
import org.dynapi.squirtle.core.queries.Query;

public class VerticaCreateQueryBuilder extends CreateQueryBuilder {
    public Class<? extends Query> sqlAbleQueryClass() { return VerticaQuery.class; }

    protected boolean local = false;
    protected boolean preserveRows = false;

    public VerticaCreateQueryBuilder(VerticaCreateQueryBuilder original) {
        super(original);
        this.local = original.local;
        this.preserveRows = original.preserveRows;
    }

    public VerticaCreateQueryBuilder() {
        super(Dialects.VERTICA);
    }

    public VerticaCreateQueryBuilder local() {
        if (!temporary)
            throw new RuntimeException("'Query' object has no attribute temporary");

        this.local = true;
        return this;
    }

    public VerticaCreateQueryBuilder preserveRows() {
        if (!temporary)
            throw new RuntimeException("'Query' object has no attribute temporary");

        this.preserveRows = true;
        return this;
    }

    @Override
    public String createTableSql(SqlAbleConfig config) {
        return String.format(
                "CREATE %s%sTABLE %s",
                local ? "LOCAL " : "",
                temporary ? "TEMPORARY " : "",
                createTable.getSql(config)
        );
    }

    @Override
    protected String tableOptionsSql(SqlAbleConfig config) {
        String sql = super.tableOptionsSql(config);
        sql += preserveRowsSql(config);
        return sql;
    }

    @Override
    protected String asSelectSql(SqlAbleConfig config) {
        return String.format(
                "%s AS (%s)",
                preserveRowsSql(config),
                asSelect.getSql(config)
        );
    }

    protected String preserveRowsSql(SqlAbleConfig ignored) {
        return preserveRows ? " ON COMMIT PRESERVE ROWS" : "";
    }
}
