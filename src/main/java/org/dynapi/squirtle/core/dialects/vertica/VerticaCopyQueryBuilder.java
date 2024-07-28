package org.dynapi.squirtle.core.dialects.vertica;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.Table;

public class VerticaCopyQueryBuilder implements SqlAble {
    public Class<? extends Query> sqlAbleQueryClass() { return VerticaQuery.class; }

    protected Table copyTable = null;
    protected String fromFile = null;

    public VerticaCopyQueryBuilder fromFile(String file) {
        this.fromFile = file;
        return this;
    }

    public VerticaCopyQueryBuilder copy(Table table) {
        this.copyTable = table;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = "";
        if (copyTable != null && fromFile != null) {
            sql += copyTableSql(config);
            sql += fromFileSql(config);
            sql += optionsSql(config);
        }
        return sql;
    }

    protected String copyTableSql(SqlAbleConfig config) {
        return String.format("COPY \"%s\"", copyTable.getSql(config));
    }

    protected String fromFileSql(SqlAbleConfig config) {
        return String.format(" FROM LOCAL '%s'", fromFile);
    }

    protected String optionsSql(SqlAbleConfig config) {
        return " PARSER fcsvparser(header=false)";
    }
}
