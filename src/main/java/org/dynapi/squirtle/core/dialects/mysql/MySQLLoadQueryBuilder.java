package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.QueryBuilderAttributes;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.Table;

public class MySQLLoadQueryBuilder implements QueryBuilderAttributes, SqlAble {
    public Class<? extends Query> sqlAbleQueryClass() { return MySQLQuery.class; }

    protected String loadFile = null;
    protected Table intoTable = null;

    public MySQLLoadQueryBuilder(MySQLLoadQueryBuilder original) {
        this.loadFile = original.loadFile;
        this.intoTable = CloneUtils.copyConstructorClone(original.intoTable);
    }

    public MySQLLoadQueryBuilder() {

    }

    public MySQLLoadQueryBuilder load(String file) {
        this.loadFile = file;
        return this;
    }

    public MySQLLoadQueryBuilder into(Table table) {
        this.intoTable = table;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = "";
        if (loadFile != null && intoTable != null) {
            sql += loadFileSql(config);
            sql += intoTableSql(config);
            sql += optionsSql(config);
        }
        return sql;
    }

    protected String loadFileSql(SqlAbleConfig ignored) {
        return String.format("LOAD DATA LOCAL INFILE '%s'", loadFile);
    }

    protected String intoTableSql(SqlAbleConfig config) {
        return String.format(" INTO TABLE `%s`", intoTable.getSql(config));
    }

    protected String optionsSql(SqlAbleConfig ignored) {
        return " FIELDS TERMINATED BY ','";
    }
}
